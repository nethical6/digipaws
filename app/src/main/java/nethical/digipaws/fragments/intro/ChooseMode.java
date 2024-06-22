package nethical.digipaws.fragments.intro;

import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import android.content.Intent;
import android.net.Uri;
import android.widget.AdapterView;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;
import androidx.fragment.app.Fragment;
import com.github.appintro.SlidePolicy;
import nethical.digipaws.R;
import nethical.digipaws.utils.DigiConstants;

public class ChooseMode extends Fragment implements SlidePolicy {

    Spinner chooseModeSn;
    
    private boolean canOverlay = true;
    
    private SharedPreferences sharedPreferences;

    public ChooseMode(SharedPreferences sp) {
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

        chooseModeSn.setSelection(
                sharedPreferences.getInt(
                        DigiConstants.PREF_MODE, DigiConstants.DIFFICULTY_LEVEL_NORMAL));

        chooseModeSn.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        sharedPreferences.edit().putInt(DigiConstants.PREF_MODE, position).apply();
                    if(position==DigiConstants.DIFFICULTY_LEVEL_NORMAL||position==DigiConstants.DIFFICULTY_LEVEL_EASY){
                        canOverlay = false;
                    }else{
                        canOverlay= true;
                    }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Handle if no item is selected
                    }
                });
    }

    @Override
    public boolean isPolicyRespected() {
        return canOverlay;
    }

    private void showOverlayPerm(){
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(requireContext())) {
                canOverlay = false;
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.missing_permission)
                    .setMessage(R.string.notification_overlay_permission)
                    .setNeutralButton("Provide",(dialog,which)->{
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + requireContext().getPackageName()));
                        startActivityForResult(intent, 69);
                        dialog.dismiss();
                    });
                    builder.create().show();
            } else {
                canOverlay = true;
            }
        }
    }
    
    @Override
    public void onUserIllegallyRequestedNextPage() {
        showOverlayPerm();
    }
    
   @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 69) {
            // Handle the result of the permission request
            if (Settings.canDrawOverlays(requireContext())) {
                // Permission is granted
               canOverlay = true;
            } else {
                // Permission is not granted
                canOverlay = false;
            }
        }
    }
}
