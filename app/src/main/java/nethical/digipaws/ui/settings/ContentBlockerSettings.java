package nethical.digipaws.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import nethical.digipaws.R;
import nethical.digipaws.ui.SettingsFragment;
import nethical.digipaws.utils.ListApps;
import nethical.digipaws.utils.ViewBlocklistLoaderTask;
import nethical.digipaws.utils.data.BlockableView;
import nethical.digipaws.view.CustomLoadingDialog;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ContentBlockerSettings extends SettingsFragment {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference_content_blockers, rootKey);
        CustomLoadingDialog dialog = new CustomLoadingDialog(requireContext());

        ViewBlocklistLoaderTask loader = new ViewBlocklistLoaderTask(requireContext());
        loader.startSearch(
                ListApps.getPackageNames(requireContext()),
                () -> {
                    // This lambda expression will be invoked when all requests are completed
                    // Add any logic you want to execute after all requests finish
                    loadScreen();
                    dialog.dismissDialog();
                });
    }

    public void loadScreen() {

        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(requireContext());

        String[] fullAppPacks = ListApps.getPackageNames(requireContext());
        for (String packageName : fullAppPacks) {

            Map<String, BlockableView> packageMap = getPackageMap(preferences, packageName);
            if (packageMap != null) {
                PreferenceCategory category = new PreferenceCategory(requireContext());
                category.setTitle(packageName); // Set the title for the category
                getPreferenceScreen()
                        .addPreference(category); // Add the category to the preference screen

                // DefaultAction.showToast(service,"class");
                for (Map.Entry<String, BlockableView> entry : packageMap.entrySet()) {
                    String viewId = entry.getValue().getViewId();
                    Boolean isEnabled = entry.getValue().isEnabled();

                    // Create a switch preference
                    SwitchPreferenceCompat switchPreference =
                            new SwitchPreferenceCompat(requireContext());
                    switchPreference.setTitle(entry.getValue().getTitle());
                    switchPreference.setSummary(
                            entry.getValue().getDescription()); // Set the summary for the switch
                    switchPreference.setKey(
                            packageName
                                    + ":id/"
                                    + entry.getValue()
                                            .getViewId()); // Set a unique key for the switch

                    // Add the switch preference to the category
                    category.addPreference(switchPreference);

                    // Set a listener for the switch preference
                    switchPreference.setOnPreferenceChangeListener(
                            new Preference.OnPreferenceChangeListener() {
                                @Override
                                public boolean onPreferenceChange(
                                        Preference preference, Object newValue) {
                                    boolean isChecked = (boolean) newValue;
                                    // Handle the switch state change here
                                    if (isChecked) {
                                        entry.getValue().setEnabled(true);
                                        entry.getValue().resetCounter();
                                        entry.getValue().resetDetails();
                                        saveMapToPreferences(
                                                requireContext(), packageName, packageMap);
                                        // Switch is checked (enabled)
                                        // Perform actions when switch is turned on
                                    } else {
                                        entry.getValue().setEnabled(false);
                                        entry.getValue().resetCounter();
                                        entry.getValue().resetDetails();
                                        saveMapToPreferences(
                                                requireContext(), packageName, packageMap);

                                        // Switch is unchecked (disabled)
                                        // Perform actions when switch is turned off
                                    }
                                    return true; // Return true to update the preference state
                                }
                            });
                }
            }
        }
    }

    private static Map<String, BlockableView> getPackageMap(
            SharedPreferences preferences, String packageName) {
        String json = preferences.getString(packageName, null);
        if (json != null) {
            // Convert JSON string back to Map<String, Boolean>
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, BlockableView>>() {}.getType();
            return gson.fromJson(json, type);
        }
        return null;
    }

    public static void saveMapToPreferences(
            Context context, String packageName, Map<String, BlockableView> map) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        // Convert the map to JSON string using Gson
        Gson gson = new Gson();
        String json = gson.toJson(map);

        // Save the JSON string to shared preferences under the package name key
        editor.putString(packageName, json);
        editor.apply();
    }

    public static Map<String, BlockableView> entryToMap(Entry<String, BlockableView> entry) {
        Map<String, BlockableView> map = new HashMap<>();
        map.put(entry.getKey(), entry.getValue());
        return map;
    }

    public static boolean isPreferencesFileEmpty(Context context, String fileName) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        Map<String, ?> allPreferences = sharedPreferences.getAll();
        return allPreferences.isEmpty();
    }
}
