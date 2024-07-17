package nethical.digipaws.fragments.intro;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputEditText;
import com.heinrichreimersoftware.materialintro.app.SlideFragment;

import nethical.digipaws.R;
import nethical.digipaws.utils.DigiConstants;

public class ConfigureWarning extends SlideFragment {

    private SharedPreferences sharedPreferences;
    private TextInputEditText title;
    private TextInputEditText desc;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.configure_message, container, false);
        title = view.findViewById(R.id.title);
        desc = view.findViewById(R.id.desc);
        sharedPreferences = requireContext().getSharedPreferences(DigiConstants.PREF_WARN_FILE,Context.MODE_PRIVATE);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title.setText(sharedPreferences.getString(DigiConstants.PREF_WARN_TITLE,getContext().getString(R.string.breathe)));
        desc.setText(sharedPreferences.getString(DigiConstants.PREF_WARN_MESSAGE,getContext().getString(R.string.warning_title)));
    }

    @Override
    public boolean canGoForward() {

        String titlet = title.getText().toString();
        if(titlet.equals("")){
            titlet = getContext().getString(R.string.breathe);
        }

        String msgm = desc.getText().toString();
        if(msgm.equals("")){
            msgm = getContext().getString(R.string.warning_title);
        }

        sharedPreferences.edit().putString(DigiConstants.PREF_WARN_TITLE,titlet).apply();
        sharedPreferences.edit().putString(DigiConstants.PREF_WARN_MESSAGE,msgm).apply();
        return true;
    }
}
