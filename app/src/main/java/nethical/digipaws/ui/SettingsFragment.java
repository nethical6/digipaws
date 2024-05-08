package nethical.digipaws.ui;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreferenceCompat;
import androidx.preference.PreferenceManager;
import nethical.digipaws.R;
import nethical.digipaws.utils.KeyboardUtils;

public class SettingsFragment extends PreferenceFragmentCompat
    implements PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

  @Override
  public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    setPreferencesFromResource(R.xml.preferences, rootKey);
  }

  @Override
  public void onStart() {
    super.onStart();
  }

  @Override
  public boolean onPreferenceStartFragment(PreferenceFragmentCompat caller, Preference pref) {
    final Bundle args = pref.getExtras();
    final Fragment fragment =
        getParentFragmentManager()
            .getFragmentFactory()
            .instantiate(requireContext().getClass().getClassLoader(), pref.getFragment());
    fragment.setArguments(args);
    requireActivity()
        .getSupportFragmentManager()
        .beginTransaction()
        .setCustomAnimations(R.anim.fade_enter, R.anim.fade_exit) // Use 0 for no exit animation
        .replace(R.id.fragment_container, fragment)
        .addToBackStack(null)
        .commit();

    return true;
  }
}
