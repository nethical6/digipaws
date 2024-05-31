package nethical.digipaws.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import com.google.android.material.snackbar.Snackbar;
import java.util.List;
import nethical.digipaws.Intro;
import nethical.digipaws.R;
import nethical.digipaws.fragments.dialogs.LoadingDialog;
import nethical.digipaws.services.BlockerService;
import nethical.digipaws.services.QuestManagerService;
import nethical.digipaws.utils.DigiConstants;
import nethical.digipaws.utils.DigiUtils;
import nethical.digipaws.utils.LoadAppList;

public class HomeFragment extends Fragment {

    
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.home_fragment, container, false);
		
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		LoadingDialog loadingDialog = new LoadingDialog("Reloading packages");
		
		loadingDialog.show(getActivity().getSupportFragmentManager(), "loading_dialog");
		List<String> packages = LoadAppList.getPackageNames(requireContext());
		loadingDialog.dismiss();
		
        Intent serviceIntent = new Intent(requireContext(), QuestManagerService.class);
        serviceIntent.setAction(DigiConstants.COIN_MANAGER_INCREMENT);
        
        requireContext().startService(serviceIntent);
      
     
        
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(DigiConstants.PREF_APP_CONFIG,Context.MODE_PRIVATE);
        
        if(!sharedPreferences.getBoolean(DigiConstants.PREF_IS_INTRO_SHOWN,false)){
           Intent intent = new Intent(getActivity(),Intro.class);
			startActivity(intent);
            getActivity().finish();
        }
        
        
        if(sharedPreferences.getInt(DigiConstants.PREF_MODE,DigiConstants.DIFFICULTY_LEVEL_EASY)==DigiConstants.DIFFICULTY_LEVEL_NORMAL){
           if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            Toast.makeText(requireContext(),"Location required to perform quest: TOUCH GRASS",Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    69); // Use a unique request code
            }
            
        }
        
		
		
		if(!DigiUtils.isAccessibilityServiceEnabled(requireContext(),BlockerService.class)){
			Snackbar.make(view, R.string.notification_accessibility_permission, Snackbar.LENGTH_LONG)
			.setAction(R.string.settings, new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
					startActivity(intent);
				}
			})
			.show();
		}
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(requireContext())) {
                Toast.makeText(requireContext(),"permission draw over other apps required",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + requireContext().getPackageName()));
                startActivityForResult(intent, 69);
                }
        }
        
        
	}
	
	// Layout file (fragment_my.xml)
	public static int getLayoutResourceId() {
		return R.layout.home_fragment;
	}
	
	
	
}