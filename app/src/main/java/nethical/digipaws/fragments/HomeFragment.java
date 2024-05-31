package nethical.digipaws.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import nethical.digipaws.R;
import nethical.digipaws.fragments.dialogs.LoadingDialog;
import nethical.digipaws.services.BlockerService;
import nethical.digipaws.utils.DigiUtils;
import nethical.digipaws.utils.LoadAppList;

import java.util.List;

public class HomeFragment extends Fragment {

    private View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
        view = inflater.inflate(R.layout.home_fragment, container, false);
		
		return view;
	}
	
    
    
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		LoadingDialog loadingDialog = new LoadingDialog("Reloading packages");
		
		loadingDialog.show(getActivity().getSupportFragmentManager(), "loading_dialog");
		List<String> packages = LoadAppList.getPackageNames(requireContext());
		loadingDialog.dismiss();
        
        
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