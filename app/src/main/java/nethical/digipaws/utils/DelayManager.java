package nethical.digipaws.utils;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;

public class DelayManager {
	
	/*
	blockerId: a unique id for each blocker
	cooldownDelay: time delay between each warning overlays
	*/
		
	public static void updateLastWarningTime(Context context,String blockerId){
		SharedPreferences sharedPreferences = context.getSharedPreferences(blockerId,
		Context.MODE_PRIVATE);
		sharedPreferences.edit().putFloat(DigiConstants.PREF_BLOCKER_LAST_WARNING_TIME_KEY,SystemClock.uptimeMillis()).apply();
	}
	
	public static boolean isWarningDelayOver(Context context,String blockerId){
		SharedPreferences sharedPreferences = context.getSharedPreferences(blockerId,
		Context.MODE_PRIVATE);
		
		float currentTime = SystemClock.uptimeMillis();
		float lastWarningTime = sharedPreferences.getFloat(DigiConstants.PREF_BLOCKER_LAST_WARNING_TIME_KEY,0f);
		
		if(lastWarningTime==0f){
			return true;
		}
		
		float diff = currentTime - lastWarningTime;
		if(diff > 30000){
			return true;
		}
		
		return false;
	}
    
    public static void updateGlobalActionDelay(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(DigiConstants.PREF_GLOBAL_ACTIONS_COOLDOWN_FILE,
		Context.MODE_PRIVATE);
        sharedPreferences.edit().putLong(DigiConstants.PREF_GLOBAL_ACTION_TIME_KEY,SystemClock.uptimeMillis());
    }
    
    public static boolean isGlobalActionCooldownActive(Context context){
       SharedPreferences sharedPreferences = context.getSharedPreferences(DigiConstants.PREF_GLOBAL_ACTIONS_COOLDOWN_FILE,
		Context.MODE_PRIVATE);
        
        if(!sharedPreferences.contains(DigiConstants.PREF_GLOBAL_ACTIONS_COOLDOWN_FILE)){
            return false;
        }
        
        Long diff = sharedPreferences.getLong(DigiConstants.PREF_GLOBAL_ACTION_TIME_KEY,0L) - SystemClock.uptimeMillis();
        if(diff > DigiConstants.GLOBAL_ACTIONS_COOLDOWN_DELAY){
            return true;
        }
        return false;
    }
	
	
	
}