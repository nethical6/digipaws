package nethical.digipaws.fragments.intro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.heinrichreimersoftware.materialintro.app.SlideFragment;

import nethical.digipaws.R;
import nethical.digipaws.utils.DigiConstants;

public class ChooseMode extends SlideFragment {

    private boolean canOverlay = true;

    private final SharedPreferences sharedPreferences;

    private RadioGroup radioGroupModes;


    private ActivityResultLauncher<Intent> activityResultLauncher;

    public ChooseMode(SharedPreferences sp) {
        sharedPreferences = sp;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.choose_preferences_mode, container, false);
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> canOverlay = Settings.canDrawOverlays(requireContext()));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        radioGroupModes = view.findViewById(R.id.radio_group_modes);
        RadioButton radioButtonHardMode = view.findViewById(R.id.radio_button_hard_mode);
        RadioButton radioButtonEasyMode = view.findViewById(R.id.radio_button_easy_mode);
        RadioButton radioButtonAdventureMode = view.findViewById(R.id.radio_button_adventure_mode);

        // Set initial selection based on SharedPreferences
        int selectedMode = sharedPreferences.getInt(
                DigiConstants.PREF_MODE, DigiConstants.DIFFICULTY_LEVEL_EXTREME
        );
        switch (selectedMode) {
            case DigiConstants.DIFFICULTY_LEVEL_NORMAL:
                radioButtonAdventureMode.setChecked(true);
                break;
            case DigiConstants.DIFFICULTY_LEVEL_EASY:
                radioButtonEasyMode.setChecked(true);
                break;
            case DigiConstants.DIFFICULTY_LEVEL_EXTREME:
            default:
                radioButtonHardMode.setChecked(true);
                break;
        }
        radioGroupModes.setOnCheckedChangeListener((group, checkedId) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            int checkedBtnId = radioGroupModes.getCheckedRadioButtonId();
            if (checkedBtnId == R.id.radio_button_hard_mode) {
                editor.putInt(DigiConstants.PREF_MODE, DigiConstants.DIFFICULTY_LEVEL_EXTREME);
                canOverlay = true;
            } else if (checkedBtnId == R.id.radio_button_easy_mode) {
                editor.putInt(DigiConstants.PREF_MODE, DigiConstants.DIFFICULTY_LEVEL_EASY);
                showOverlayPerm();
                canOverlay = false;
            } else if (checkedBtnId == R.id.radio_button_adventure_mode) {
                editor.putInt(DigiConstants.PREF_MODE, DigiConstants.DIFFICULTY_LEVEL_NORMAL);
                showOverlayPerm();
                canOverlay = false;
            }
            editor.apply();
        });
        addOnNavigationBlockedListener((position, direction) -> showOverlayPerm());

    }


    @Override
    public boolean canGoForward() {
        return Settings.canDrawOverlays(requireContext());
    }

    private void showOverlayPerm() {
        if (!Settings.canDrawOverlays(requireContext())) {
            canOverlay = false;
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.missing_permission)
                    .setMessage(R.string.notification_overlay_permission)
                    .setCancelable(false)
                    .setNeutralButton("Provide", (dialog, which) -> {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:" + requireContext().getPackageName()));
                        activityResultLauncher.launch(intent);
                        dialog.dismiss();
                    });
            builder.create().show();
        } else {
            canOverlay = true;
        }
    }

}
