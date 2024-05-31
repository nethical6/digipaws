package nethical.digipaws.itemblockers;

import android.os.SystemClock;
import nethical.digipaws.data.ServiceData;
import nethical.digipaws.utils.CoinManager;
import nethical.digipaws.utils.DelayManager;
import nethical.digipaws.utils.DigiConstants;
import nethical.digipaws.utils.DigiUtils;
import nethical.digipaws.utils.OverlayManager;
import nethical.digipaws.utils.OverlayManager;

public class AppBlocker {
    
    private boolean isSettingsBlocked=false;
    private float lastWarningTimestamp = 0f;
    private float lastOverlayTimestamp = 0f;
    private boolean isOverlayVisible = false;
    private ServiceData data;
    private int difficulty = DigiConstants.DIFFICULTY_LEVEL_EASY;
    
    
    private void init(){
		isSettingsBlocked = data.isReelsBlocked();
        difficulty = data.getDifficulty();
    }
    
    
    public void performAction(ServiceData data){
        this.data = data;
        init();
        
        
        for (String blockedPackageName : data.getBlockedApps()) {
            if(blockedPackageName.equals(data.getPackageName())){
               punish();
                break;
                
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
                        DigiUtils.pressHome(data.getService());
                        overlayManager.removeOverlay();
                        isOverlayVisible = false;
                    }
                    );
                    isOverlayVisible = true;
                }
                break;
            
            case(DigiConstants.DIFFICULTY_LEVEL_EXTREME):
                if(DelayManager.isDelayOver(lastWarningTimestamp,1500)){
                    DigiUtils.pressHome(data.getService());
                    lastWarningTimestamp = SystemClock.uptimeMillis();
                }
                break;
            
                    
            case(DigiConstants.DIFFICULTY_LEVEL_NORMAL):
            // Check if warning cooldown is over
               if(DelayManager.isDelayOver(lastWarningTimestamp)){
                  // prevents creating multiple instances of overlays
                    if(isOverlayVisible){
                        break;
                    }
                    OverlayManager overlayManager = data.getOverlayManager();
				    overlayManager.showOverlay(difficulty,()->{
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
    
}