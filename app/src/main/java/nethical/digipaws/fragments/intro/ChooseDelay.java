package nethical.digipaws.fragments.intro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.provider.Settings;
import android.graphics.Color;
import android.util.Log;
import android.widget.LinearLayout;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
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

public class ChooseDelay extends Fragment {

    private NumberPicker delay;
    
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
        delay.setMaxValue(180);
        delay.setMinValue(2);
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(DigiConstants.PREF_APP_CONFIG,Context.MODE_PRIVATE);

        
        delay.setOnValueChangedListener(new NumberPicker.OnValueChangeListener(){
            @Override
            public void onValueChange(NumberPicker np, int oldnum, int num) {
                    sharedPreferences.edit().putInt(DigiConstants.PREF_DELAY,num*60*1000).apply();
            }
        });

    }
}
