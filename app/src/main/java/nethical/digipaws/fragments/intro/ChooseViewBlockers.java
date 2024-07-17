package nethical.digipaws.fragments.intro;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.heinrichreimersoftware.materialintro.app.SlideFragment;

import nethical.digipaws.R;
import nethical.digipaws.utils.DigiConstants;

public class ChooseViewBlockers extends SlideFragment {

    CheckBox isPornDisabled;
    CheckBox isEngmntDisabled;
    CheckBox isShortsDisabled;
    
    private SharedPreferences sharedPreferences;
    
    public ChooseViewBlockers(SharedPreferences sp){
        sharedPreferences = sp;
    }
    
    @Override
    public View onCreateView(
        LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.choose_preferences_view_blockers, container, false);
        isPornDisabled = view.findViewById(R.id.porn_cb);
        isEngmntDisabled = view.findViewById(R.id.engmnt_cb);
        isShortsDisabled = view.findViewById(R.id.shorts_cb);
        
         return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        isPornDisabled.setChecked(sharedPreferences.getBoolean(DigiConstants.PREF_IS_PORN_BLOCKED,false));
        isShortsDisabled.setChecked(sharedPreferences.getBoolean(DigiConstants.PREF_IS_SHORTS_BLOCKED,false));
        isEngmntDisabled.setChecked(sharedPreferences.getBoolean(DigiConstants.PREF_IS_ENGMMT_BLOCKED,false));
        
        
        
        isPornDisabled.setOnCheckedChangeListener((button,isChecked)->{
            sharedPreferences.edit().putBoolean(DigiConstants.PREF_IS_PORN_BLOCKED,isChecked).apply();
        });
        
        isShortsDisabled.setOnCheckedChangeListener((button,isChecked)->{
            sharedPreferences.edit().putBoolean(DigiConstants.PREF_IS_SHORTS_BLOCKED,isChecked).apply();
        });
        
        isEngmntDisabled.setOnCheckedChangeListener((button,isChecked)->{
            sharedPreferences.edit().putBoolean(DigiConstants.PREF_IS_ENGMMT_BLOCKED,isChecked).apply();
        });
    }

   
}