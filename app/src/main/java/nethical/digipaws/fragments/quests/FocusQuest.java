package nethical.digipaws.fragments.quests;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;


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
        hollowTimer.setOnClickListener((v)->{
            if(isQuestRunning){
               return;
            }
            SurvivalModeManager.enableSurvivalMode(requireContext());
            startTimer();
        });
        
	}
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        context.registerReceiver(timerUpdateReceiver, new IntentFilter("TIMER_UPDATED"));
    }
    
    
    private void startTimer() {
        Intent serviceIntent = new Intent(requireContext(), FocusModeTimerService.class);
        requireContext().startService(serviceIntent);
    }
    
    private BroadcastReceiver timerUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("TIMER_UPDATED")) {
                String time = intent.getStringExtra("time");
                hollowTimer.setTitle(time);
                if(isQuestRunning==false){
                    isQuestRunning= true;
                }
                
            }
        }
    };
    
}