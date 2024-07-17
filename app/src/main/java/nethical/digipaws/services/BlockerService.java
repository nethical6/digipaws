package nethical.digipaws.services;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.view.accessibility.AccessibilityEvent;
import java.util.ArrayList;
import java.util.HashSet;
import nethical.digipaws.data.ServiceData;
import nethical.digipaws.itemblockers.AppBlocker;
import nethical.digipaws.itemblockers.KeywordBlocker;
import nethical.digipaws.itemblockers.SettingsBlocker;
import nethical.digipaws.itemblockers.ViewBlocker;
import nethical.digipaws.utils.DelayManager;
import nethical.digipaws.utils.DigiConstants;
import nethical.digipaws.utils.LoadAppList;
import nethical.digipaws.utils.OverlayManager;

public class BlockerService extends AccessibilityService {
	
    private ViewBlocker viewBlocker;
    
    private  SharedPreferences configData;
    
    private AppBlocker appBlocker;
    private ServiceData serviceData;
    private KeywordBlocker keywordBlocker;
    
    private boolean isAntiUninstallOn = false;
    private SettingsBlocker settingsBlocker;

    private long lastDataRefreshed = 0;

	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {

        if(event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED && DelayManager.isDelayOver(lastDataRefreshed,20000)){

            configData = getSharedPreferences(DigiConstants.PREF_APP_CONFIG,Context.MODE_PRIVATE);

            serviceData = new ServiceData(this,configData.getInt(DigiConstants.PREF_MODE,DigiConstants.DIFFICULTY_LEVEL_EASY));

            serviceData.setDelay(configData.getInt(DigiConstants.PREF_DELAY,120000));
            serviceData.setReelsBlocked(configData.getBoolean(DigiConstants.PREF_IS_SHORTS_BLOCKED,false));
            serviceData.setPornBlocked(configData.getBoolean(DigiConstants.PREF_IS_PORN_BLOCKED,false));
            serviceData.setEngagementBlocked(configData.getBoolean(DigiConstants.PREF_IS_ENGMMT_BLOCKED,false));
            serviceData.setBlockedApps(new ArrayList<>(
                            configData.getStringSet(
                                    DigiConstants.PREF_BLOCKED_APPS_LIST_KEY, new HashSet<>())));
            isAntiUninstallOn = configData.getBoolean(DigiConstants.PREF_IS_ANTI_UNINSTALL,false);

            lastDataRefreshed = SystemClock.uptimeMillis();
        }
        
        
        serviceData.setEvent(event);
        
        
        if(event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED || event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED){
            
            viewBlocker.performAction(serviceData);
        }
        
        
        if(keywordBlocker.isEdFocused()&&event.getEventType()==AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED){
            keywordBlocker.performAction(serviceData);
        }
        if(event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED){
            appBlocker.performAction(serviceData);
        }
        
        if(event.getEventType()==AccessibilityEvent.TYPE_VIEW_FOCUSED){
            keywordBlocker.checkIfEditext(serviceData);
        }
       if (isAntiUninstallOn && event.getPackageName() != null &&
            event.getPackageName().toString().equals(DigiConstants.SETTINGS_PACKAGE_NAME)
        && event.getEventType()==AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED ) {
            settingsBlocker.performAction(serviceData);
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
        | AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
        | AccessibilityEvent.TYPE_VIEW_FOCUSED;
        
		info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
		info.packageNames = LoadAppList.getPackageNames(this).stream().toArray(String[]::new);
		setServiceInfo(info);
        
        SharedPreferences configData = getSharedPreferences(DigiConstants.PREF_APP_CONFIG,Context.MODE_PRIVATE);

        serviceData = new ServiceData(this,configData.getInt(DigiConstants.PREF_MODE,DigiConstants.DIFFICULTY_LEVEL_EASY));
        
        serviceData.setOverlayManager(new OverlayManager(serviceData));
        
        serviceData.setDelay(configData.getInt(DigiConstants.PREF_DELAY,120000));
        serviceData.setReelsBlocked(configData.getBoolean(DigiConstants.PREF_IS_SHORTS_BLOCKED,false));
        serviceData.setPornBlocked(configData.getBoolean(DigiConstants.PREF_IS_PORN_BLOCKED,false));
        serviceData.setEngagementBlocked(configData.getBoolean(DigiConstants.PREF_IS_ENGMMT_BLOCKED,false));
        serviceData.setBlockedApps(new ArrayList<>(
                        configData.getStringSet(
                                DigiConstants.PREF_BLOCKED_APPS_LIST_KEY, new HashSet<>())));
        serviceData.setIsRebootBlocked(configData.getBoolean(DigiConstants.PREF_IS_ANTI_REBOOT,false));
        isAntiUninstallOn = configData.getBoolean(DigiConstants.PREF_IS_ANTI_UNINSTALL,false);
        
        viewBlocker = new ViewBlocker();
        appBlocker = new AppBlocker();
        keywordBlocker = new KeywordBlocker();
		settingsBlocker = new SettingsBlocker();
        lastDataRefreshed = SystemClock.uptimeMillis();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
    
    
    
    
    
}