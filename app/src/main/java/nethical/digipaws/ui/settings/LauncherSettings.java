package nethical.digipaws.ui.settings;

import android.os.Bundle;

import nethical.digipaws.R;
import nethical.digipaws.ui.SettingsFragment;

public class LauncherSettings extends SettingsFragment {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference_launcher, rootKey);
    }
}
