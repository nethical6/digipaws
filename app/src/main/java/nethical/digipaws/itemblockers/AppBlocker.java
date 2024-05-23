package nethical.digipaws.itemblockers;
import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.SharedPreferences;
import nethical.digipaws.data.ServiceData;
import nethical.digipaws.utils.DigiConstants;
import nethical.digipaws.utils.DigiUtils;
import nethical.digipaws.utils.SurvivalModeManager;

public class AppBlocker {
    
    public static void performAction(ServiceData data){
        if(isSettingsBlockerOn(data.getService()) && data.getPackageName()==DigiConstants.SETTINGS_PACKAGE_NAME){
            DigiUtils.pressHome(data.getService());
            return;
        }
        if(SurvivalModeManager.isSurvivalModeActive(data.getService())){
            DigiUtils.pressHome(data.getService());
            return;
        }
    }
    
    private static boolean isSettingsBlockerOn(AccessibilityService service){
        SharedPreferences sharedPreferences = service.getSharedPreferences(DigiConstants.PREF_APPBLOCKER_CONFIG_FILE,
		Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(DigiConstants.PREF_APPBLOCKER_SETTINGS_BLOCKER_KEY,false);
    }
}
