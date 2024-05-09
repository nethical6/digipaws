package nethical.digipaws;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import nethical.digipaws.ui.LauncherHomeFragment;
import nethical.digipaws.ui.SettingsFragment;

public class LauncherActivity extends AppCompatActivity {

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel =
                    new NotificationChannel(Constants.NOTIFICATION_CHANNEL, "Blockers", importance);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean launcherEnabled = preferences.getBoolean(Constants.LAUNCHER_PREF, false);

        if (launcherEnabled) {
            Fragment launcherFragment = new LauncherHomeFragment();
            transaction.replace(R.id.fragment_container, launcherFragment);

        } else {
            Fragment settingsFragment = new SettingsFragment();
            transaction.replace(R.id.fragment_container, settingsFragment);
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RESULT_ADMIN_PERM) {
            if (resultCode == RESULT_OK) {
                showToast("done");
                preferences.edit().putBoolean("disable_uninstall", true).apply();
                showToast("failed to enable");
                // Device admin enabled successfully
            } else {
                preferences.edit().putBoolean("disable_uninstall", false).apply();
                showToast("failed to enable");
                // Device admin not enabled, handle accordingly
            }
        }
    }
}
