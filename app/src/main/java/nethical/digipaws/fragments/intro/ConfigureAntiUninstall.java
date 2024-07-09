package nethical.digipaws.fragments.intro;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.CheckBox;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import nethical.digipaws.R;
import nethical.digipaws.receivers.AdminReceiver;
import nethical.digipaws.utils.DigiConstants;

public class ConfigureAntiUninstall extends Fragment  {

    private static final int REQUEST_CODE_ENABLE_ADMIN = 123;

    private SharedPreferences sharedPreferences;
    private DevicePolicyManager devicePolicyManager;
    private ComponentName deviceAdminReceiver;
    
    private CheckBox enableAntiUninstallCb;
    
    public ConfigureAntiUninstall(SharedPreferences sp){
        sharedPreferences = sp;
    }
    
    @Override
    public View onCreateView(
        LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.choose_preferences_uninstall, container, false);
        enableAntiUninstallCb = view.findViewById(R.id.anti_uninstall_cb);
         return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        Date date = new Date();
       
        SimpleDateFormat sdf = new SimpleDateFormat(DigiConstants.DATE_FORMAT, Locale.getDefault());
        String dateString = sdf.format(date);
      
        //stores date for streak
        if(!sharedPreferences.contains(DigiConstants.PREF_USAGE_STREAK_START)){
           sharedPreferences.edit().putString(DigiConstants.PREF_USAGE_STREAK_START,dateString).apply();
        }
        //stores date for checking if challenge has been completed
        sharedPreferences.edit().putString(DigiConstants.PREF_ANTI_UNINSTALL_START,dateString).apply();
     
        devicePolicyManager =
                        (DevicePolicyManager)
                                requireContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
        deviceAdminReceiver =
                        new ComponentName(requireContext(), AdminReceiver.class);

        enableAntiUninstallCb.setChecked(devicePolicyManager.isAdminActive(deviceAdminReceiver));
        
        enableAntiUninstallCb.setOnCheckedChangeListener((v,isSwitchOn)->{
            if (isSwitchOn) {
                
                // Check if the app is a device administrator
                if (!devicePolicyManager.isAdminActive(deviceAdminReceiver)) {
                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, deviceAdminReceiver);
                    intent.putExtra(
                            DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                            "Enable device admin to prevent uninstall attempts");
                    startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN);
                }
            } else {
                    sharedPreferences.edit().putBoolean(DigiConstants.PREF_IS_ANTI_UNINSTALL,false).apply();
                    if(!devicePolicyManager.isAdminActive(deviceAdminReceiver)){
                        return;
                    }
                // Disable the device admin if it was enabled
                if (devicePolicyManager != null && deviceAdminReceiver != null) {
                    devicePolicyManager.removeActiveAdmin(deviceAdminReceiver);
                }
            }
        });
        
    }
        @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ENABLE_ADMIN) {
            if (resultCode == Activity.RESULT_OK) {
                // Device admin enabled successfully
                Toast.makeText(requireContext(), "Device admin enabled", Toast.LENGTH_SHORT).show();
                sharedPreferences.edit().putBoolean(DigiConstants.PREF_IS_ANTI_UNINSTALL,true).apply();
            } else {
                enableAntiUninstallCb.setChecked(false);
                Toast.makeText(
                                requireContext(),
                                "Failed to enable device admin",
                                Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

   
}