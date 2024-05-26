package nethical.digipaws.services;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.view.accessibility.AccessibilityEvent;
import nethical.digipaws.data.ServiceData;
import nethical.digipaws.itemblockers.AppBlocker;
import nethical.digipaws.itemblockers.KeywordBlocker;
import nethical.digipaws.itemblockers.ViewBlocker;
import nethical.digipaws.utils.LoadAppList;


public class BlockerService extends AccessibilityService {
	
	
	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
        AppBlocker.performAction(new ServiceData(this,event));
		ViewBlocker.performAction(new ServiceData(this,event));
        KeywordBlocker.performAction(new ServiceData(this,event));
	}
	
	@Override
	public void onInterrupt() {
		// Handle accessibility service interruption
	}
	
	@Override
	protected void onServiceConnected() {
		super.onServiceConnected();
		AccessibilityServiceInfo info = new AccessibilityServiceInfo();
		info.eventTypes =
		AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
		| AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
        | AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED
        | AccessibilityEvent.TYPE_VIEW_FOCUSED;
		info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
		info.packageNames = LoadAppList.getPackageNames(this).stream().toArray(String[]::new);
		setServiceInfo(info);
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
    
    
    
    
    
}