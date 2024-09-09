package nethical.digipaws.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import nethical.digipaws.MainActivity;
import nethical.digipaws.R;
import nethical.digipaws.utils.CoinManager;
import nethical.digipaws.utils.DigiConstants;
import nethical.digipaws.utils.DigiUtils;
import nethical.digipaws.utils.SurvivalModeManager;

public class FocusModeTimerService extends Service {
    private CountDownTimer countDownTimer;
    private long sessionLength = DigiConstants.FOCUS_MODE_LENGTH; // in milliseconds
    private final Context context = this;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.hasExtra("session_length")) {
            sessionLength = intent.getIntExtra("session_length", DigiConstants.FOCUS_MODE_LENGTH);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(1, getNotification("90:00"), ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE);
        } else {
            startForeground(1, getNotification("90:00"));
        }

        countDownTimer = new CountDownTimer(sessionLength, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String timeFormatted = getTimeFormatted(millisUntilFinished);
                Intent updateUIIntent = new Intent("nethical.digipaws.TIMER_UPDATED");
                updateUIIntent.setPackage(getPackageName());
                updateUIIntent.putExtra("time", timeFormatted);
                sendBroadcast(updateUIIntent);

                updateNotification(timeFormatted);
            }

            @Override
            public void onFinish() {
                Intent updateUIIntent = new Intent("nethical.digipaws.TIMER_UPDATED");
                updateUIIntent.setPackage(getPackageName());
                updateUIIntent.putExtra("over", true);
                sendBroadcast(updateUIIntent);

                if (sessionLength == DigiConstants.FOCUS_MODE_LENGTH) {
                    CoinManager.incrementCoin(context);
                }

                SurvivalModeManager.disableSurvivalMode(getApplicationContext());
                DigiUtils.sendNotification(getApplicationContext(), "Quest Completed!", "", R.drawable.swords);
                SharedPreferences questPref = getSharedPreferences(
                        DigiConstants.PREF_QUEST_INFO_FILE, Context.MODE_PRIVATE);
                questPref.edit().putInt(DigiConstants.KEY_TOTAL_FOCUSED, questPref.getInt(DigiConstants.KEY_TOTAL_FOCUSED, DigiConstants.DEFAULT_FOCUSED) + Math.round((float) sessionLength / 60000)).apply();
                questPref.edit().putString(DigiConstants.PREF_QUEST_ID_KEY, DigiConstants.QUEST_ID_NULL).apply();
                stopSelf();
            }
        }.start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        SurvivalModeManager.disableSurvivalMode(getApplicationContext());
        SharedPreferences questPref = getSharedPreferences(
                DigiConstants.PREF_QUEST_INFO_FILE, Context.MODE_PRIVATE);

        questPref.edit().putString(DigiConstants.PREF_QUEST_ID_KEY, DigiConstants.QUEST_ID_NULL).apply();
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
                .setContentText("Time Remaining: " + time)
                .setSmallIcon(R.drawable.swords)
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true)
                .setSilent(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .build();
    }

    private void updateNotification(String time) {
        Notification notification = getNotification(time);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(1, notification);
        }
    }

    @SuppressLint("DefaultLocale")
    private String getTimeFormatted(long millis) {
        int minutes = (int) (millis / 1000) / 60;
        int seconds = (int) (millis / 1000) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
