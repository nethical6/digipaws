package nethical.digipaws.fragments.intro;

import android.content.Intent;
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
        
    }
    

    
}
