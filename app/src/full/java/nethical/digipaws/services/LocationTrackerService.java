package nethical.digipaws.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.CountDownTimer;
import androidx.core.app.NotificationCompat;
import android.app.NotificationManager;
import android.app.Service;
import android.os.IBinder;
import android.content.Intent;
import nethical.digipaws.R;
import nethical.digipaws.utils.CoinManager;
import nethical.digipaws.utils.DigiConstants;
import nethical.digipaws.utils.LocationManager;

public class LocationTrackerService extends Service implements LocationManager.LocationListener {

    private LocationManager liveLocationTracker;
    private Location radarLocation = new Location("radarLocation");
    
    SharedPreferences questPref;
    
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
        
        if(intent.getAction() == "STOP"){
            stopQuest();
            stopForeground(STOP_FOREGROUND_REMOVE);
            stopSelf();
            
        }
        
        
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
                        .setContentTitle("Quest Running: TOUCH GRASS")
                        .setContentText("Initialized successfully")
                        .setOnlyAlertOnce(true)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

        startForeground(70, builder.build());
  }
  
  private void updateNofification(String title,String content,int priority)  {
      NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, DigiConstants.NOTIFICATION_CHANNEL)
                        .setSmallIcon(R.drawable.swords)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setOnlyAlertOnce(true)
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
        editor.apply();  
    }
   
    

    @Override
    public void onLocationChanged(Location location) {
       distance = liveLocationTracker.getDistanceBetweenLocations(location,radarLocation);
        updateNofification("Quest: Touch Grass","Distance Covered: "+ String.valueOf(distance),NotificationCompat.PRIORITY_DEFAULT);
        if(distance>DigiConstants.RADAR_RADIUS){
            CoinManager.incrementCoin(this);
            updateNofification("Quest Completed","You earned 1 digicoin",NotificationCompat.PRIORITY_MAX);
            liveLocationTracker.stopLocationUpdates();
            stopQuest();
        }
    }
    
}
