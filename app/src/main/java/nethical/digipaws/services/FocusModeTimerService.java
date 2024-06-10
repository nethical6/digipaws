package nethical.digipaws.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import nethical.digipaws.MainActivity;
import nethical.digipaws.R;
import nethical.digipaws.utils.DigiConstants;
import nethical.digipaws.utils.DigiUtils;
import nethical.digipaws.utils.SurvivalModeManager;

public class FocusModeTimerService extends Service {
    private CountDownTimer countDownTimer; 
    private long timeLeftInMillis = DigiConstants.FOCUS_MODE_LENGTH; // in milliseconds

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(1, getNotification("60:00"));

        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                String timeFormatted = getTimeFormatted(timeLeftInMillis);
                updateNotification(timeFormatted);
                Intent updateUIIntent = new Intent("TIMER_UPDATED");
                updateUIIntent.putExtra("time", timeFormatted);
                sendBroadcast(updateUIIntent);
            }

            @Override
            public void onFinish() {
                stopSelf();
                SurvivalModeManager.disableSurvivalMode(getApplicationContext());
                DigiUtils.sendNotification(getApplicationContext(),"Quest Completed!","You earned 1 Aura Coin.",R.drawable.swords);
            }
        }.start();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Notification getNotification(String time) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        return new NotificationCompat.Builder(this, DigiConstants.NOTIFICATION_CHANNEL)
                .setContentTitle("Focus Mode Running")
                .setContentText("Time Remaining: "+time)
                .setSmallIcon(R.drawable.swords)
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true)
                .setSilent(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();
    }

    private void updateNotification(String time) {
        Notification notification = getNotification(time);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(1, notification);
        }
    }

    private String getTimeFormatted(long millis) {
        int minutes = (int) (millis / 1000) / 60;
        int seconds = (int) (millis / 1000) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
