package nethical.digipaws.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import nethical.digipaws.R;

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
                        .instantiate(
                                requireContext().getClass().getClassLoader(), pref.getFragment());
        fragment.setArguments(args);
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();

        return true;
    }
}