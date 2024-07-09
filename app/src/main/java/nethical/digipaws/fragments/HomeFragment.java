package nethical.digipaws.fragments;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import androidx.core.content.FileProvider;
import com.google.android.datatransport.BuildConfig;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import android.net.Uri;
import com.google.android.material.internal.EdgeToEdgeUtils;
import java.io.File;
import android.os.Environment;
import java.io.FileOutputStream;
import java.io.IOException;
import nethical.digipaws.MainActivity;
import nethical.digipaws.fragments.dialogs.SelectQuestDialog;
import nethical.digipaws.fragments.intro.ConfigureWarning;
import nethical.digipaws.fragments.quests.FocusQuest;
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
import nethical.digipaws.views.HollowCircleView;

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
        calculateStats();
        
        daysRemaining.setOnClickListener(v->{
            DigiUtils.replaceScreen(getActivity().getSupportFragmentManager(),new ChallengeCompletedFragment());
        });
        
        HollowCircleView hcv = view.findViewById(R.id.select_quest);
        switch(sharedPreferences.getInt(DigiConstants.PREF_MODE,DigiConstants.DIFFICULTY_LEVEL_EASY)){
            case DigiConstants.DIFFICULTY_LEVEL_EASY:
                hcv.setTitle("Focus");
                hcv.setOnClickListener(v->{
                    DigiUtils.replaceScreen(getFragmentManager(),new FocusQuest());
                });
                break;
            case DigiConstants.DIFFICULTY_LEVEL_NORMAL:
                hcv.setOnClickListener(v->{
                    SelectQuestDialog dialog = new SelectQuestDialog();
                    dialog.show(getFragmentManager(), "select_quest"); 
                });
                break;
            
            case DigiConstants.DIFFICULTY_LEVEL_EXTREME:
                hcv.setTitle("Focus");
                hcv.setOnClickListener(v->{
                    DigiUtils.replaceScreen(getFragmentManager(),new FocusQuest());
                });
                break;
        }
        
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
        
        daysRemaining.append(String.valueOf(days) + " "+ getContext().getString(R.string.anti_uninstall_desc_day));
        
        DevicePolicyManager devicePolicyManager =
                        (DevicePolicyManager)
                                requireContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName deviceAdminReceiver =
                        new ComponentName(requireContext(), AdminReceiver.class);

        //if(true){
       if(days >= DigiConstants.CHALLENGE_TIME ){
            if(!devicePolicyManager.isAdminActive(deviceAdminReceiver)){
                    return;
                }
            DigiUtils.sendNotification(requireContext(),"Challenge Completed","Anti-Uninstall has been disabled!!!!!!",R.drawable.swords);
            sharedPreferences.edit().putBoolean(DigiConstants.PREF_IS_ANTI_UNINSTALL,false).apply();
                // Disable the device admin if it was enabled
            
            
            if (devicePolicyManager != null && deviceAdminReceiver != null) {
                devicePolicyManager.removeActiveAdmin(deviceAdminReceiver);
            }
            DigiUtils.replaceScreen(getActivity().getSupportFragmentManager(),new ChallengeCompletedFragment(true,String.valueOf(days)));
            
        }
    }
    
    private void calculateStats(){
        SharedPreferences questPref = requireContext().getSharedPreferences(DigiConstants.PREF_QUEST_INFO_FILE, Context.MODE_PRIVATE);
        daysRemaining.append("\nTotal Pushups: " + String.valueOf(questPref.getInt(DigiConstants.KEY_TOTAL_PUSHUPS,0)) + " reps");
        daysRemaining.append("\nTotal Squats: " + String.valueOf(questPref.getInt(DigiConstants.KEY_TOTAL_SQUATS,0)) + " reps");
        daysRemaining.append("\nTotal Distance Ran: " + String.valueOf(questPref.getInt(DigiConstants.KEY_TOTAL_DISTANCE_RUN,0)) + " metres");
        
        daysRemaining.append("\nTotal Time Focused: " + String.valueOf(questPref.getInt(DigiConstants.KEY_TOTAL_FOCUSED,0)) + " minutes");
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
        daysRemaining.setText("");
        calculateDaysPassed();
        calculateStats();
    }
    
    private void checkAccessibiltyPermission(){
        if(!DigiUtils.isAccessibilityServiceEnabled(requireContext(),BlockerService.class)){
			Snackbar.make(view, R.string.notification_accessibility_permission, Snackbar.LENGTH_LONG)
			.setAction(R.string.settings, new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					buildAccessibiltyDialog();
				}
			}).show();
		}
    }
	
	// Layout file (fragment_my.xml)
	public static int getLayoutResourceId() {
		return R.layout.home_fragment;
	}
	
    private void buildAccessibiltyDialog(){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.missing_permission)
                    .setMessage(R.string.accessibility_perm)
                    .setPositiveButton("Agree",(dialog,which)->{
                        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
					    startActivity(intent);
                        dialog.dismiss();
                    })
                    .setNegativeButton("How DigiPaws processes my data?",(dialog,which)->{
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(DigiConstants.WEBSITE_ROOT+"about/permission"));
                        startActivity(intent);
                        dialog.dismiss();
                    });
                    builder.create().show();
    }
	
    
}