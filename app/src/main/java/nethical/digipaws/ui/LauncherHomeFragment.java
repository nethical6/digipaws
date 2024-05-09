package nethical.digipaws.ui;

import android.app.WallpaperManager;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.media.AudioManager;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import nethical.digipaws.R;
import nethical.digipaws.ui.quest.LocationQuestFragment;
import nethical.digipaws.utils.ListApps;
import nethical.digipaws.utils.SurvivalModeConfig;
import nethical.digipaws.view.EqualizerView;

public class LauncherHomeFragment extends Fragment {

    private LinearLayout homeScreen;
    private LinearLayout infoLayout;
    private TextView mmtimeTextView;
    private TextView hhtimeTextView;
    private TextView dateTextView;
    private TextView info;
    private EqualizerView equalizerView;
    private Calendar calendar = Calendar.getInstance();
    private GestureDetector gestureDetector;

    private boolean isSurvivalEnabled = false;
    private String survivalModeEndTime = "00:00";
    private String crntTime = "00:00";

    public LauncherHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        setWallpaper();
        checkSurvival();
        checkAudio();

        new Thread(
                        () -> {
                            // run code on background thread
                            ListApps.updateCache(requireContext());

                            // here activity is the reference of activity

                        })
                .start();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void checkSurvival() {
        if (SurvivalModeConfig.isEnabled(requireContext())) {
            // survivalLayout.setVisibility(View.VISIBLE);
            survivalModeEndTime = SurvivalModeConfig.getUnlockTime(requireContext());

            if (crntTime == survivalModeEndTime) {
                //	survivalLayout.setVisibility(View.GONE);
                isSurvivalEnabled = false;
                // info.setText("Smile");

                generateStats();
                return;
            }
            info.setText(
                    "Your device has been locked until the clock reaches "
                            + survivalModeEndTime
                            + " hours. Attempt a quest to unlock device now.");
            isSurvivalEnabled = true;
        } else {
            //	survivalLayout.setVisibility(View.GONE);
            generateStats();
            isSurvivalEnabled = false;
        }
    }

    public void checkAudio() {
        AudioManager manager =
                (AudioManager) requireContext().getSystemService(requireContext().AUDIO_SERVICE);
        if (!manager.isMusicActive()) {
            equalizerView.setVisibility(View.GONE);
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_launcher_home, container, false);

        homeScreen = rootView.findViewById(R.id.homeScreen);
        infoLayout = rootView.findViewById(R.id.info_layout);
        mmtimeTextView = rootView.findViewById(R.id.mmTime);
        hhtimeTextView = rootView.findViewById(R.id.hhTime);
        dateTextView = rootView.findViewById(R.id.Date);
        info = rootView.findViewById(R.id.info);
        equalizerView = rootView.findViewById(R.id.equalizerView);

        return rootView;
    }

    @Override
    public void onViewCreated(View arg0, Bundle arg1) {
        super.onViewCreated(arg0, arg1);
        infoLayout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Fragment SettingsFragment = new LocationQuestFragment();

                        requireActivity()
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .setCustomAnimations(R.anim.fade_enter, R.anim.fade_exit)
                                .replace(R.id.fragment_container, SettingsFragment)
                                .addToBackStack(null)
                                .commit();
                    }
                });

        mmtimeTextView.setTypeface(
                Typeface.createFromAsset(requireContext().getAssets(), "regulartime.ttf"),
                Typeface.BOLD);
        hhtimeTextView.setTypeface(
                Typeface.createFromAsset(requireContext().getAssets(), "regulartime.ttf"),
                Typeface.BOLD);

        checkSurvival();
        checkAudio();
        generateStats();

        gestureDetector =
                new GestureDetector(
                        requireContext(),
                        new GestureDetector.SimpleOnGestureListener() {
                            private static final int SWIPE_THRESHOLD = 10;

                            @Override
                            public boolean onDown(MotionEvent e) {

                                return true; // Required for onFling to be called
                            }

                            @Override
                            public boolean onFling(
                                    MotionEvent e1,
                                    MotionEvent e2,
                                    float velocityX,
                                    float velocityY) {
                                float diffY = e2.getY() - e1.getY();

                                if (isSurvivalEnabled) {
                                    showToast(
                                            "Your device will be unlocked after "
                                                    + survivalModeEndTime
                                                    + " hours.");
                                    return true;
                                }

                                if (Math.abs(diffY) > SWIPE_THRESHOLD) {
                                    if (diffY < 0) {
                                        // User swiped upwards

                                        Fragment launcherAppsFragment = new LauncherAppsFragment();
                                        requireActivity()
                                                .getSupportFragmentManager()
                                                .beginTransaction()
                                                .setCustomAnimations(
                                                        R.anim.fade_enter,
                                                        R.anim.fade_exit) // Use 0 for no exit
                                                // animation
                                                .replace(
                                                        R.id.fragment_container,
                                                        launcherAppsFragment)
                                                .addToBackStack(null)
                                                .commit();
                                        return true;
                                    }
                                }
                                return false;
                            }
                        });

        // Set touch listener on the home screen layout to detect swipe gestures
        homeScreen.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        return gestureDetector.onTouchEvent(motionEvent);
                    }
                });

        updateTimeAndDate(); // Update time and date initially
    }

    private void setWallpaper() {
        if (getActivity() != null) {
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(getActivity());
            try {
                Drawable wallpaperDrawable = wallpaperManager.getDrawable();

                // Set the wallpaper as the background of the activity's Window
                //	getActivity().getWindow().setBackgroundDrawable(wallpaperDrawable);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateTimeAndDate() {
        int hour = calendar.get(Calendar.HOUR_OF_DAY); // 24-hour format
        int minute = calendar.get(Calendar.MINUTE);

        // Format the hour and minute with leading zeros if needed
        String formattedHour = String.format("%02d", hour);
        String formattedMinute = String.format("%02d", minute);

        crntTime = formattedHour + ":" + formattedMinute;
        hhtimeTextView.setText(formattedHour);
        mmtimeTextView.setText(formattedMinute);

        dateTextView.setText(DateFormat.format("EEEE, MMMM dd", calendar.getTime()));
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void generateStats() {
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(requireContext());

        int streak = preferences.getInt("streak", 0);
        int viewBlocked = preferences.getInt("viewblock_count", 0);
        int keywordBlocked = preferences.getInt("keywordblock_count", 0);

        String formattedValue =
                ("STREAK: "
                        + String.valueOf(streak)
                        + "\n"
                        + "Times Content Blocked: "
                        + String.valueOf(viewBlocked)
                        + "\n"
                        + "Times Keyword Blocked: "
                        + String.valueOf(viewBlocked)
                        + "\n");

        info.setText(formattedValue);
    }
}
