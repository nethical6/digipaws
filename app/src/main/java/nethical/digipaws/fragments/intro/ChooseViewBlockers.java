package nethical.digipaws.fragments.intro;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;

import com.heinrichreimersoftware.materialintro.app.SlideFragment;

import nethical.digipaws.R;
import nethical.digipaws.utils.DigiConstants;

public class ChooseViewBlockers extends SlideFragment {

    private final SharedPreferences sharedPreferences;

    public ChooseViewBlockers(SharedPreferences sp){
        sharedPreferences = sp;
    }
    
    @Override
    public View onCreateView(
        LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.choose_preferences_view_blockers, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CheckBox isPornDisabled = view.findViewById(R.id.porn_cb);
        CheckBox isEngmntDisabled = view.findViewById(R.id.engmnt_cb);
        CheckBox isShortsDisabled = view.findViewById(R.id.shorts_cb);
        CheckBox doNotBlockTheFirstReel = view.findViewById(R.id.shorts_allow_first_short_cb);

        isPornDisabled.setChecked(sharedPreferences.getBoolean(DigiConstants.PREF_IS_PORN_BLOCKED,false));
        isShortsDisabled.setChecked(sharedPreferences.getBoolean(DigiConstants.PREF_IS_SHORTS_BLOCKED,false));
        isEngmntDisabled.setChecked(sharedPreferences.getBoolean(DigiConstants.PREF_IS_ENGMMT_BLOCKED,false));
        doNotBlockTheFirstReel.setChecked(sharedPreferences.getBoolean(DigiConstants.PREF_IS_PREVENT_BLOCKING_FIRST_REEL,false));

        if(isShortsDisabled.isChecked()){
            doNotBlockTheFirstReel.setVisibility(View.VISIBLE);
        }
        isPornDisabled.setOnCheckedChangeListener((button,isChecked)->{
            sharedPreferences.edit().putBoolean(DigiConstants.PREF_IS_PORN_BLOCKED,isChecked).apply();
        });
        
        isShortsDisabled.setOnCheckedChangeListener((button,isChecked)->{
            sharedPreferences.edit().putBoolean(DigiConstants.PREF_IS_SHORTS_BLOCKED,isChecked).apply();
            if(isChecked){
                doNotBlockTheFirstReel.setVisibility(View.VISIBLE);
            } else {
                doNotBlockTheFirstReel.setVisibility(View.GONE);
            }
        });
        doNotBlockTheFirstReel.setOnCheckedChangeListener((button,isChecked)->{
            sharedPreferences.edit().putBoolean(DigiConstants.PREF_IS_PREVENT_BLOCKING_FIRST_REEL,isChecked).apply();
        });
        isEngmntDisabled.setOnCheckedChangeListener((button,isChecked)->{
            sharedPreferences.edit().putBoolean(DigiConstants.PREF_IS_ENGMMT_BLOCKED,isChecked).apply();
        });
    }

   
}