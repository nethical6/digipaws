package nethical.digipaws.data;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import nethical.digipaws.utils.DigiConstants;
import nethical.digipaws.utils.OverlayManager;

public class ServiceData {

    private AccessibilityService service;
    private AccessibilityEvent event;

    private String blockerId = "ok";
    private int difficulty = DigiConstants.DIFFICULTY_LEVEL_EXTREME;

    private List<String> blockedApps = null;

    private boolean isReelsBlocked = true;
    private boolean isEngagementBlocked = false;
    private boolean isPornBlocked = false;
    
    
    private boolean isSettingsBlocked = false;

    private WindowManager windowManager = null;
    private OverlayManager overlayManager;

    private int delay = 120000;

    public ServiceData(AccessibilityService service, int difficulty) {
        this.difficulty = difficulty;
        this.service = service;
        windowManager = (WindowManager) service.getSystemService(service.WINDOW_SERVICE);
    }

    public AccessibilityService getService() {
        return this.service;
    }

    public void setService(AccessibilityService service) {
        this.service = service;
    }

    public AccessibilityEvent getEvent() {
        return this.event;
    }

    public void setEvent(AccessibilityEvent event) {
        this.event = event;
    }

    public String getPackageName() {
        return event.getPackageName().toString();
    }

    public String getBlockerId() {
        return this.blockerId;
    }

    public void setBlockerId(String blockerId) {
        this.blockerId = blockerId;
    }

    public int getDifficulty() {
        return this.difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public boolean isReelsBlocked() {
        return this.isReelsBlocked;
    }

    public void setReelsBlocked(boolean isReelsBlocked) {
        this.isReelsBlocked = isReelsBlocked;
    }

    public boolean isEngagementBlocked() {
        return this.isEngagementBlocked;
    }

    public void setEngagementBlocked(boolean isEngagementBlocked) {
        this.isEngagementBlocked = isEngagementBlocked;
    }
    
    public boolean isPornBlocked() {
        return this.isPornBlocked;
    }

    public void setPornBlocked(boolean isPornBlocked) {
        this.isPornBlocked = isPornBlocked;
    }

    public WindowManager getWindowManager() {

        return this.windowManager;
    }

    public OverlayManager getOverlayManager() {
        return this.overlayManager;
    }

    public void setOverlayManager(OverlayManager overlayManager) {
        this.overlayManager = overlayManager;
    }

    public boolean isSettingsBlocked() {
        return this.isSettingsBlocked;
    }

    public void isSettingsBlocked(boolean isSettingsBlocked) {
        this.isSettingsBlocked = isSettingsBlocked;
    }

    public List<String> getBlockedApps() {
        return this.blockedApps;
    }

    public void setBlockedApps() {
        SharedPreferences sharedPreferences =
                getService()
                        .getSharedPreferences(
                                DigiConstants.PREF_BLOCKED_APPS_FILE, Context.MODE_PRIVATE);

        blockedApps =
                new ArrayList<>(
                        sharedPreferences.getStringSet(
                                DigiConstants.PREF_BLOCKED_APPS_LIST_KEY, new HashSet<>()));
    }

    public int getDelay() {
        return this.delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}
