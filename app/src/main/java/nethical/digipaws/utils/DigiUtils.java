package nethical.digipaws.utils;

import android.accessibilityservice.AccessibilityService;
import android.app.LauncherActivity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.SystemClock;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import nethical.digipaws.Constants;
import nethical.digipaws.R;

public class DigiUtils {

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    public static void sendNotification(Context context, String title, String content) {
        Intent activityIntent = new Intent(context, LauncherActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        activityIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL)
                        .setSmallIcon(R.drawable.paws)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(69, builder.build());
    }

    public static void updateStatCount(Context context, int type) {
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
        SharedPreferences preferences =
                context.getSharedPreferences("cooldown_data", Context.MODE_PRIVATE);

        long currentTime = SystemClock.uptimeMillis();
        long lastTimestamp = preferences.getLong(Constants.PREF_COOLDOWN_TIMESTAMP, 0);
        return currentTime - lastTimestamp < Constants.COOLDOWN_DELAY;
    }

    public static void startCooldown(Context context) {
        SharedPreferences preferences =
                context.getSharedPreferences("cooldown_data", Context.MODE_PRIVATE);
        long currentTime = SystemClock.uptimeMillis();
        preferences.edit().putLong(Constants.PREF_COOLDOWN_TIMESTAMP, currentTime).apply();
    }

    public static String getStringFromResources(
            Context context, int stringResourceId, int formatArgs) {
        Resources resources = context.getResources();
        String string = resources.getString(stringResourceId);
        return String.format(string, formatArgs);
    }

    public static String getStringFromResources(
            AccessibilityService context, int stringResourceId, int formatArgs) {
        Resources resources = context.getResources();
        String string = resources.getString(stringResourceId);
        return String.format(string, formatArgs);
    }
}
