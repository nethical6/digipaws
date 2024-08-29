package nethical.digipaws.fragments.intro;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputEditText;
import com.heinrichreimersoftware.materialintro.app.SlideFragment;

import java.util.Objects;

import nethical.digipaws.R;
import nethical.digipaws.utils.DigiConstants;

public class ConfigureWarning extends SlideFragment {

    private SharedPreferences customMsgSharedPreferences;
    private SharedPreferences userConfigPrefs;
    private TextInputEditText title;
    private TextInputEditText desc;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.configure_message, container, false);
        title = view.findViewById(R.id.title);
        desc = view.findViewById(R.id.desc);
        customMsgSharedPreferences = requireContext().getSharedPreferences(DigiConstants.PREF_WARN_FILE,Context.MODE_PRIVATE);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title.setText(customMsgSharedPreferences.getString(DigiConstants.PREF_WARN_TITLE, requireContext().getString(R.string.breathe)));
        desc.setText(customMsgSharedPreferences.getString(DigiConstants.PREF_WARN_MESSAGE, requireContext().getString(R.string.warning_title)));
    }

    @Override
    public boolean canGoForward() {

        String titlet = Objects.requireNonNull(title.getText()).toString();
        if(titlet.isEmpty()){
            titlet = requireContext().getString(R.string.breathe);
        }

        String msgm = Objects.requireNonNull(desc.getText()).toString();
        if(msgm.isEmpty()){
            msgm = requireContext().getString(R.string.warning_title);
        }

        customMsgSharedPreferences.edit().putString(DigiConstants.PREF_WARN_TITLE,titlet).apply();
        customMsgSharedPreferences.edit().putString(DigiConstants.PREF_WARN_MESSAGE,msgm).apply();
        return true;
    }

}
