package nethical.digipaws.ui.settings;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import nethical.digipaws.MyDeviceAdminReceiver;
import nethical.digipaws.R;

public class AdvancedSettings extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final int REQUEST_CODE_ENABLE_ADMIN = 123;

    private SharedPreferences sharedPreferences;
    private DevicePolicyManager devicePolicyManager;
    private ComponentName deviceAdminReceiver;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference_adv, rootKey);

        // Get the default SharedPreferences instance
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
    }

    @Override
    public void onStart() {
        super.onStart();
        // Register the listener to listen for preference changes
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        // Unregister the listener to avoid memory leaks
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("disable_uninstall")) {
            boolean isSwitchOn = sharedPreferences.getBoolean(key, false);
            if (isSwitchOn) {
                // Switch is turned on
                devicePolicyManager = (DevicePolicyManager) requireContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
                deviceAdminReceiver = new ComponentName(requireContext(), MyDeviceAdminReceiver.class);

                // Check if the app is a device administrator
                if (!devicePolicyManager.isAdminActive(deviceAdminReceiver)) {
                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, deviceAdminReceiver);
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Enable device admin to protect app");
                    startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN);
                }
            } else {
                // Switch is turned off
                devicePolicyManager = (DevicePolicyManager) requireContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
                deviceAdminReceiver = new ComponentName(requireContext(), MyDeviceAdminReceiver.class);

                // Disable the device admin if it was enabled
                if (devicePolicyManager != null && deviceAdminReceiver != null) {
                    devicePolicyManager.removeActiveAdmin(deviceAdminReceiver);
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ENABLE_ADMIN) {
            if (resultCode == Activity.RESULT_OK) {
                // Device admin enabled successfully
                Toast.makeText(requireContext(), "Device admin enabled", Toast.LENGTH_SHORT).show();
            } else {
                // Device admin not enabled, turn off the switch and remove admin
                sharedPreferences.edit().putBoolean("disable_uninstall", false).apply();
                if (devicePolicyManager != null && deviceAdminReceiver != null) {
                    devicePolicyManager.removeActiveAdmin(deviceAdminReceiver);
                }
                Toast.makeText(requireContext(), "Failed to enable device admin", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
