package nethical.digipaws.services;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.SharedPreferences;
import android.os.Looper;
import android.os.Handler;
import android.view.accessibility.AccessibilityEvent;
import nethical.digipaws.itemblockers.ViewBlocker;
import nethical.digipaws.utils.DigiConstants;
import nethical.digipaws.utils.LoadAppList;


public class BlockerService extends AccessibilityService {
	
	
	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
		String packageName = String.valueOf(event.getPackageName());
		ViewBlocker.performAction(this);
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
		| AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
		info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
		info.packageNames = LoadAppList.getPackageNames(this).stream().toArray(String[]::new);
		setServiceInfo(info);
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}