package nethical.digipaws;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.google.android.material.internal.EdgeToEdgeUtils;
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
        EdgeToEdgeUtils.applyEdgeToEdge(getWindow(), true);
        //  getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        sharedPreferences = getSharedPreferences(DigiConstants.PREF_APP_CONFIG, Context.MODE_PRIVATE);

        addSlide(new SimpleSlide.Builder()
                .description(R.string.app_desc)
                .image(R.drawable.paws)
                .background(R.color.md_theme_light_onBackground)
                .backgroundDark(R.color.md_theme_dark_background)
                .scrollable(false)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title( R.string.easy_mode)
                .description(R.string.easy_mode_desc)
                .background(R.color.md_theme_light_onBackground)
                .backgroundDark(R.color.md_theme_dark_background)
                .scrollable(false)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title(R.string.adventure_mode)
                .description(R.string.adventure_mode_desc)
                .background(R.color.md_theme_light_onBackground)
                .backgroundDark(R.color.md_theme_dark_background)
                .scrollable(false)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title(R.string.hard_mode)
                .description(R.string.hard_mode_desc)
                .background(R.color.md_theme_light_onBackground)
                .backgroundDark(R.color.md_theme_dark_background)
                .scrollable(false)
                .build());


        addSlide(new FragmentSlide.Builder()
                .fragment(new ChooseMode(sharedPreferences))
                .background(R.color.md_theme_light_onBackground)
                .backgroundDark(R.color.md_theme_dark_background)
                .build());

        addSlide(new FragmentSlide.Builder()
                .fragment(new ConfigureWarning())
                .background(R.color.md_theme_light_onBackground)
                .backgroundDark(R.color.md_theme_dark_background)
                .build());

        addSlide(new FragmentSlide.Builder()
                .fragment(new ChooseDelay(sharedPreferences))
                .background(R.color.md_theme_light_onBackground)
                .backgroundDark(R.color.md_theme_dark_background)
                .build());

        addSlide(new FragmentSlide.Builder()
                .fragment(new ChooseViewBlockers(sharedPreferences))
                .background(R.color.md_theme_light_onBackground)
                .backgroundDark(R.color.md_theme_dark_background)
                .build());

        addSlide(new FragmentSlide.Builder()
                .fragment(new ChooseBlockedApps(sharedPreferences))
                .background(R.color.md_theme_light_onBackground)
                .backgroundDark(R.color.md_theme_dark_background)
                .build());

        addSlide(new FragmentSlide.Builder()
                .fragment(new ConfigureAntiUninstall(sharedPreferences))
                .background(R.color.md_theme_light_onBackground)
                .backgroundDark(R.color.md_theme_dark_background)
                .build());

        addSlide(new FragmentSlide.Builder()
                .fragment(new TermsAndConditions())
                .background(R.color.md_theme_light_onBackground)
                .backgroundDark(R.color.md_theme_dark_background)
                .build());

        }


}