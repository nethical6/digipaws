package nethical.digipaws.fragments.intro;

import android.content.SharedPreferences;
import android.widget.AdapterView;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;
import androidx.fragment.app.Fragment;
import nethical.digipaws.R;
import nethical.digipaws.utils.DigiConstants;

public class ChooseMode extends Fragment {

    Spinner chooseModeSn;
    
  private SharedPreferences sharedPreferences;
    
    public ChooseMode(SharedPreferences sp){
        sharedPreferences = sp;
    }
    
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
