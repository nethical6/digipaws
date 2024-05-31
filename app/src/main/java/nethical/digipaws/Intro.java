package nethical.digipaws;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;

import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import com.github.appintro.AppIntro;
import com.github.appintro.AppIntroFragment;

import nethical.digipaws.fragments.intro.ChooseBlockedApps;
import nethical.digipaws.fragments.intro.ChooseDelay;
import nethical.digipaws.fragments.intro.ChooseMode;
import nethical.digipaws.fragments.intro.ChooseViewBlockers;
import nethical.digipaws.utils.DigiConstants;

public class Intro extends AppIntro{
    
	private SharedPreferences sharedPreferences;
    
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        sharedPreferences = getSharedPreferences(DigiConstants.PREF_APP_CONFIG,Context.MODE_PRIVATE);
        
		addSlide(AppIntroFragment.createInstance(getString(R.string.welcome),
		HtmlCompat.fromHtml(getString(R.string.app_desc),Html.FROM_HTML_MODE_COMPACT),
		R.drawable.paws,
		R.color.md_theme_dark_background
		));
		
		addSlide(AppIntroFragment.createInstance(
		getString(R.string.easy_mode),
		HtmlCompat.fromHtml(getString(R.string.easy_mode_desc),Html.FROM_HTML_MODE_LEGACY),
		R.drawable.intro_easy,
		R.color.md_theme_dark_background
		));
		
		addSlide(AppIntroFragment.createInstance(
		getString(R.string.adventure_mode),
		HtmlCompat.fromHtml(getString(R.string.adventure_mode_desc),Html.FROM_HTML_MODE_LEGACY),
		R.drawable.intro_explore,
		R.color.md_theme_dark_background
		));
		
		addSlide(AppIntroFragment.createInstance(
		getString(R.string.hard_mode),
		HtmlCompat.fromHtml(getString(R.string.hard_mode_desc),Html.FROM_HTML_MODE_LEGACY),
		R.drawable.intro_hard,
		R.color.md_theme_dark_background
		));
        
        
        
        addSlide(new ChooseMode(sharedPreferences));
        addSlide(new ChooseViewBlockers(sharedPreferences));
        addSlide(new ChooseDelay(sharedPreferences));
        addSlide(new ChooseBlockedApps(sharedPreferences));
        
        addSlide(AppIntroFragment.createInstance(
		"Thanks For choosing us",
		"By continuing you agree that we are not responsible for any damages done by this app to you or your device(s)",
        R.drawable.paws,
		R.color.md_theme_dark_background
		));
        
		showStatusBar(true);
	    setWizardMode(true);
		
		//Enable immersive mode (no status and nav bar)
		setImmersiveMode();
		
		//Enable/disable page indicators
		setIndicatorEnabled(true);
		setSkipButtonEnabled(false);
		//Dhow/hide ALL buttons
	//	setButtonsEnabled(false);
		
	}
	
    
    
	@Override
	protected void onSkipPressed(Fragment currentFragment) {
		super.onSkipPressed(currentFragment);
	}
    @Override
    protected void onSlideChanged(Fragment oldFragment, Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);

        // You can perform actions when the slide changes, if needed
        if (newFragment instanceof ChooseBlockedApps) {
            ((ChooseBlockedApps) newFragment).loadAppsAndDisplay();
        }
        
        
        
    }
    
	
	@Override
	protected void onDonePressed(Fragment currentFragment) {
		super.onDonePressed(currentFragment);
        sharedPreferences.edit().putBoolean(DigiConstants.PREF_IS_INTRO_SHOWN,true).apply();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
	}
}