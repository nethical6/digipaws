package nethical.digipaws.services;

import android.app.Notification;
import nethical.digipaws.MainActivity;
import android.app.PendingIntent;
import androidx.core.app.NotificationCompat;
import android.app.NotificationManager;
import android.app.Service;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.content.Intent;
import android.widget.Toast;
import nethical.digipaws.utils.CoinManager;
import nethical.digipaws.utils.DigiConstants;
import nethical.digipaws.utils.DigiUtils;
import nethical.digipaws.R;

public class QuestManagerService extends Service {

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
    
    @Override
    public int onStartCommand(Intent intent, int arg1, int arg2) {
        if (intent != null && checkCallingOrSelfPermission(DigiConstants.PERMISSION_MANAGE_QUEST) == PackageManager.PERMISSION_GRANTED) {
            handleQuestAction(intent);
        } else {
            Toast.makeText(this, "Digipaws: Permission denied to Manage Quests", Toast.LENGTH_SHORT).show();
        }
        return START_STICKY;
        
    }
    
    private void handleQuestAction(Intent intent){
        if(intent.getAction().equals(DigiConstants.COIN_MANAGER_INCREMENT)){
            updateNotification();
        }
        
    }
    
    
    
    
        private Notification getNotification() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        return new NotificationCompat.Builder(this, DigiConstants.NOTIFICATION_CHANNEL)
                .setContentTitle("DigiCoin Earned")
                .setContentText("You earned 1 Digicoin ")
                .setSmallIcon(R.drawable.swords)
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true)
                .setSilent(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();
    }

    private void updateNotification() {
        Notification notification = getNotification();
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager != null) {
         //   manager.notify(1, notification);
            startForeground(1,notification);
        }
    }
    
}
