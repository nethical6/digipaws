package nethical.digipaws.utils;
import android.os.SystemClock;

public class DelayManager {
    
    public static boolean isDelayOver(float lastTimeStamp){
		float currentTime = SystemClock.uptimeMillis();
		return currentTime - lastTimeStamp > 30000;
	}
    
    public static boolean isDelayOver(float lastTimeStamp,int delay){
		float currentTime = SystemClock.uptimeMillis();
		return currentTime - lastTimeStamp > delay;
	}
    
    
}