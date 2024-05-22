package nethical.digipaws.itemblockers;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.view.View;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import nethical.digipaws.R;
import nethical.digipaws.data.BlockerData;
import nethical.digipaws.utils.DelayManager;
import nethical.digipaws.utils.DigiConstants;
import nethical.digipaws.utils.DigiUtils;
import nethical.digipaws.utils.OverlayManager;

public class ViewBlocker {
	
	public static void performAction(AccessibilityService service){
		SharedPreferences preferences = service.getSharedPreferences(DigiConstants.PREF_VIEWBLOCKER_CONFIG_FILE,Context.MODE_PRIVATE);
		
		// block short-form content
		if(preferences.getBoolean(DigiConstants.PREF_VIEWBLOCKER_SHORTS_KEY,true)){
			performShortsAction(service);
		}
		
		// block comments and video descriptions
		if(preferences.getBoolean(DigiConstants.PREF_VIEWBLOCKER_ENGAGEMENT_KEY,true)){
			performEngagementAction(service);
		}
	}
	
	private static void performShortsAction(AccessibilityService service){
		
		AccessibilityNodeInfo rootNode = service.getRootInActiveWindow();
		
        for (int i = 0; i < BlockerData.shortsViewIds.length; i++) {
            if(isViewOpened(rootNode,BlockerData.shortsViewIds[i])){
			    punish(service,DigiConstants.SHORTS_BLOCKER_ID);
                break;
			    
		    }
        }
	
	}
	
	
	private static void performEngagementAction(AccessibilityService service){
		AccessibilityNodeInfo rootNode = service.getRootInActiveWindow();
		
		for (int i = 0; i < BlockerData.engagementPanelViewIds.length; i++) {
            if(isViewOpened(rootNode,BlockerData.engagementPanelViewIds[i])){
			    pressBack(service);
                break;
			    
		    }
        }
        
        
	}
	
	public static void punish(AccessibilityService service,String blockerId){
		SharedPreferences preferences = service.getSharedPreferences(DigiConstants.PREF_VIEWBLOCKER_CONFIG_FILE,Context.MODE_PRIVATE);
		
		int difficulty = preferences.getInt(DigiConstants.PREF_PUNISHMENT_DIFFICULTY_KEY,DigiConstants.DIFFICULTY_LEVEL_EASY);
		switch(difficulty){
			case(DigiConstants.DIFFICULTY_LEVEL_EASY):
                if(DelayManager.isWarningDelayOver(service,blockerId)){
                    DigiUtils.pressBack(service);
                    OverlayManager overlayManager = new OverlayManager(service,blockerId);
				    overlayManager.showWarningOverlay();
                }
                break;
            
            case(DigiConstants.DIFFICULTY_LEVEL_EXTREME):
                DigiUtils.pressBack(service);
                break;
            
            case(DigiConstants.DIFFICULTY_LEVEL_NORMAL):
                
            
            
				
			
		}
	}
	
	
	
	
	
	private static boolean isViewOpened(AccessibilityNodeInfo rootNode,String viewId){
		AccessibilityNodeInfo viewNode =
		findElementById(rootNode, viewId);
		if (viewNode != null) {
			// view found
			return true;
		}
		return false;
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