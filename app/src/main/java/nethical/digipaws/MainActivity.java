package nethical.digipaws;

import android.app.NotificationManager;
import android.app.NotificationChannel;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import android.content.Context;
import nethical.digipaws.fragments.HomeFragment;
import nethical.digipaws.fragments.dialogs.LoadingDialog;
import nethical.digipaws.fragments.dialogs.SelectQuestDialog;
import nethical.digipaws.fragments.quests.MarathonQuest;
import nethical.digipaws.R;
import nethical.digipaws.services.LocationTrackerService;
import nethical.digipaws.utils.CoinManager;
import nethical.digipaws.utils.DigiConstants;
import nethical.digipaws.utils.DigiUtils;
import nethical.digipaws.utils.LoadAppList;
import nethical.digipaws.utils.OverlayManager;
import nethical.digipaws.utils.SurvivalModeManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		
       // SurvivalModeManager.enableSurvivalMode(this);
        
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
        
        // Create a new set and add strings to it
        Set<String> newBlockedAppsSet = new HashSet<>();
        newBlockedAppsSet.add("com.example.myapplication");

        // Save the new set to SharedPreferences
        SharedPreferences sharedPreferences = this.getSharedPreferences(DigiConstants.PREF_BLOCKED_APPS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(DigiConstants.PREF_BLOCKED_APPS_LIST_KEY, newBlockedAppsSet);
        editor.apply();

		
    }
	
	
	public void SelectQuest(View view){
		SelectQuestDialog dialog = new SelectQuestDialog();
		dialog.show(getSupportFragmentManager(), "select_quest"); // Use a unique tag for the dialog
	}
}