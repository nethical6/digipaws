package nethical.digipaws.fragments.intro;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.github.appintro.AppIntro;
import com.github.appintro.AppIntro2;
import nethical.digipaws.Intro;
import nethical.digipaws.R;
import nethical.digipaws.utils.DigiConstants;

public class ChooseDelay extends Fragment {

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
        delay.setOnClickListener(v->{
            if(!delay.isEnabled()){
                Toast.makeText(getContext(),"Current selected Mode does not allow tweaking breaktimes.",Toast.LENGTH_SHORT).show();
            }
        });
        
        delay.setMaxValue(180);
        delay.setMinValue(2);
        
        delay.setOnValueChangedListener(new NumberPicker.OnValueChangeListener(){
            @Override
            public void onValueChange(NumberPicker np, int oldnum, int num) {
                    sharedPreferences.edit().putInt(DigiConstants.PREF_DELAY,num*60*1000).apply();
            }
        });

    }
}
