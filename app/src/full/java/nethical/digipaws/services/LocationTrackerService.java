package nethical.digipaws.services;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ServiceInfo;
import android.location.Location;
import android.os.Build;
import android.os.CountDownTimer;
import androidx.core.app.ActivityCompat;
import android.content.pm.PackageManager;
import androidx.core.app.NotificationCompat;
import android.app.NotificationManager;
import android.app.Service;
import android.os.IBinder;
import android.content.Intent;
import androidx.core.app.ServiceCompat;
import nethical.digipaws.R;
import nethical.digipaws.utils.CoinManager;
import nethical.digipaws.utils.DigiConstants;
import nethical.digipaws.utils.LocationManager;
import org.osmdroid.util.GeoPoint;

public class LocationTrackerService extends Service implements LocationManager.LocationListener {

    private LocationManager liveLocationTracker;
    private Location radarLocation = new Location("radarLocation");
    private int radius = DigiConstants.RADAR_RADIUS;
    
    private SharedPreferences questPref;
    
    private CountDownTimer countDownTimer;
    private String formattedTime ="00";
    private float distance = 0f;
    
    
    private NotificationManager notificationManager;
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        questPref = getSharedPreferences(DigiConstants.PREF_QUEST_INFO_FILE,
				Context.MODE_PRIVATE);
        
        liveLocationTracker = new LocationManager(this);
     
    }
    

    @Override
    public int onStartCommand(Intent intent, int arg1, int arg2) {
        if (!checkLocationPermissionIsGiven()) return START_NOT_STICKY;
      
        
        showNotification();
        
        
        if(intent.getAction() == "STOP"){
            stopQuest();
            stopForeground(STOP_FOREGROUND_REMOVE);
            stopSelf();
            
        }
        
        radarLocation.setLatitude(intent.getDoubleExtra(DigiConstants.KEY_RADAR_LATITUDE,0D));
        radarLocation.setLongitude(intent.getDoubleExtra(DigiConstants.KEY_RADAR_LONGITUDE,0D));
        radius = intent.getIntExtra("radius",DigiConstants.RADAR_RADIUS);
        
        liveLocationTracker.setLiveLocationListener(this);
        liveLocationTracker.startLocationUpdates();
        
        return START_STICKY;
    }
    
    private void showNotification() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
   
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, DigiConstants.NOTIFICATION_CHANNEL)
                        .setSmallIcon(R.drawable.swords)
                        .setContentTitle("Quest Running: TOUCH GRASS")
                        .setContentText("Initialized successfully")
                        .setOnlyAlertOnce(true)
                        .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

        startForeground(1, builder.build(),ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION);
  }
  
  private void updateNofification(String title,String content,int priority)  {
      NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, DigiConstants.NOTIFICATION_CHANNEL)
                        .setSmallIcon(R.drawable.swords)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setOnlyAlertOnce(true)
                        .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
                        .setSilent(true)
                        .setPriority(priority);
        notificationManager.notify(70,builder.build());
  }
    
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        liveLocationTracker.stopLocationUpdates();
    }
    
    private void stopQuest(){
        SharedPreferences.Editor editor = questPref.edit();
        editor.putString(DigiConstants.PREF_QUEST_ID_KEY,DigiConstants.QUEST_ID_NULL);
        editor.putBoolean(DigiConstants.PREF_IS_QUEST_RUNNING_KEY,false);
        editor.putInt(DigiConstants.KEY_TOTAL_DISTANCE_RUN,questPref.getInt(DigiConstants.KEY_TOTAL_DISTANCE_RUN,0)+radius);
        editor.apply();  
    }
   
    

    @Override
    public void onLocationChanged(Location location) {
        if(location==null){return;}
        Intent updateUIIntent = new Intent("LOCATION_UPDATES");
       
        updateUIIntent.putExtra("clatitude", location.getLatitude());
        updateUIIntent.putExtra("clongitude", location.getLongitude());
        updateUIIntent.putExtra("rlatitude", radarLocation.getLatitude());
        updateUIIntent.putExtra("rlongitude",radarLocation.getLongitude());
        sendBroadcast(updateUIIntent);
        
        distance = liveLocationTracker.getDistanceBetweenLocations(location,radarLocation);
        updateNofification("Quest: Touch Grass","Distance Covered: "+ String.valueOf(distance),NotificationCompat.PRIORITY_DEFAULT);
   
        
        if(distance>radius){
            CoinManager.incrementCoin(this);
            updateNofification("Quest Completed","You earned 1 digicoin",NotificationCompat.PRIORITY_MAX);
            liveLocationTracker.stopLocationUpdates();
            stopQuest();
            Intent questComplete = new Intent("MARATHON_QUEST_COMPLETE");
            sendBroadcast(questComplete);
            
        }
        
    }
    private boolean checkLocationPermissionIsGiven() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED;
    } else {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
}

    
}
