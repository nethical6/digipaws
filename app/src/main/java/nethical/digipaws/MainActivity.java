package nethical.digipaws;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Calendar;

import nethical.digipaws.fragments.HomeFragment;
import nethical.digipaws.receivers.ResetCoinsReceiver;
import nethical.digipaws.utils.DigiConstants;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
		
       // SurvivalModeManager.enableSurvivalMode(this);
      // h.setText("hshs");
        
       SharedPreferences sharedPreferences = getSharedPreferences(DigiConstants.PREF_APP_CONFIG,Context.MODE_PRIVATE);
        
       //if(!sharedPreferences.getBoolean(DigiConstants.PREF_IS_INTRO_SHOWN,false)){
        if(false){
           Intent intent = new Intent(this, AppIntroActivity.class);
			startActivity(intent);
            //finish();
        }
       /*
        cointCount = findViewById(R.id.coint_count);
        
        if(sharedPreferences.getInt(DigiConstants.PREF_MODE,DigiConstants.DIFFICULTY_LEVEL_EASY)==DigiConstants.DIFFICULTY_LEVEL_EASY || sharedPreferences.getInt(DigiConstants.PREF_MODE,DigiConstants.DIFFICULTY_LEVEL_EASY)==DigiConstants.DIFFICULTY_LEVEL_NORMAL){
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.missing_permission)
                    .setMessage(R.string.notification_overlay_permission)
                    .setNeutralButton("Provide",(dialog,which)->{
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                        startActivityForResult(intent, 69);
                        dialog.dismiss();
                    });
                    builder.create().show();
        
                }
            }
            
        }
        
        if(sharedPreferences.getInt(DigiConstants.PREF_MODE,DigiConstants.DIFFICULTY_LEVEL_EASY)==DigiConstants.DIFFICULTY_LEVEL_EASY || sharedPreferences.getInt(DigiConstants.PREF_MODE,DigiConstants.DIFFICULTY_LEVEL_EASY)==DigiConstants.DIFFICULTY_LEVEL_EXTREME){
            cointCount.setVisibility(View.GONE);
        }
        */
        
       // setDailyAlarm(this);

		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.fragment_container, new HomeFragment());
		//transaction.addToBackStack(null);
		transaction.commit();
        
        //checkAndRequestBatteryOptimization(this);
        /*
        
        // setup notification channels
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel =
                    new NotificationChannel(DigiConstants.NOTIFICATION_CHANNEL, "Blockers", importance);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }*/
    }




    public void configure(View view){
        if(!BuildConfig.DEBUG){
            SharedPreferences sp = this.getSharedPreferences(DigiConstants.PREF_APP_CONFIG, Context.MODE_PRIVATE);
            if(sp.getBoolean(DigiConstants.PREF_IS_ANTI_UNINSTALL,false)){
                Toast.makeText(this,"Anti Uninstall is active. Cannot change configurations.",Toast.LENGTH_LONG).show();
                return;
            }
        }
        Intent intent = new Intent(this, AppIntroActivity.class);
        startActivity(intent);
        Toast.makeText(this,"Changes take time to reflect",Toast.LENGTH_LONG).show();

    }
    public void info(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(DigiConstants.WEBSITE_ROOT));
        startActivity(intent);
    }
    
    public static void setDailyAlarm(Context context) {
    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    
    Intent intent = new Intent(context, ResetCoinsReceiver.class);
    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

    // Set the alarm to start at midnight
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis());
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    
    // If the time set is before the current time, add one day
    if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
        calendar.add(Calendar.DAY_OF_YEAR, 1);
    }

    // Set exact alarm for the next midnight
    long intervalMillis = AlarmManager.INTERVAL_DAY;
    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 0, pendingIntent);
    }

}
    