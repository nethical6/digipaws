package nethical.digipaws.itemblockers;

import android.accessibilityservice.AccessibilityService;
import android.app.Service;
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
import nethical.digipaws.data.ServiceData;
import nethical.digipaws.utils.DelayManager;
import nethical.digipaws.utils.DigiConstants;
import nethical.digipaws.utils.DigiUtils;
import nethical.digipaws.utils.OverlayManager;
import nethical.digipaws.utils.SurvivalModeManager;

public class ViewBlocker {
	
	public static void performAction(ServiceData data){
		SharedPreferences preferences = data.getService().getSharedPreferences(DigiConstants.PREF_VIEWBLOCKER_CONFIG_FILE,Context.MODE_PRIVATE);
		
		// block short-form content
		if(preferences.getBoolean(DigiConstants.PREF_VIEWBLOCKER_SHORTS_KEY,true)){
			performShortsAction(data);
		}
		
		// block comments and video descriptions
		if(preferences.getBoolean(DigiConstants.PREF_VIEWBLOCKER_ENGAGEMENT_KEY,true)){
			performEngagementAction(data);
		}
	}
	
	private static void performShortsAction(ServiceData data){
		
		AccessibilityNodeInfo rootNode = data.getService().getRootInActiveWindow();
		
        for (int i = 0; i < BlockerData.shortsViewIds.length; i++) {
            if(isViewOpened(rootNode,BlockerData.shortsViewIds[i])){
                data.setBlockerId(DigiConstants.SHORTS_BLOCKER_ID);
			    punish(data);
                break;
			    
		    }
        }
	
	}
	
	
	private static void performEngagementAction(ServiceData data){
		AccessibilityNodeInfo rootNode = data.getService().getRootInActiveWindow();
		
		for (int i = 0; i < BlockerData.engagementPanelViewIds.length; i++) {
            if(isViewOpened(rootNode,BlockerData.engagementPanelViewIds[i])){
			    DigiUtils.pressBack(data.getService());
                break;
			    
		    }
        }
        
        
	}
	
	public static void punish(ServiceData data){
		SharedPreferences preferences = data.getService().getSharedPreferences(DigiConstants.PREF_VIEWBLOCKER_CONFIG_FILE,Context.MODE_PRIVATE);
		
		int difficulty = preferences.getInt(DigiConstants.PREF_PUNISHMENT_DIFFICULTY_KEY,DigiConstants.DIFFICULTY_LEVEL_NORMAL);
		switch(difficulty){
			case(DigiConstants.DIFFICULTY_LEVEL_EASY):
            // check if time limit has been surpassed
                if(DelayManager.isWarningDelayOver(data.getService(),data.getBlockerId())){
                  // prevents creating multiple instances of overlays
                    if(DelayManager.isOverlayCooldownActive(data.getService())){
                        DigiUtils.pressBack(data.getService());
                        break;
                    }
                    DigiUtils.pressBack(data.getService());
                    OverlayManager overlayManager = new OverlayManager(data.getService(),data.getBlockerId());
				    overlayManager.showWarningOverlay();
                    DelayManager.updateOverlayCooldown(data.getService());
                }
                break;
            
            case(DigiConstants.DIFFICULTY_LEVEL_EXTREME):
                DigiUtils.pressBack(data.getService());
                break;
            
            case(DigiConstants.DIFFICULTY_LEVEL_NORMAL):
                if(DelayManager.isOverlayCooldownActive(data.getService())){
                        DigiUtils.pressBack(data.getService());
                        break;
                    }
                    DigiUtils.pressBack(data.getService());
                    OverlayManager overlayManager = new OverlayManager(data.getService(),data.getBlockerId());
				    overlayManager.showSMUseCoinsOverlay(data);
                    DelayManager.updateOverlayCooldown(data.getService());
            
            
				
			
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
		//	e.printStackTrace();
		}
		return targetNode;
	}
	
	
}