package nethical.digipaws.services;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import java.util.ArrayList;
import java.util.HashSet;

import nethical.digipaws.R;
import nethical.digipaws.data.ServiceData;
import nethical.digipaws.itemblockers.AppBlocker;
import nethical.digipaws.itemblockers.KeywordBlocker;
import nethical.digipaws.itemblockers.SettingsBlocker;
import nethical.digipaws.itemblockers.ViewBlocker;
import nethical.digipaws.utils.DelayManager;
import nethical.digipaws.utils.DigiConstants;
import nethical.digipaws.utils.DigiUtils;
import nethical.digipaws.utils.LoadAppList;

public class BlockerService extends AccessibilityService {

    private ViewBlocker viewBlocker;

    private AppBlocker appBlocker;
    private ServiceData serviceData;
    private KeywordBlocker keywordBlocker;

    private boolean isAntiUninstallOn = false;
    private SettingsBlocker settingsBlocker;

    private float lastDataRefreshed = 0;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        Log.d("Event", String.valueOf(event.getEventType()));
        try {
            if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED && DelayManager.isDelayOver(lastDataRefreshed, 20000)) {
                refreshData();
            }
        } catch (Exception ignored) {
        }

        serviceData.setEvent(event);

        if(serviceData.isViewingFirstReelAllowed()){
            if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_SCROLLED ) {
                viewBlocker.performAction(serviceData);
            }
        } else {
            if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED ) {
                viewBlocker.performAction(serviceData);
            }
        }

        try {
            if (keywordBlocker.isEdFocused() && event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
                keywordBlocker.performAction(serviceData);
            }
            if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
                appBlocker.performAction(serviceData);
            }
        } catch (Exception ignored) {
        }

        try {

            if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_FOCUSED) {
                keywordBlocker.checkIfEditText(serviceData);
            }
        } catch (Exception ignored) {
        }


        try {
            if (isAntiUninstallOn && event.getPackageName() != null && event.getPackageName().toString().equals(DigiConstants.SETTINGS_PACKAGE_NAME) && event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
                settingsBlocker.performAction(serviceData);
            }
        } catch (Exception ignored) {
        }

    }

    @Override
    public void onInterrupt() {
        DigiUtils.sendNotification(this, "Service Interrupted", "if DigiPaws does not work, try re-enabling it in accessibility", R.drawable.info);
        // Handle accessibility service interruption
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED | AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED | AccessibilityEvent.TYPE_VIEW_FOCUSED | AccessibilityEvent.TYPE_VIEW_SCROLLED;

        refreshData();

        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        info.packageNames = LoadAppList.getPackageNames(this).toArray(new String[0]);
        setServiceInfo(info);

        viewBlocker = new ViewBlocker();
        appBlocker = new AppBlocker();
        keywordBlocker = new KeywordBlocker();
        settingsBlocker = new SettingsBlocker();
    }

    private void refreshData() {

        SharedPreferences configData = getSharedPreferences(DigiConstants.PREF_APP_CONFIG, Context.MODE_PRIVATE);

        serviceData = new ServiceData(this, configData.getInt(DigiConstants.PREF_MODE, DigiConstants.DIFFICULTY_LEVEL_EASY));

        serviceData.setDelay(configData.getInt(DigiConstants.PREF_DELAY, 120000));
        serviceData.setReelsBlocked(configData.getBoolean(DigiConstants.PREF_IS_SHORTS_BLOCKED, false));
        serviceData.setPornBlocked(configData.getBoolean(DigiConstants.PREF_IS_PORN_BLOCKED, false));
        serviceData.setEngagementBlocked(configData.getBoolean(DigiConstants.PREF_IS_ENGMMT_BLOCKED, false));
        serviceData.setViewingFirstReelAllowed(configData.getBoolean(DigiConstants.PREF_IS_PREVENT_BLOCKING_FIRST_REEL, false));

        isAntiUninstallOn = configData.getBoolean(DigiConstants.PREF_IS_ANTI_UNINSTALL, false);
        serviceData.setBlockedApps(new ArrayList<>(configData.getStringSet(DigiConstants.PREF_BLOCKED_APPS_LIST_KEY, new HashSet<>())));

        lastDataRefreshed = SystemClock.uptimeMillis();
    }


}