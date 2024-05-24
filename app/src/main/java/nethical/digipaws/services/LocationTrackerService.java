package nethical.digipaws.services;

import android.app.NotificationChannel;
import android.app.Notification;
import android.content.Context;
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
    private Location radarLocation = new Location("radarLocation");
    
    private NotificationManager notificationManager;
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
    public int onStartCommand(Intent intent, int arg1, int arg2) {
        
        radarLocation.setLatitude(intent.getDoubleExtra(DigiConstants.KEY_RADAR_LATITUDE,0D));
        radarLocation.setLongitude(intent.getDoubleExtra(DigiConstants.KEY_RADAR_LONGITUDE,0D));
        
        
        liveLocationTracker.setLiveLocationListener(this);
        liveLocationTracker.startLocationUpdates();
        showNotification();
        
        return START_NOT_STICKY;
    }
    
    private void showNotification() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
   
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, DigiConstants.NOTIFICATION_CHANNEL)
                        .setSmallIcon(R.drawable.swords)
                        .setContentTitle("Location Quest Running")
                        .setContentText("You have x remaining time")
                        .setOnlyAlertOnce(true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        startForeground(70, builder.build());
  }
  
  private void updateNofification(String content)  {
      NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, DigiConstants.NOTIFICATION_CHANNEL)
                        .setSmallIcon(R.drawable.swords)
                        .setContentTitle("Location Quest Running")
                        .setContentText(content)
                        .setOnlyAlertOnce(true)
                        .setSilent(true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationManager.notify(70,builder.build());
  }
    
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        liveLocationTracker.stopLocationUpdates();
    }
   
    
    

    @Override
    public void onLocationChanged(Location location) {
       float distance = liveLocationTracker.getDistanceBetweenLocations(location,radarLocation);
        updateNofification("distance: "+ String.valueOf(distance));
    }
    
}
