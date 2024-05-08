package nethical.digipaws.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
import androidx.preference.PreferenceManager;
import com.google.gson.Gson;
import java.util.Calendar;
import java.util.Map;
import nethical.digipaws.blockers.ViewBlocker;
import nethical.digipaws.utils.data.BlockableView;
import nethical.digipaws.utils.data.SurvivalModeData;

public class SurvivalModeConfig {
  private static final String PREFS_NAME = "SurvivalModePrefs";
  private static final String DATA_KEY = "survival_mode_data";

  // Save SurvivalModeData object to SharedPreferences
  public static void start(Context context, SurvivalModeData data) {
    SharedPreferences sharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    Gson gson = new Gson();
    String json = gson.toJson(data);
    editor.putString(DATA_KEY, json);
    editor.apply();
  }

  public static void stop(Context context) {
    SurvivalModeData sd = getInfo(context);
    sd.setEnabled(false);

    resetCounter(context);

    start(context, sd);
  }

  // Retrieve SurvivalModeData object from SharedPreferences
  public static SurvivalModeData getInfo(Context context) {
    SharedPreferences sharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    Gson gson = new Gson();
    String json = sharedPreferences.getString(DATA_KEY, null);
    return gson.fromJson(json, SurvivalModeData.class);
  }

  public static boolean isEnabled(Context context) {
    SharedPreferences sharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    Gson gson = new Gson();
    String json = sharedPreferences.getString(DATA_KEY, null);
    SurvivalModeData data = gson.fromJson(json, SurvivalModeData.class);
    if (data == null) {
      return false;
    }
    if (data.isEnabled()) {
      return true;
    } else {
      return false;
    }
  }

  public static String getUnlockTime(Context context) {
    SharedPreferences sharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    Gson gson = new Gson();
    String json = sharedPreferences.getString(DATA_KEY, null);
    SurvivalModeData data = gson.fromJson(json, SurvivalModeData.class);
    if (data == null) {
      return "00:00";
    }
    return data.getEndTime();
  }

  public static String generateEndTime(Context context, int hours, int mins) {
    Calendar calendar = Calendar.getInstance();
    int crnt_hour = calendar.get(Calendar.HOUR_OF_DAY); // 24-hour format
    int crnt_minute = calendar.get(Calendar.MINUTE);

    int end_hour = crnt_hour + hours;
    int end_minute = crnt_minute + mins;

    // Adjust end_hour and end_minute if they exceed 24 hours or 60 minutes
    if (end_minute >= 60) {
      end_hour += end_minute / 60;
      end_minute %= 60;
    }
    if (end_hour >= 24) {
      end_hour %= 24;
    }

    String end_time_hh = String.format("%02d", end_hour);
    String end_time_mm = String.format("%02d", end_minute);

    String final_end_time = end_time_hh + ":" + end_time_mm;
    return final_end_time;
  }

  public static void resetCounter(Context context) {

    SurvivalModeData data = getInfo(context);
    if (data == null) {
      return;
    }

    String packageName = data.getPackageName();
    String ViewId = data.getViewIdName();

    if (ViewId == "null" || packageName == "null") {
      return;
    }

    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

    try {
      Map<String, BlockableView> blocker_data = ViewBlocker.getPackageMap(preferences, packageName);
      blocker_data.get(ViewId).resetCounter();
      ViewBlocker.saveMapToPreferences(context, packageName, blocker_data);

    } catch (Exception e) {
      Log.d("viee", ViewId + ":: " + e.toString());
    }
  }

  public static void setLocation(Context context, Double latitude, Double longitude) {
    SurvivalModeData sd = getInfo(context);
    sd.setLatitude(latitude);
    sd.setLongitude(longitude);
    start(context, sd);
  }
}
