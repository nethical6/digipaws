package nethical.digipaws.services;

import android.app.NotificationChannel;
import android.app.Notification;
import android.location.Location;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import android.app.NotificationManager;
import android.app.Service;
import android.os.IBinder;
import android.content.Intent;
import nethical.digipaws.R;
import nethical.digipaws.utils.DigiConstants;
import nethical.digipaws.utils.LocationManager;

public class LocationTrackerService extends Service implements LocationManager.LocationListener {

    private LocationManager liveLocationTracker;
    
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        liveLocationTracker = new LocationManager(this);
    }
    

    @Override
    public int onStartCommand(Intent arg0, int arg1, int arg2) {
        liveLocationTracker.setLiveLocationListener(this);
        liveLocationTracker.startLocationUpdates();
        showNotification();
        
        return START_NOT_STICKY;
    }
    
    private void showNotification() {
    NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, DigiConstants.NOTIFICATION_CHANNEL)
                        .setSmallIcon(R.drawable.swords)
                        .setContentTitle("title")
                        .setContentText("content")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

    startForeground(70, builder.build());
  }
    
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        liveLocationTracker.stopLocationUpdates();
    }
   
    
    

    @Override
    public void onLocationChanged(Location location) {
        
    }
    
}
