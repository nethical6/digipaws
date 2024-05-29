package nethical.digipaws.fragments.intro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.provider.Settings;
import android.graphics.Color;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
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

public class ChooseMode extends Fragment {

    Spinner chooseModeSn;
    
    @Override
    public View onCreateView(
        LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.choose_preferences_mode, container, false);
        chooseModeSn = view.findViewById(R.id.choose_mode);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(DigiConstants.PREF_APP_CONFIG,Context.MODE_PRIVATE);

        chooseModeSn.setSelection(sharedPreferences.getInt(DigiConstants.PREF_MODE,DigiConstants.DIFFICULTY_LEVEL_NORMAL));
        
        chooseModeSn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            sharedPreferences.edit().putInt(DigiConstants.PREF_MODE,position).apply();
        }
        
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // Handle if no item is selected
        }
        });
        
    }
    

    
}
