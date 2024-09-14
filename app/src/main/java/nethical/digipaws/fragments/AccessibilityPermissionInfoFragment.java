package nethical.digipaws.fragments;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.provider.Settings;
import android.widget.Button;
import androidx.core.content.FileProvider;
import android.net.Uri;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.internal.EdgeToEdgeUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import androidx.fragment.app.Fragment;


import nethical.digipaws.BuildConfig;
import nethical.digipaws.R;
import nethical.digipaws.services.BlockerService;
import nethical.digipaws.utils.DigiConstants;
import nethical.digipaws.utils.DigiUtils;


public class AccessibilityPermissionInfoFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.accessibility_perm_info_fragment, container, false);
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button accept = view.findViewById(R.id.accept);
        Button decline = view.findViewById(R.id.decline);

        accept.setOnClickListener(v -> {
            if (BuildConfig.LITE_VERSION && android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                showRestrictedSettingsInfoDialog();
                return;
            }
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
        });

        decline.setOnClickListener(v ->
                getParentFragmentManager().popBackStack());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (DigiUtils.isAccessibilityServiceEnabled(requireContext(), BlockerService.class)) {{
            getParentFragmentManager().popBackStack();
        }}
    }

    private void showRestrictedSettingsInfoDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext()).setTitle("Additional Step Required").setMessage("All third party apps that use Accessibility Services on Android 13+ requires Restricted Settings to be enabled prior to this process. If you have enabled it, you can proceed further.")
                .setNeutralButton("Watch Tutorial", (dialog, which) -> {
                    dialog.dismiss();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/0bcLjpfrmHw?si=L3vXHPj1MJHA3DGm"));
                    requireContext().startActivity(intent);
                })
                .setPositiveButton("Open Settings",(dialog,which) -> {
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivity(intent);
                });
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

    }
}