package nethical.digipaws.fragments.intro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.provider.Settings;
import android.graphics.Color;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;
import com.github.appintro.SlidePolicy;
import com.google.android.material.color.DynamicColors;
import com.google.android.material.snackbar.Snackbar;
import java.util.List;
import nethical.digipaws.R;
import nethical.digipaws.fragments.dialogs.LoadingDialog;
import nethical.digipaws.services.BlockerService;
import nethical.digipaws.utils.DigiConstants;
import nethical.digipaws.utils.DigiUtils;
import nethical.digipaws.utils.LoadAppList;
import nethical.digipaws.fragments.dialogs.SelectQuestDialog;
import nethical.digipaws.utils.OverlayManager;
import nethical.digipaws.views.HollowCircleView;
import org.osmdroid.views.MapView;

public class ChooseViewBlockers extends Fragment  {

    CheckBox isPornDisabled;
    CheckBox isEngmntDisabled;
    CheckBox isShortsDisabled;
    
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
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(DigiConstants.PREF_APP_CONFIG,Context.MODE_PRIVATE);
        
        isPornDisabled.setChecked(sharedPreferences.getBoolean(DigiConstants.PREF_IS_PORN_BLOCKED,false));
        isShortsDisabled.setChecked(sharedPreferences.getBoolean(DigiConstants.PREF_IS_SHORTS_BLOCKED,true));
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