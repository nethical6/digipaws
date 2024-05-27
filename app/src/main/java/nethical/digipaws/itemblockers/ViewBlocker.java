package nethical.digipaws.itemblockers;

import android.accessibilityservice.AccessibilityService;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.SystemClock;
import android.view.View;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import nethical.digipaws.R;
import nethical.digipaws.data.BlockerData;
import nethical.digipaws.data.ServiceData;
import nethical.digipaws.utils.CoinManager;
import nethical.digipaws.utils.DelayManager;
import nethical.digipaws.utils.DigiConstants;
import nethical.digipaws.utils.DigiUtils;
import nethical.digipaws.utils.OverlayManager;
import nethical.digipaws.utils.SurvivalModeManager;

public class ViewBlocker {
	
    private Context context;
    
    private boolean isReelsBlocked=true;
    private boolean isEngagementBlocked=true;
    private int difficulty = DigiConstants.DIFFICULTY_LEVEL_NORMAL;
    
    private float lastWarningTimestamp = 0f;
    private float lastOverlayTimestamp = 0f;
    private boolean isOverlayVisible = false;
    private ServiceData data;
    
    private void init(){
		isReelsBlocked = data.isReelsBlocked();
        isEngagementBlocked = data.isEngagementBlocked();
    }
    
	public void performAction(ServiceData data){
        this.data = data;
            
        init();
		
		// block short-form content
		if(isReelsBlocked){
			performShortsAction(data);
		}
		
		// block comments and video descriptions
		if(isEngagementBlocked){
			performEngagementAction(data);
		}
	}
	
	private void performShortsAction(ServiceData data){
		
		AccessibilityNodeInfo rootNode = data.getService().getRootInActiveWindow();
		
        for (int i = 0; i < BlockerData.shortsViewIds.length; i++) {
            if(isViewOpened(rootNode,BlockerData.shortsViewIds[i])){
                punish(data);
                return;
		    }
        }
        if(isOverlayVisible){
            data.getOverlayManager().removeOverlay();
            isOverlayVisible=false;
        }
	
	}
	
	
	private void performEngagementAction(ServiceData data){
		AccessibilityNodeInfo rootNode = data.getService().getRootInActiveWindow();
		for (int i = 0; i < BlockerData.engagementPanelViewIds.length; i++) {
            if(isViewOpened(rootNode,BlockerData.engagementPanelViewIds[i])){
			    DigiUtils.pressBack(data.getService());
                break;
			    
		    }
        }
        
        
	}
	
	public void punish(ServiceData data){
		switch(difficulty){
			case(DigiConstants.DIFFICULTY_LEVEL_EASY):
            
                break;
            
            case(DigiConstants.DIFFICULTY_LEVEL_EXTREME):
                DigiUtils.pressBack(data.getService());
                break;
            
            case(DigiConstants.DIFFICULTY_LEVEL_NORMAL):
            // Check if warning cooldown is over
               if(DelayManager.isDelayOver(lastWarningTimestamp)){
                  // prevents creating multiple instances of overlays
                    if(isOverlayVisible){
                        break;
                    }
                    OverlayManager overlayManager = data.getOverlayManager();
				    overlayManager.showSMUseCoinsOverlay(()->{
                        // Proceed Button clickdd
                        CoinManager.decrementCoin(data.getService());
                        overlayManager.removeOverlay();
                        isOverlayVisible = false;
                        lastWarningTimestamp = SystemClock.uptimeMillis();
                    },
                    ()->{
                        // Close button clicked
                        DigiUtils.pressBack(data.getService());
                        overlayManager.removeOverlay();
                        isOverlayVisible = false;
                    }
                    );
                    isOverlayVisible = true;
                }
                break;
		}
	}
	
	
	
	
	
	private boolean isViewOpened(AccessibilityNodeInfo rootNode,String viewId){
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