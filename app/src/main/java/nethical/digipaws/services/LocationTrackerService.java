package nethical.digipaws.services;

import android.app.LauncherActivity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.CountDownTimer;
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
                .setContentTitle("Quest Running")
                .setContentText("A quest is running in the background")
                .setSmallIcon(R.drawable.paws)
        .setChronometerCountDown(true)
		.setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

       NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
      notificationManager.notify(69, notification.build());
        
        
        final Context context = this;
		
		
		
		
		
        new Thread(
		
                        () -> {
							
							
                            SharedPreferences preferences = context.getSharedPreferences(Constants.LOC_QUEST_DATA_PREF, Context.MODE_PRIVATE);
							radarLocation.setLatitude(preferences.getFloat(Constants.LOC_QUEST_RADAR_LAT_PREF,0.0f));
							radarLocation.setLongitude(preferences.getFloat(Constants.LOC_QUEST_RADAR_LON_PREF,0.0f));
							
							
							
							
							
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

                                                
                                                
                                                    double distance =
                                                            liveLocationTracker
                                                                    .getDistanceBetweenLocations(
                                                                            location,
                                                                            radarLocation);

                                                    if (distance > Constants.LOC_QUEST_RADAR_RADIUS) {
                                        
                                                        
                                                        SurvivalModeConfig.stop(context);
                                        //DigiUtils.showToast(context,"done");
                                       DigiUtils.sendNotification(context,"Quest Completed!!!","Survival Mode has been disabled");
                                        liveLocationTracker.stopLocationUpdates();
										
										stopForeground(true);
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
