package nethical.digipaws.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import nethical.digipaws.Constants;
import androidx.core.app.NotificationManagerCompat;
import nethical.digipaws.R;

public class DigiUtils {
    
    public static void showToast(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
    
    public static void sendNotification(Context context,String title,String content){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL)
        .setSmallIcon(R.drawable.paws)
        .setContentTitle(title)
        .setContentText(content)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
      notificationManager.notify(69, builder.build());
    
    }
    
     public static void updateStatCount(Context context,int type) {
        SharedPreferences preferences = context.getSharedPreferences("stats", Context.MODE_PRIVATE);
		switch (type) {
		case Constants.BLOCK_TYPE_VIEW:
			int crntV = preferences.getInt("viewblock_count", 0);
			preferences.edit().putInt("viewblock_count", crntV++).apply();
			break;
		case Constants.BLOCK_TYPE_KEYWORD:
			int crntK = preferences.getInt("keywordblock_count", 0);
			preferences.edit().putInt("keywordblock_count", crntK++).apply();
			break;
		}
	}
    
    public static boolean isCooldownActive(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("cooldown_data", Context.MODE_PRIVATE);
	
		long currentTime = SystemClock.uptimeMillis();
		long lastTimestamp = preferences.getLong(Constants.PREF_COOLDOWN_TIMESTAMP, 0);
		return currentTime - lastTimestamp < Constants.COOLDOWN_DELAY;
	}
    
    public static void startCooldown(Context context) {
       SharedPreferences preferences = context.getSharedPreferences("cooldown_data", Context.MODE_PRIVATE);
		long currentTime = SystemClock.uptimeMillis();
		preferences.edit().putLong(Constants.PREF_COOLDOWN_TIMESTAMP, currentTime).apply();
	}
}
