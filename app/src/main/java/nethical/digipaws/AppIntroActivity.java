package nethical.digipaws;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

import nethical.digipaws.fragments.intro.ChooseBlockedApps;
import nethical.digipaws.fragments.intro.ChooseDelay;
import nethical.digipaws.fragments.intro.ChooseMode;
import nethical.digipaws.fragments.intro.ChooseViewBlockers;
import nethical.digipaws.fragments.intro.ConfigureAntiUninstall;
import nethical.digipaws.fragments.intro.ConfigureWarning;
import nethical.digipaws.fragments.intro.TermsAndConditions;
import nethical.digipaws.utils.DigiConstants;

public class AppIntroActivity extends IntroActivity {

    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setFullscreen(true);
        setButtonBackVisible(false);
        EdgeToEdge.enable(this);

        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        //  getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        sharedPreferences = getSharedPreferences(DigiConstants.PREF_APP_CONFIG, Context.MODE_PRIVATE);

        addSlide(new SimpleSlide.Builder()
                .description(R.string.app_desc)
                .image(R.drawable.paws)
                .background(R.color.md_theme_dark_background)
                .backgroundDark(R.color.md_theme_dark_background)
                .scrollable(false)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title(R.string.easy_mode)
                .description(R.string.easy_mode_desc)
                .background(R.color.easy_color)
                .backgroundDark(R.color.easy_color)
                .scrollable(false)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title(R.string.adventure_mode)
                .description(R.string.adventure_mode_desc)
                .background(R.color.adventure_color)
                .backgroundDark(R.color.adventure_color)
                .scrollable(false)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title(R.string.hard_mode)
                .description(R.string.hard_mode_desc)
                .background(R.color.difficult_color)
                .backgroundDark(R.color.difficult_color)
                .scrollable(false)
                .build());


        addSlide(new FragmentSlide.Builder()
                .fragment(new ChooseMode(sharedPreferences))
                .background(R.color.mode_color)
                .backgroundDark(R.color.mode_color)
                .build());


        addSlide(new FragmentSlide.Builder()
                .fragment(new ConfigureWarning())
                .background(R.color.customize_color)
                .backgroundDark(R.color.customize_color)
                .build());

        addSlide(new FragmentSlide.Builder()
                .fragment(new ChooseDelay(sharedPreferences))
                .background(R.color.time_color)
                .backgroundDark(R.color.time_color)
                .build());

        addSlide(new FragmentSlide.Builder()
                .fragment(new ChooseViewBlockers(sharedPreferences))
                .background(R.color.block_color)
                .backgroundDark(R.color.block_color)
                .build());

        ChooseBlockedApps chooseBlockedApps = new ChooseBlockedApps(sharedPreferences);
        addSlide(new FragmentSlide.Builder()
                .fragment(chooseBlockedApps)
                .background(R.color.app_color)
                .backgroundDark(R.color.app_color)
                .build());

        addSlide(new FragmentSlide.Builder()
                .fragment(new ConfigureAntiUninstall(sharedPreferences))
                .background(R.color.committed_color)
                .backgroundDark(R.color.md_theme_dark_background)
                .build());

        addSlide(new FragmentSlide.Builder()
                .fragment(new TermsAndConditions())
                .background(R.color.md_theme_light_onBackground)
                .backgroundDark(R.color.md_theme_dark_background)
                .build());


        addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                int mode = sharedPreferences.getInt(DigiConstants.PREF_MODE, DigiConstants.DIFFICULTY_LEVEL_EXTREME);
                if (position == 5 && mode == DigiConstants.DIFFICULTY_LEVEL_EXTREME) {
                    goToSlide(7);
                }
                if (position == 6 && (mode == DigiConstants.DIFFICULTY_LEVEL_EXTREME || mode == DigiConstants.DIFFICULTY_LEVEL_NORMAL)) {
                    goToSlide(7);
                }
                if (position == 8) {
                    chooseBlockedApps.loadAppsAndDisplay();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


    }

}