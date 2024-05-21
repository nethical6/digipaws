package nethical.digipaws.utils;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;

public class DelayManager {
	
	/*
	blockerId: a unique id for each blocker
	cooldownDelay: time delay between each warning overlays
	*/
	
	public static void saveInfo(Context context,String blockerId,int cooldownDelay){
		SharedPreferences sharedPreferences = context.getSharedPreferences(blockerId,
		Context.MODE_PRIVATE);
		sharedPreferences.edit().putInt(DigiConstants.PREF_BLOCKER_COOLDOWN_KEY,cooldownDelay).apply();
	}
		
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
	
	public static void setCurrentBlockerDelayTo(Context context,String viewId){
		SharedPreferences sharedPreferences = context.getSharedPreferences(DigiConstants.PREF_BLOCKER_DELAY_FILE,
		Context.MODE_PRIVATE);
		sharedPreferences.edit().putString(DigiConstants.PREF_BLOCKER_DELAY_VIEWID_KEY,viewId).apply();
	}
	
	public static String getCurrentBlockerDelayId(Context context){
		SharedPreferences sharedPreferences = context.getSharedPreferences(DigiConstants.PREF_BLOCKER_DELAY_FILE,
		Context.MODE_PRIVATE);
		return sharedPreferences.getString(DigiConstants.PREF_BLOCKER_DELAY_VIEWID_KEY,"iusearchbtw");
	}
	
}