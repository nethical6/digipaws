package nethical.digipaws.fragments.intro;

import android.content.SharedPreferences;
import android.widget.CheckBox;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import androidx.fragment.app.Fragment;
import nethical.digipaws.R;
import nethical.digipaws.utils.DigiConstants;

public class ConfigureAntiUninstall extends Fragment  {

    
    
    private SharedPreferences sharedPreferences;
    
    public ConfigureAntiUninstall(SharedPreferences sp){
        sharedPreferences = sp;
    }
    
    @Override
    public View onCreateView(
        LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.choose_preferences_uninstall, container, false);
         return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        
    }

   
}