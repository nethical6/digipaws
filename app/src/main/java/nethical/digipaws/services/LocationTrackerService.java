package nethical.digipaws.services;

import android.app.LauncherActivity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import androidx.core.app.NotificationManagerCompat;
import nethical.digipaws.Constants;
import nethical.digipaws.R;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;
import android.app.NotificationManager;
import android.os.Handler;
import nethical.digipaws.utils.DigiUtils;
import nethical.digipaws.utils.LocationHelper;
import nethical.digipaws.utils.SurvivalModeConfig;
import org.osmdroid.util.GeoPoint;

public class LocationTrackerService extends Service {

    private static final int NOTIFICATION_ID = 1;
    private LocationHelper liveLocationTracker = null;
    private Location radarLocation=new Location("radar");
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Create a notification
        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), Constants.NOTIFICATION_CHANNEL)
                .setContentTitle("Service Running")
                .setContentText("This service is performing a long-running task")
                .setSmallIcon(R.drawable.paws)
        .setChronometerCountDown(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

       NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
      notificationManager.notify(69, notification.build());
        
        SharedPreferences preferences = this.getSharedPreferences("location_quest", Context.MODE_PRIVATE);
        radarLocation.setLatitude(preferences.getFloat("radar_latitude",0.0f));
        radarLocation.setLongitude(preferences.getFloat("radar_longitude",0.0f));
        
        final Context context = this;
        new Thread(
                        () -> {
                            // Run code on background thread
                            liveLocationTracker = new LocationHelper(getApplicationContext());

                            liveLocationTracker.setLiveLocationListener(
                                    new LocationHelper.LocationListener() {
                                        @Override
                                        public void onLocationChanged(Location location) {
                                            if (location != null) {
                                                GeoPoint currentLocation =
                                                        new GeoPoint(
                                                                location.getLatitude(),
                                                                location.getLongitude());

                                                
                                                if (true) {
                                                    double distance =
                                                            liveLocationTracker
                                                                    .getDistanceBetweenLocations(
                                                                            location,
                                                                            radarLocation);

                                                    if (distance > 100) {
                                        
                                                        
                                                        //SurvivalModeConfig.stop(context);
                                        //DigiUtils.showToast(context,"done");
                                       DigiUtils.sendNotification(context,"Quest done","w");
                                        
                                                    }
                                                }
                                            }
                                        }
                                    });
                            liveLocationTracker.startLocationUpdates();
                        })
                .start();
        
        return START_STICKY; // Redeliver intent if service is killed
    }

    

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true); // Remove notification
    }
}
