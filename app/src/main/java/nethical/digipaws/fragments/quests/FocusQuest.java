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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import nethical.digipaws.R;
import nethical.digipaws.fragments.HomeFragment;
import nethical.digipaws.services.FocusModeTimerService;
import nethical.digipaws.utils.DigiConstants;
import nethical.digipaws.utils.DigiUtils;
import nethical.digipaws.utils.SurvivalModeManager;


public class FocusQuest extends Fragment {

    
    private TextView timer;
    private boolean isQuestRunning = false;
    private NumberPicker setFTime;
    private LinearLayout selectTimeLayout;
    private int selectedFocusTime = 1;
    private Button startFocus;

    SharedPreferences questInfo;

    private TextView mins;
    private int minsClickCounter = 0;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.focus_quest, container, false);
		timer = view.findViewById(R.id.timer);
        setFTime = view.findViewById(R.id.choose_ftime);
        selectTimeLayout = view.findViewById(R.id.focus_set_time_layout);
        startFocus = view.findViewById(R.id.btn_start_focus);
        mins = view.findViewById(R.id.mins_txt);
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
            makeQuestInfoDialog(questInfo).setCancelable(false).show();
        }
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(DigiConstants.PREF_APP_CONFIG, Context.MODE_PRIVATE);
        int mode = sharedPreferences.getInt(DigiConstants.PREF_MODE, DigiConstants.DIFFICULTY_LEVEL_EASY);
        if (mode == DigiConstants.DIFFICULTY_LEVEL_NORMAL) {
            selectedFocusTime = 90; // tweak to change focus mode adv timing
            startFocus.setVisibility(View.VISIBLE);
            selectTimeLayout.setVisibility(View.GONE);
        } else {
            startFocus.setVisibility(View.VISIBLE);
            selectTimeLayout.setVisibility(View.VISIBLE);
        }
        setFTime.setMaxValue(180);
        setFTime.setMinValue(1);

        setFTime.setOnValueChangedListener((np, oldnum, num) -> selectedFocusTime = num);


        startFocus.setOnClickListener(v -> {
            makeQuestWarningDialog().show();
        });
        mins.setOnClickListener(v -> {
            minsClickCounter++;
            if(minsClickCounter==6){
                Toast.makeText(requireContext(),"Press 4 times more to force exit focus mode", Toast.LENGTH_SHORT).show();;
            }
            if(minsClickCounter>10){
                SurvivalModeManager.disableSurvivalMode(requireContext());
                DigiUtils.replaceScreenWithoutAddingToBackStack(getParentFragmentManager(),new HomeFragment());
                SharedPreferences questPref = requireContext().getSharedPreferences(
                        DigiConstants.PREF_QUEST_INFO_FILE, Context.MODE_PRIVATE);

                questPref.edit().putString(DigiConstants.PREF_QUEST_ID_KEY,DigiConstants.QUEST_ID_NULL).apply();

            }
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
        serviceIntent.putExtra("session_length",selectedFocusTime*60000);
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
                    getParentFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                if(startFocus.getVisibility() == View.VISIBLE){
                    startFocus.setVisibility(View.GONE);
                    selectTimeLayout.setVisibility(View.GONE);
                    timer.setVisibility(View.VISIBLE);
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
                .setTitle("Quest Complete")
                .setMessage("Keep going King!!")
                .setNeutralButton("Quit",(dialog,which)->{
                    dialog.dismiss();
                    DigiUtils.replaceScreenWithoutAddingToBackStack(getParentFragmentManager(),new HomeFragment());
                });
    }
    private MaterialAlertDialogBuilder makeQuestInfoDialog(SharedPreferences pref){
        return new MaterialAlertDialogBuilder(requireContext())
                .setTitle("The Focus Quest")
                .setMessage(R.string.focus_desc)
                .setNeutralButton("Close",(dialog,which)->{
                    dialog.dismiss();
                    pref.edit().putBoolean(DigiConstants.PREF_QUEST_FOCUS_DESCRIBE_DIALOG_KEY,true).apply();;
                });
    }

    private MaterialAlertDialogBuilder makeQuestWarningDialog(){
        return new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Warning")
                .setMessage("Are you sure you want to proceed? You wont be able to access applications on your device for the next "+ String.valueOf(selectedFocusTime)+" minutes. Remember that this quest cannot be stopped once started!!!")
                .setPositiveButton("Proceed",(dialog,which)->{
                    if(startFocus.getVisibility() == View.GONE){
                        dialog.dismiss();
                        Toast.makeText(requireContext(),"Already Focusing!!",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    startTimer();
                    if(isQuestRunning){
                        return;
                    }
                    questInfo.edit().putString(DigiConstants.PREF_QUEST_ID_KEY,DigiConstants.QUEST_ID_FOCUS).apply();
                    SurvivalModeManager.enableSurvivalMode(requireContext());
                    startFocus.setVisibility(View.GONE);
                    selectTimeLayout.setVisibility(View.GONE);
                    timer.setVisibility(View.VISIBLE);
                    dialog.dismiss();

                })
                .setNegativeButton("Dismiss", (dialog,which)-> {
                    dialog.dismiss();
                });

    }

    
    
}