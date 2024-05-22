package nethical.digipaws.data;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import nethical.digipaws.utils.DigiConstants;

public class ServiceData {

    private AccessibilityService service;
    private AccessibilityEvent event;
    private String blockerId = DigiConstants.SHORTS_BLOCKER_ID;

    public ServiceData(AccessibilityService service, AccessibilityEvent event) {
        this.service = service;
        this.event = event;
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
}
