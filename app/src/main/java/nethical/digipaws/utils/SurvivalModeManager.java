package nethical.digipaws.utils;
import android.content.Context;
import android.content.SharedPreferences;

public class SurvivalModeManager {
    
    public static boolean isSurvivalModeActive(Context context){
       SharedPreferences sharedPreferences = context.getSharedPreferences(DigiConstants.PREF_SURVIVAL_MODE_CONFIG_FILE,
		Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(DigiConstants.PREF_SURVIVAL_MODE_IS_ENABLED_KEY,false);
    }
    
}
