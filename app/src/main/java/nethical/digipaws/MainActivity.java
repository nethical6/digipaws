package nethical.digipaws;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import nethical.digipaws.fragments.HomeFragment;
import nethical.digipaws.fragments.dialogs.SelectQuestDialog;
import nethical.digipaws.utils.CoinManager;
import nethical.digipaws.utils.DigiConstants;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		
       // SurvivalModeManager.enableSurvivalMode(this);
        
        
       SharedPreferences sharedPreferences = getSharedPreferences(DigiConstants.PREF_APP_CONFIG,Context.MODE_PRIVATE);
        
        if(!sharedPreferences.getBoolean(DigiConstants.PREF_IS_INTRO_SHOWN,false)){
           Intent intent = new Intent(this,Intro.class);
			startActivity(intent);
            finish();
        }
        
        
        
        
        Button cointCount = findViewById(R.id.coint_count);
        cointCount.setText(" " + String.valueOf(CoinManager.getCoinCount(this)));
        
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.fragment_container, new HomeFragment());
		//transaction.addToBackStack(null);
		transaction.commit();
        
        
        
        // setup notification channels
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel =
                    new NotificationChannel(DigiConstants.NOTIFICATION_CHANNEL, "Blockers", importance);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        
    }
	
	
	public void SelectQuest(View view){
		SelectQuestDialog dialog = new SelectQuestDialog();
		dialog.show(getSupportFragmentManager(), "select_quest"); // Use a unique tag for the dialog
	}
    public void Configure(View view){
		Intent intent = new Intent(this,Intro.class);
		startActivity(intent);
        Toast.makeText(this,"Restarting services required to apply any configuration changes",Toast.LENGTH_LONG).show();
	}
}