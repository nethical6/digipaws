package nethical.digipaws.blockers;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import nethical.digipaws.Constants;
import nethical.digipaws.utils.DigiUtils;
import nethical.digipaws.utils.SurvivalModeConfig;
import nethical.digipaws.utils.data.BlockableView;
import nethical.digipaws.utils.data.SurvivalModeData;

import java.lang.reflect.Type;
import java.util.Map;

public class ViewBlocker {

    private Map<String, Boolean> viewBooleanMap;

    public static void performAction(AccessibilityService service, String packageName) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(service);

        if (DigiUtils.isCooldownActive(service)) {
            return; // Ignore action if cooldown is active
        }

        // Retrieve the map from shared preferences based on the package name
        Map<String, BlockableView> packageMap = getPackageMap(preferences, packageName);
        if (packageMap != null) {
            // DefaultAction.showToast(service,"class");
            AccessibilityNodeInfo rootNode = service.getRootInActiveWindow();
            DigiUtils.updateStatCount(service, Constants.BLOCK_TYPE_VIEW);
            // Iterate through the entries of the packageMap
            for (Map.Entry<String, BlockableView> entry : packageMap.entrySet()) {
                String viewId = entry.getValue().getViewId();
                Boolean isEnabled = entry.getValue().isEnabled();
                if (isEnabled == false || viewId == "null") {
                    continue;
                }

                AccessibilityNodeInfo viewNode =
                        findElementById(rootNode, packageName + ":id/" + viewId);
                if (viewNode != null) {
                    // view found
                    service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                    DigiUtils.updateStatCount(service, Constants.BLOCK_TYPE_VIEW);

                    // DefaultAction.startCooldown(service, preferences);
                    entry.getValue().decrementCounter();
                    int counter = entry.getValue().getCount();
                    //	DefaultAction.showToast(service, "You got " + String.valueOf(counter));
                    saveMapToPreferences(service, packageName, packageMap);

                    if (counter == 0) {
                        // user exceeded the threshold and punishment is started
                        DigiUtils.showToast(service, "Survival mode enabled");
                        SurvivalModeConfig.start(
                                service.getApplicationContext(),
                                new SurvivalModeData(
                                        true,
                                        SurvivalModeConfig.generateEndTime(
                                                service.getApplicationContext(), 1, 30),
                                        packageName,
                                        viewId));
                        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);

                    } else {
                        String msg =
                                "You have "
                                        + String.valueOf(counter)
                                        + " remaining attempts. Exceeding this limit will result in a penalty.";
                        DigiUtils.sendNotification(
                                service, "Blocked " + entry.getValue().getCommonName(), msg);
                        DigiUtils.updateStatCount(service, Constants.BLOCK_TYPE_VIEW);
                    }
                }
            }
        }
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

    public static Map<String, BlockableView> getPackageMap(
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

    public static AccessibilityNodeInfo findElementById(AccessibilityNodeInfo node, String id) {
        if (node == null) return null;
        AccessibilityNodeInfo targetNode = null;
        try {
            targetNode = node.findAccessibilityNodeInfosByViewId(id).get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return targetNode;
    }
}
