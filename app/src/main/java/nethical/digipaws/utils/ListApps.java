package nethical.digipaws.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.LauncherActivityInfo;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListApps {

  private static final String PREFS_NAME = "AppListPrefs";
  private static final String PREF_APP_LIST = "CachedAppList";

  public static void getUserAccessibleApps(Context context, AppLoadListener listener) {
    SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

    // Check if cached app list exists in SharedPreferences
    String cachedAppListJson = preferences.getString(PREF_APP_LIST, null);
    if (cachedAppListJson != null) {
      // Cached app list exists, parse and return it
      List<AppModel> cachedAppList = parseAppListFromJson(cachedAppListJson);
      listener.onAppsLoaded(cachedAppList);
    } else {
      // Update the cache and return the updated list
      List<AppModel> updatedAppList = updateCache(context);
      listener.onAppsLoaded(updatedAppList);
    }
  }

  public static boolean cacheExists(Context context) {
    SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

    // Check if cached app list exists in SharedPreferences
    String cachedAppListJson = preferences.getString(PREF_APP_LIST, null);
    if (cachedAppListJson != null) {
      return true;
    }
    return false;
  }

  public static List<AppModel> loadFromCache(Context context) {
    SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

    // Check if cached app list exists in SharedPreferences
    String cachedAppListJson = preferences.getString(PREF_APP_LIST, null);
    if (cachedAppListJson != null) {
      // Cached app list exists, parse and return it
      return parseAppListFromJson(cachedAppListJson);
    } else {
      return updateCache(context);
    }
  }

  public static List<AppModel> updateCache(Context context) {
    List<AppModel> appList = new ArrayList<>();
    SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

    UserManager userManager = (UserManager) context.getSystemService(Context.USER_SERVICE);
    LauncherApps launcherApps =
        (LauncherApps) context.getSystemService(Context.LAUNCHER_APPS_SERVICE);
    Collator collator = Collator.getInstance();

    List<UserHandle> userProfiles = userManager.getUserProfiles();
    for (UserHandle profile : userProfiles) {
      List<LauncherActivityInfo> activityInfos = launcherApps.getActivityList(null, profile);
      for (LauncherActivityInfo app : activityInfos) {
        String packageName = app.getComponentName().getPackageName();
        String className = app.getComponentName().getClassName();
        String appLabelShown = app.getLabel().toString();

        if ("nethical.digipaws".equals(packageName)) {
          appLabelShown = "Digipaws Settings";
        }
        appList.add(new AppModel(appLabelShown, packageName, className));
      }
    }

    // Sort the app list alphabetically by app label
    Collections.sort(
        appList,
        new Comparator<AppModel>() {
          @Override
          public int compare(AppModel app1, AppModel app2) {
            return app1.getAppLabel().toLowerCase().compareTo(app2.getAppLabel().toLowerCase());
          }
        });

    // Cache the app list in SharedPreferences
    SharedPreferences.Editor editor = preferences.edit();
    String cachedAppListJson = preferences.getString(PREF_APP_LIST, null);
    String newList = convertAppListToJson(appList);
    if (cachedAppListJson != newList) {
      editor.putString(PREF_APP_LIST, newList);
      editor.apply();
    }
    return appList;
  }

  public static String[] getPackageNames(Context context) {
    List<AppModel> appList = loadFromCache(context);

    List<String> packageNamesList = new ArrayList<>();
    for (AppModel appModel : appList) {
      packageNamesList.add(appModel.getPackageName());
    }
    return packageNamesList.toArray(new String[0]);
  }

  public static String[] getNonIgnoredPackageNames(Context context) {

    String[] array1 = {
      "nethical.digipaws",
      "com.android.settings",
      "com.google.android.dialer",
      "com.google.android.deskclock",
      "android",
      "com.google.android.gm",
      "com.google.android.calendar",
      "com.android.phone",
      "com.google.android.cellbroadcastreceiver",
      "com.android.systemui"
    };
    String[] array2 = getPackageNames(context);

    // Convert array2 to ArrayList for easier removal
    ArrayList<String> list2 = new ArrayList<>(Arrays.asList(array2));

    // Loop through array1
    for (String value : array1) {
      // If value from array1 is found in array2, remove it
      if (list2.contains(value)) {
        list2.remove(value);
      }
    }

    // Convert back ArrayList to array
    return list2.toArray(new String[0]);
  }

  private static String convertAppListToJson(List<AppModel> appList) {
    Gson gson = new Gson();
    return gson.toJson(appList);
  }

  private static List<AppModel> parseAppListFromJson(String json) {
    Gson gson = new Gson();
    Type type = new TypeToken<List<AppModel>>() {}.getType();
    return gson.fromJson(json, type);
  }

  public static class AppModel {
    private String appLabel;
    private String packageName;
    private String className;
    private boolean isBlocked = false;

    public AppModel(String appLabel, String packageName, String className) {
      this.appLabel = appLabel;
      this.packageName = packageName;
      this.className = className;
    }

    public String getAppLabel() {
      return appLabel;
    }

    public String getPackageName() {
      return packageName;
    }

    public String getClassName() {
      return className;
    }

    public boolean isAppBlocked() {
      return isBlocked;
    }

    public void setAppLabel(String name) {
      appLabel = name;
    }

    public void setBlocked(Boolean block) {
      isBlocked = block;
    }
  }

  public interface AppLoadListener {
    void onAppsLoaded(List<AppModel> appList);
  }
}
