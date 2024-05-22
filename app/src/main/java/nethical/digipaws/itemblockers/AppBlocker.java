package nethical.digipaws.itemblockers;
import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.SharedPreferences;
import nethical.digipaws.utils.DigiConstants;
import nethical.digipaws.utils.DigiUtils;

public class AppBlocker {
    
    public static void performAction(AccessibilityService service,String packageName){
        
        if(isSettingsBlockerOn(service) && packageName==DigiConstants.SETTINGS_PACKAGE_NAME){
            DigiUtils.pressHome(service);
            return;
        }
        
        
        
    }
    
    private static boolean isSettingsBlockerOn(AccessibilityService service){
        SharedPreferences sharedPreferences = service.getSharedPreferences(DigiConstants.PREF_APPBLOCKER_CONFIG_FILE,
		Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(DigiConstants.PREF_APPBLOCKER_SETTINGS_BLOCKER_KEY,false);
    }
}
