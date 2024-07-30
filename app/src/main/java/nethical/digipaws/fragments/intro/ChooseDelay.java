package nethical.digipaws.fragments.intro;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.heinrichreimersoftware.materialintro.app.SlideFragment;

import nethical.digipaws.R;
import nethical.digipaws.utils.DigiConstants;

public class ChooseDelay extends SlideFragment {

    private NumberPicker delay;
    private SharedPreferences sharedPreferences;
    
    public ChooseDelay(SharedPreferences sp){
        sharedPreferences = sp;
    }
    
    @Override
    public View onCreateView(
        LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.choose_preferences_delay, container, false);
        delay = view.findViewById(R.id.choose_delay);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        if(sharedPreferences.getInt(DigiConstants.PREF_MODE,DigiConstants.DIFFICULTY_LEVEL_EASY) != DigiConstants.DIFFICULTY_LEVEL_EASY){
          delay.setEnabled(false);
        }
        
        delay.setMaxValue(180);
        delay.setMinValue(2);
        
        delay.setOnValueChangedListener((np, oldnum, num) -> sharedPreferences.edit().putInt(DigiConstants.PREF_DELAY,num*60*1000).apply());

    }
}
