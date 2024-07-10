package nethical.digipaws.itemblockers;

import android.content.Context;
import android.os.SystemClock;
import android.view.accessibility.AccessibilityNodeInfo;
import nethical.digipaws.data.BlockerData;
import nethical.digipaws.data.ServiceData;
import nethical.digipaws.utils.CoinManager;
import nethical.digipaws.utils.DelayManager;
import nethical.digipaws.utils.DigiConstants;
import nethical.digipaws.utils.DigiUtils;
import nethical.digipaws.utils.OverlayManager;

public class ViewBlocker {
	
    private Context context;
    
    private boolean isReelsBlocked=true;
    private boolean isEngagementBlocked=true;
    private int difficulty = DigiConstants.DIFFICULTY_LEVEL_EASY;
    
    private float lastWarningTimestamp = 0f;
    private float lastOverlayTimestamp = 0f;
    private float removeOverlayTimestamp = 0f;
    private float lastGlobalActionTimestamp = 0f;
    
    private boolean isOverlayVisible = false;
    private ServiceData data;
    
    private void init(){
        difficulty = data.getDifficulty();
		isReelsBlocked = data.isReelsBlocked();
        isEngagementBlocked = data.isEngagementBlocked();
    }
    
	public void performAction(ServiceData data){
        if(isOverlayVisible) { return;}
        if(!DelayManager.isDelayOver(removeOverlayTimestamp,1000)){return;}
        this.data = data;
            
        init();
		
		// block short-form content
		if(isReelsBlocked){
			performShortsAction();
		}
		
		// block comments and video descriptions
		if(isEngagementBlocked){
			performEngagementAction();
		}
	}
    
  
	private void performShortsAction(){
		
		AccessibilityNodeInfo rootNode = data.getService().getRootInActiveWindow();
		
        for (int i = 0; i < BlockerData.shortsViewIds.length; i++) {
            if(isViewOpened(rootNode,BlockerData.shortsViewIds[i])){
                punish();
                return;
		    }
        }
        if(isOverlayVisible){
            data.getOverlayManager().removeOverlay();
            isOverlayVisible=false;
        }
	
	}
    
	
	
	private void performEngagementAction(){
		AccessibilityNodeInfo rootNode = data.getService().getRootInActiveWindow();
		for (int i = 0; i < BlockerData.engagementPanelViewIds.length; i++) {
            if(isViewOpened(rootNode,BlockerData.engagementPanelViewIds[i])){
			    pressBack();
		    }
        }
        
        
	}
	
	public void punish(){
		switch(difficulty){
			case(DigiConstants.DIFFICULTY_LEVEL_EASY):
            // Check if warning cooldown is over
               if(DelayManager.isDelayOver(lastWarningTimestamp,data.getDelay())){
                  // prevents creating multiple instances of overlays
                    if(isOverlayVisible){
                        break;
                    }
                    OverlayManager overlayManager = data.getOverlayManager();
				    overlayManager.showOverlay(difficulty,()->{
                        // Proceed Button clickdd
                        overlayManager.removeOverlay();
                        isOverlayVisible = false;
                        lastWarningTimestamp = SystemClock.uptimeMillis();
                    },
                    ()->{
                        // Close button clicked
                        pressBack();
                        overlayManager.removeOverlay();
                        isOverlayVisible = false;
                    }
                    );
                    isOverlayVisible = true;
                }
                break;
            
            case(DigiConstants.DIFFICULTY_LEVEL_EXTREME):
                pressBack();
                break;
            
                    
            case(DigiConstants.DIFFICULTY_LEVEL_NORMAL):
            // Check if warning cooldown is over
               if(DelayManager.isDelayOver(lastWarningTimestamp,DigiConstants.ADVENTURE_MODE_COOLDOWN)){
                  // prevents creating multiple instances of overlays
                    if(isOverlayVisible){
                        break;
                    }
                    OverlayManager overlayManager = data.getOverlayManager();
                    int crnt_coins = CoinManager.getCoinCount(data.getService());
                    if(crnt_coins<=0){
                        overlayManager.showNoCoinsOverlay(()->{
                                //proceed button
                                overlayManager.removeOverlay();
                                isOverlayVisible = false;
                                removeOverlayTimestamp = SystemClock.uptimeMillis();
                    
                                },
                            ()->{
                                // Close button clicked
                                pressBack();
                                overlayManager.removeOverlay();
                                removeOverlayTimestamp = SystemClock.uptimeMillis();
                                isOverlayVisible = false;
                            }
                            );
                        isOverlayVisible = true;
                        break;
                    }
                
                
				    overlayManager.showOverlay(difficulty,()->{
                        // Proceed Button clickdd
                        CoinManager.decrementCoin(data.getService());
                        overlayManager.removeOverlay();
                        removeOverlayTimestamp = SystemClock.uptimeMillis();
                        isOverlayVisible = false;
                        lastWarningTimestamp = SystemClock.uptimeMillis();
                    },
                    ()->{
                        // Close button clicked
                        pressBack();
                        removeOverlayTimestamp = SystemClock.uptimeMillis();
                        overlayManager.removeOverlay();
                        isOverlayVisible = false;
                    }
                    );
                    isOverlayVisible = true;
                }
                break;
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
	private void pressBack(){
         if(DelayManager.isDelayOver(lastGlobalActionTimestamp,DigiConstants.GLOBAL_ACTION_COOLDOWN)){
                   DigiUtils.pressBack(data.getService());
                    lastGlobalActionTimestamp = SystemClock.uptimeMillis();
            }
    }
	
}