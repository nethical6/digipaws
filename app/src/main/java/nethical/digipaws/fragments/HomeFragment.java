package nethical.digipaws.fragments;

import android.annotation.SuppressLint;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import nethical.digipaws.R;
import nethical.digipaws.adapters.SelectQuestAdapter;
import nethical.digipaws.fragments.quests.FocusQuest;
import nethical.digipaws.receivers.AdminReceiver;
import nethical.digipaws.services.BlockerService;
import nethical.digipaws.utils.CoinManager;
import nethical.digipaws.utils.DigiConstants;
import nethical.digipaws.utils.DigiUtils;

public class HomeFragment extends Fragment {

    private TextView dayStreakTextView;
    private SharedPreferences sharedPreferences;
    private String daysStreak = "0";

    private TextView cointCount;
    private int mode = 0;
    private ActivityResultLauncher<Intent> activityResultLauncherOverlay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        dayStreakTextView = view.findViewById(R.id.streak);
        cointCount = view.findViewById(R.id.coin_count);
        activityResultLauncherOverlay = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    checkAndRequestBatteryOptimization(requireContext());
                }
        );
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.quest_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        String[] listItems = getResources().getStringArray(R.array.Quests);
        recyclerView.setAdapter(new SelectQuestAdapter(listItems, requireContext(), getParentFragmentManager()));

        SharedPreferences questInfo = requireContext().getSharedPreferences(DigiConstants.PREF_QUEST_INFO_FILE,Context.MODE_PRIVATE);
        switch (questInfo.getString(DigiConstants.PREF_QUEST_ID_KEY,DigiConstants.QUEST_ID_NULL)){
            case DigiConstants.QUEST_ID_MARATHON:
                try {
                    Fragment fragment = (Fragment) Class.forName("nethical.digipaws.fragments.quests.MarathonQuest").newInstance();
                    DigiUtils.replaceScreenWithoutAddingToBackStack(getParentFragmentManager(),fragment);
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                         java.lang.InstantiationException e) {
                    throw new RuntimeException(e);
                }
                break;

            case DigiConstants.QUEST_ID_FOCUS:
                DigiUtils.replaceScreenWithoutAddingToBackStack(getParentFragmentManager(), new FocusQuest());
                break;

        }

        sharedPreferences = requireContext().getSharedPreferences(DigiConstants.PREF_APP_CONFIG, Context.MODE_PRIVATE);
        mode = sharedPreferences.getInt(DigiConstants.PREF_MODE, DigiConstants.DIFFICULTY_LEVEL_EASY);
        if (mode == DigiConstants.DIFFICULTY_LEVEL_EASY || mode == DigiConstants.DIFFICULTY_LEVEL_NORMAL) {
            checkOverlay();;
        };
        if(mode == DigiConstants.DIFFICULTY_LEVEL_NORMAL){
            refreshCoinCount();
        }
        dayStreakTextView.setOnClickListener(v -> {
            DigiUtils.replaceScreen(getParentFragmentManager(), new ChallengeCompletedFragment(false, daysStreak));
        });

    }

    private void checkOverlay() {
        if (!Settings.canDrawOverlays(requireContext())) {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.missing_permission)
                    .setMessage(R.string.notification_overlay_permission)
                    .setNeutralButton("Provide", (dialog, which) -> {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:" + requireContext().getPackageName()));
                        activityResultLauncherOverlay.launch(intent);
                        dialog.dismiss();
                    });
            builder.setCancelable(false);
            builder.create().show();
        }
    }


    public void calculateDaysPassed() {

        SimpleDateFormat sdf = new SimpleDateFormat(DigiConstants.DATE_FORMAT, Locale.getDefault());
        Date givenDate = getDate(requireContext());
        Date currentDate = new Date();
        long differenceInMillis = currentDate.getTime() - givenDate.getTime();
        long days = TimeUnit.DAYS.convert(differenceInMillis, TimeUnit.MILLISECONDS);
        daysStreak = String.valueOf(days);
        dayStreakTextView.setText("Day " + days);

        DevicePolicyManager devicePolicyManager =
                (DevicePolicyManager)
                        requireContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName deviceAdminReceiver =
                new ComponentName(requireContext(), AdminReceiver.class);

        Date antiUninstallStartDay = getChallengeDate(requireContext());
        long adifferenceInMillis = currentDate.getTime() - antiUninstallStartDay.getTime();
        long antiUninstallDaysElapsed = TimeUnit.DAYS.convert(adifferenceInMillis, TimeUnit.MILLISECONDS);

        //if(true){
        if (antiUninstallDaysElapsed >= DigiConstants.CHALLENGE_TIME) {
            if (!devicePolicyManager.isAdminActive(deviceAdminReceiver)) {
                return;
            }

            DigiUtils.sendNotification(requireContext(), "Challenge Completed", "Anti-Uninstall has been disabled!!!!!!", R.drawable.swords);
            sharedPreferences.edit().putBoolean(DigiConstants.PREF_IS_ANTI_UNINSTALL, false).apply();

            devicePolicyManager.removeActiveAdmin(deviceAdminReceiver);
            DigiUtils.replaceScreen(getParentFragmentManager(), new ChallengeCompletedFragment(true, String.valueOf(days)));
        }
    }

    @SuppressLint("SetTextI18n")
    public void refreshCoinCount() {
        if (mode == DigiConstants.DIFFICULTY_LEVEL_NORMAL) {
            cointCount.setText("You hold " + String.valueOf(CoinManager.getCoinCount(requireContext())) + " Aura");
        }
    }

    public Date getDate(Context context) {

        String dateString = sharedPreferences.getString(DigiConstants.PREF_USAGE_STREAK_START, "2024-06-21");

        SimpleDateFormat sdf = new SimpleDateFormat(DigiConstants.DATE_FORMAT, Locale.getDefault());
        try {
            return sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Date getChallengeDate(Context context) {

        String dateString = sharedPreferences.getString(DigiConstants.PREF_ANTI_UNINSTALL_START, "0001-01-01");

        SimpleDateFormat sdf = new SimpleDateFormat(DigiConstants.DATE_FORMAT, Locale.getDefault());
        try {
            return sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        checkAccessibiltyPermission();
        calculateDaysPassed();
        refreshCoinCount();
    }

    private void checkAccessibiltyPermission() {
        if (!DigiUtils.isAccessibilityServiceEnabled(requireContext(), BlockerService.class)) {
            DigiUtils.replaceScreen(getParentFragmentManager(),new AccessibilityPermissionInfoFragment());
        } else {
            checkAndRequestBatteryOptimization(requireContext());
        }
    }

    private void checkAndRequestBatteryOptimization(Context context) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (!powerManager.isIgnoringBatteryOptimizations(context.getPackageName())) {
            Snackbar.make(dayStreakTextView, R.string.battery_optimisation_desc, Snackbar.LENGTH_SHORT)
                    .setAction(R.string.settings, v -> {
                        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context)
                                .setTitle(R.string.battery_optimisation_title)
                                .setMessage(R.string.battery_optimisation_desc)
                                .setNeutralButton("Provide", (dialog, which) -> {
                                    Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                                    intent.setData(Uri.parse("package:" + context.getPackageName()));
                                    context.startActivity(intent);
                                    dialog.dismiss();
                                });
                        builder.create().show();
                    }).show();

        }
    }


}
