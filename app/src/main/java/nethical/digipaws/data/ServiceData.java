package nethical.digipaws.data;

import android.accessibilityservice.AccessibilityService;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import nethical.digipaws.utils.DigiConstants;
import nethical.digipaws.utils.OverlayManager;

public class ServiceData {

    private AccessibilityService service;
    private AccessibilityEvent event;
    private String blockerId = DigiConstants.SHORTS_BLOCKER_ID;
    private int difficulty = DigiConstants.DIFFICULTY_LEVEL_NORMAL;

    private boolean isReelsBlocked = true;
    private boolean isEngagementBlocked = true;
    private boolean isSettingsBlocked = false;

    private WindowManager windowManager = null;
    private OverlayManager overlayManager;

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

    public void isEngagementBlocked(boolean isEngagementBlocked) {
        this.isEngagementBlocked = isEngagementBlocked;
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
}
