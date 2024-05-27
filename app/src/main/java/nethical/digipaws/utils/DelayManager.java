package nethical.digipaws.utils;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;

public class DelayManager {
    
    public static boolean isDelayOver(float lastTimeStamp){
		float currentTime = SystemClock.uptimeMillis();
		return currentTime - lastTimeStamp > 30000;
	}
    
}