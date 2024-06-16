package nethical.digipaws.fragments;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import com.google.android.material.internal.EdgeToEdgeUtils;
import nethical.digipaws.receivers.AdminReceiver;
import android.content.Context;
import java.util.Date;
import android.content.SharedPreferences;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.text.ParseException;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.TimeUnit;
import nethical.digipaws.R;
import nethical.digipaws.fragments.dialogs.LoadingDialog;
import nethical.digipaws.services.BlockerService;
import nethical.digipaws.utils.DigiConstants;
import nethical.digipaws.utils.DigiUtils;
import nethical.digipaws.utils.LoadAppList;

import java.util.List;

public class HomeFragment extends Fragment {

    private View view;
    private TextView daysRemaining;
    private SharedPreferences sharedPreferences;
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
        EdgeToEdgeUtils.applyEdgeToEdge(getActivity().getWindow(),false);
        view = inflater.inflate(R.layout.home_fragment, container, false);
		daysRemaining = view.findViewById(R.id.anti_uninstall_info);
		return view;
	}
	
    
    
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		LoadingDialog loadingDialog = new LoadingDialog("Reloading packages");
		
		loadingDialog.show(getActivity().getSupportFragmentManager(), "loading_dialog");
		List<String> packages = LoadAppList.getPackageNames(requireContext());
		loadingDialog.dismiss();
        sharedPreferences = getContext().getSharedPreferences(DigiConstants.PREF_APP_CONFIG,Context.MODE_PRIVATE);
        calculateDaysPassed();
	}
    
    
    public void calculateDaysPassed()  {
        
        SimpleDateFormat sdf = new SimpleDateFormat(DigiConstants.DATE_FORMAT, Locale.getDefault());

        // Parse the given date
        Date givenDate = getDate(requireContext());

        // Get the current date
        Date currentDate = new Date();

        // Calculate the difference in milliseconds
        long differenceInMillis = currentDate.getTime() - givenDate.getTime();

        // Convert milliseconds to days
        long days = TimeUnit.DAYS.convert(differenceInMillis, TimeUnit.MILLISECONDS);
        
        daysRemaining.setText(String.valueOf(days) + " "+ getContext().getString(R.string.anti_uninstall_desc_day));
        
        DevicePolicyManager devicePolicyManager =
                        (DevicePolicyManager)
                                requireContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName deviceAdminReceiver =
                        new ComponentName(requireContext(), AdminReceiver.class);

        
        if(days>=DigiConstants.CHALLENGE_TIME){
            daysRemaining.append("\nAnti Uninstall disabled!!");
            sharedPreferences.edit().putBoolean(DigiConstants.PREF_IS_ANTI_UNINSTALL,false).apply();
            if(!devicePolicyManager.isAdminActive(deviceAdminReceiver)){
                    return;
                }
                // Disable the device admin if it was enabled
            if (devicePolicyManager != null && deviceAdminReceiver != null) {
                devicePolicyManager.removeActiveAdmin(deviceAdminReceiver);
            }
        }
    }
    
    public Date getDate(Context context) {
        
        String dateString = sharedPreferences.getString(DigiConstants.PREF_ANTI_UNINSTALL_START, "0001-01-01");

        if (dateString != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(DigiConstants.DATE_FORMAT, Locale.getDefault());
            try {
                return sdf.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        checkAccessibiltyPermission();
    }
    
    private void checkAccessibiltyPermission(){
        if(!DigiUtils.isAccessibilityServiceEnabled(requireContext(),BlockerService.class)){
			Snackbar.make(view, R.string.notification_accessibility_permission, Snackbar.LENGTH_LONG)
			.setAction(R.string.settings, new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
					startActivity(intent);
				}
			}).show();
		}
    }
	
	// Layout file (fragment_my.xml)
	public static int getLayoutResourceId() {
		return R.layout.home_fragment;
	}
	
	
	
}