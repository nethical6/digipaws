package nethical.digipaws.blockers;

import android.accessibilityservice.AccessibilityService;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import nethical.digipaws.Constants;
import nethical.digipaws.utils.DigiUtils;
import nethical.digipaws.utils.SurvivalModeConfig;

public class AppBlocker {

    public static void performAction(AccessibilityService service, String packageName) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(service);

        if (preferences == null) {
            preferences = PreferenceManager.getDefaultSharedPreferences(service);
        }
        if (DigiUtils.isCooldownActive(service)) {
            return; // Ignore action if cooldown is active
        }
        boolean settingsBlocked = preferences.getBoolean(Constants.SETTINGS_PREF, false);
        if (settingsBlocked && packageName == "com.android.settings") {
            service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
            DigiUtils.sendNotification(
                    service, "Settings Blocked", "Entering Settings has been blocked");
            return;
        }
        /*
        if (SurvivalModeConfig.isEnabled(service)) {
            service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
            DigiUtils.sendNotification(
                    service, "Survival Mode is Active", "All Apps have been temporarily blocked.");
            return;
        }*/
    }
}
