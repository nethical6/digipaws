package nethical.digipaws.fragments.quests;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import android.content.pm.PackageManager;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import nethical.digipaws.R;
import nethical.digipaws.services.FocusModeTimerService;
import nethical.digipaws.utils.DigiConstants;
import nethical.digipaws.utils.SurvivalModeManager;


public class FocusQuest extends Fragment {

    
    private TextView timer;
    private boolean isQuestRunning = false;
    private NumberPicker setFTime;
    private int selectedFocusTime = 90;

    SharedPreferences questInfo;
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.focus_quest, container, false);
		timer = view.findViewById(R.id.timer);
        setFTime = view.findViewById(R.id.choose_ftime);
		return view;
	}
	
    
    
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
        if( ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
            makeNotificationPermissionDialog().create().show();
        }
        questInfo = requireContext().getSharedPreferences(DigiConstants.PREF_QUEST_INFO_FILE,Context.MODE_PRIVATE);
        if(!questInfo.getBoolean(DigiConstants.PREF_QUEST_FOCUS_DESCRIBE_DIALOG_KEY,false)){
            showQuestInfoDialog(questInfo).setCancelable(false).show();
        }
        setFTime.setMaxValue(180);
        setFTime.setMinValue(30);

        timer.setOnClickListener((v)->{
             if(isQuestRunning){
               return;
            }
             questInfo.edit().putString(DigiConstants.PREF_QUEST_ID_KEY,DigiConstants.QUEST_ID_FOCUS).apply();
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
                timer.setText(time);
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
    private MaterialAlertDialogBuilder showQuestInfoDialog(SharedPreferences pref){
        return new MaterialAlertDialogBuilder(requireContext())
                .setTitle("The Focus Quest!!!")
                .setMessage(R.string.focus_desc)
                .setNeutralButton("Close",(dialog,which)->{
                    dialog.dismiss();
                    pref.edit().putBoolean(DigiConstants.PREF_QUEST_FOCUS_DESCRIBE_DIALOG_KEY,true).apply();;
                });
    }


    
    
}