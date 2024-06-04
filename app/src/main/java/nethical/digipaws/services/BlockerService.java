package nethical.digipaws.services;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.accessibility.AccessibilityEvent;
import java.util.ArrayList;
import java.util.HashSet;
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
        
        if(event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED){
            viewBlocker.performAction(serviceData);
            appBlocker.performAction(serviceData);
        }
        
        if(event.getEventType()==AccessibilityEvent.TYPE_VIEW_FOCUSED){
            keywordBlocker.performAction(serviceData);
        }
        
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
         AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
        | AccessibilityEvent.TYPE_VIEW_FOCUSED;
		info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
		info.packageNames = LoadAppList.getPackageNames(this).stream().toArray(String[]::new);
		setServiceInfo(info);
        
        SharedPreferences sharedPreferences = getSharedPreferences(DigiConstants.PREF_APP_CONFIG,Context.MODE_PRIVATE);

        serviceData = new ServiceData(this,sharedPreferences.getInt(DigiConstants.PREF_MODE,DigiConstants.DIFFICULTY_LEVEL_EASY));
        
        serviceData.setOverlayManager(new OverlayManager(serviceData));
        
        serviceData.setDelay(sharedPreferences.getInt(DigiConstants.PREF_DELAY,120000));
        serviceData.setReelsBlocked(sharedPreferences.getBoolean(DigiConstants.PREF_IS_SHORTS_BLOCKED,false));
        serviceData.setPornBlocked(sharedPreferences.getBoolean(DigiConstants.PREF_IS_PORN_BLOCKED,false));
        serviceData.setEngagementBlocked(sharedPreferences.getBoolean(DigiConstants.PREF_IS_ENGMMT_BLOCKED,false));
        serviceData.setBlockedApps(new ArrayList<>(
                        sharedPreferences.getStringSet(
                                DigiConstants.PREF_BLOCKED_APPS_LIST_KEY, new HashSet<>())));
        viewBlocker = new ViewBlocker();
        appBlocker = new AppBlocker();
        keywordBlocker = new KeywordBlocker();
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
    
    
    
    
    
}