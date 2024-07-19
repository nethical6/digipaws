package nethical.digipaws.fragments.quests;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import android.content.pm.PackageManager;
import androidx.fragment.app.Fragment;


import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import nethical.digipaws.R;
import nethical.digipaws.services.FocusModeTimerService;
import nethical.digipaws.utils.SurvivalModeManager;
import nethical.digipaws.views.HollowCircleView;


public class FocusQuest extends Fragment {

    
    private HollowCircleView hollowTimer;
    private boolean isQuestRunning = false;
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.focus_quest, container, false);
		hollowTimer = view.findViewById(R.id.hollow_timer);
		return view;
	}
	
    
    
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		hollowTimer.setTitle("Start");
        if( ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
            makeNotificationPermissionDialog().create().show();
        }
        
        hollowTimer.setOnClickListener((v)->{
             if(isQuestRunning){
               return;
            }
            SurvivalModeManager.enableSurvivalMode(requireContext());
            startTimer();
        });

    }


    @Override
    public void onResume() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getActivity().registerReceiver( timerUpdateReceiver, new IntentFilter("nethical.digipaws.TIMER_UPDATED"), Context.RECEIVER_NOT_EXPORTED);
        } else {
            getActivity().registerReceiver(timerUpdateReceiver,new IntentFilter("nethical.digipaws.TIMER_UPDATED"));
        }
        super.onResume();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        requireContext().unregisterReceiver(timerUpdateReceiver);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void startTimer() {
        Intent serviceIntent = new Intent(requireContext(), FocusModeTimerService.class);
        requireContext().startService(serviceIntent);
    }
    
    public BroadcastReceiver timerUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.hasExtra("time")){
                String time = intent.getStringExtra("time");
                hollowTimer.setTitle(time);
                if(!isQuestRunning){
                    isQuestRunning= true;
                }
            }

                if(intent.hasExtra("over")){
                    isQuestRunning = false;
                    makeQuestCompleteDialog().show();
                }

            }
    };
    
    private MaterialAlertDialogBuilder makeNotificationPermissionDialog(){
        return new MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.missing_permission)
                    .setMessage(R.string.notification_notif_permission)
                    .setNeutralButton("Provide",(dialog,which)->{
                        requestPermissions(
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        0);

                    });
    }

    private MaterialAlertDialogBuilder makeQuestCompleteDialog(){
        return new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Congratulations!!!")
                .setMessage("Remember to come again!")
                .setNeutralButton("Quit",(dialog,which)->{
                    dialog.dismiss();
                    getParentFragmentManager().popBackStack();

                });
    }

    
    
}