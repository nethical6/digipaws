package nethical.digipaws.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.LauncherActivityInfo;
import android.content.pm.LauncherApps;
import android.os.UserHandle;
import android.os.UserManager;

import java.text.Collator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nethical.digipaws.data.BlockerData;

public class LoadAppList {

	private static boolean cacheExists(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(DigiConstants.PREF_PACKAGES_FILE,
				Context.MODE_PRIVATE);
		Set<String> packageNames = sharedPreferences.getStringSet(DigiConstants.PREF_PACKAGES_KEY, new HashSet<>());

        return !packageNames.isEmpty();
	}

	// Retrieve the list of package names
	public static List<String> getPackageNames(Context context) {
		if (cacheExists(context)) {
			SharedPreferences sharedPreferences = context.getSharedPreferences(DigiConstants.PREF_PACKAGES_FILE,
					Context.MODE_PRIVATE);
			return new ArrayList<>(sharedPreferences.getStringSet(DigiConstants.PREF_PACKAGES_KEY, new HashSet<>()));
		}
		return loadAndSavePackages(context);
	}

	public static List<String> loadAndSavePackages(Context context) {
		List<String> packageList = loadPackages(context);

		SharedPreferences sharedPreferences = context.getSharedPreferences(DigiConstants.PREF_PACKAGES_FILE,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putStringSet(DigiConstants.PREF_PACKAGES_KEY, new HashSet<>(packageList));
		editor.apply();

		return packageList;
	}

	private static List<String> loadPackages(Context context) {
		List<String> packageList = new ArrayList<>();

		UserManager userManager = (UserManager) context.getSystemService(Context.USER_SERVICE);
		LauncherApps launcherApps = (LauncherApps) context.getSystemService(Context.LAUNCHER_APPS_SERVICE);
		Collator collator = Collator.getInstance();

		List<UserHandle> userProfiles = userManager.getUserProfiles();
		for (UserHandle profile : userProfiles) {
			List<LauncherActivityInfo> activityInfos = launcherApps.getActivityList(null, profile);
			for (LauncherActivityInfo app : activityInfos) {
				String packageName = app.getComponentName().getPackageName();
				packageList.add(packageName);
			}
		}

		return removeIgnoredPackages(context, packageList);
	}

	public static boolean isSystemPackage(ApplicationInfo pkgInfo) {
		return ((pkgInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
	}

	private static List<String> removeIgnoredPackages(Context context,List<String> packages) {
		
		for (String value : BlockerData.nonBlockedPackages.keySet()) {
            packages.remove(value);
		}
		SharedPreferences config = context.getSharedPreferences(DigiConstants.PREF_APP_CONFIG, Context.MODE_PRIVATE);

		for (String value : config.getStringSet(DigiConstants.PREF_WHITELISTED_APPS_LIST_KEY,new HashSet<>())){
			packages.remove(value);
		}
		return packages;
	}
	
}