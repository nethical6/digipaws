package nethical.digipaws.services;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Service;
import android.view.accessibility.AccessibilityEvent;
import nethical.digipaws.data.ServiceData;
import nethical.digipaws.itemblockers.AppBlocker;
import nethical.digipaws.itemblockers.KeywordBlocker;
import nethical.digipaws.itemblockers.ViewBlocker;
import nethical.digipaws.utils.DigiConstants;
import nethical.digipaws.utils.LoadAppList;
import nethical.digipaws.utils.OverlayManager;


public class BlockerService extends AccessibilityService {
	
    private ViewBlocker viewBlocker;
    
    private AppBlocker appBlocker;
    private ServiceData serviceData;
    private KeywordBlocker keywordBlocker;
    
	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
        serviceData.setEvent(event);
        viewBlocker.performAction(serviceData);
        appBlocker.performAction(serviceData);
        keywordBlocker.performAction(serviceData);
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
        | AccessibilityEvent.TYPE_VIEW_FOCUSED;
		info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
		info.packageNames = LoadAppList.getPackageNames(this).stream().toArray(String[]::new);
		setServiceInfo(info);
        
        serviceData = new ServiceData(this,DigiConstants.DIFFICULTY_LEVEL_NORMAL);
        
        serviceData.setOverlayManager(new OverlayManager(serviceData));
        serviceData.setBlockedApps();
        
        viewBlocker = new ViewBlocker();
        appBlocker = new AppBlocker();
        keywordBlocker = new KeywordBlocker();
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
    
    
    
    
    
}