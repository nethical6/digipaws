package nethical.digipaws.itemblockers;

import android.os.SystemClock;
import nethical.digipaws.data.BlockerData;
import nethical.digipaws.data.ServiceData;
import nethical.digipaws.utils.CoinManager;
import nethical.digipaws.utils.DelayManager;
import nethical.digipaws.utils.DigiConstants;
import nethical.digipaws.utils.DigiUtils;
import nethical.digipaws.utils.OverlayManager;
import nethical.digipaws.utils.OverlayManager;
import nethical.digipaws.utils.SurvivalModeManager;

public class AppBlocker {

    
    private float lastWarningTimestamp = 0f;
    private float removeOverlayTimestamp = 0f;
    private float lastGlobalActionTimestamp = 0f;
    
    private boolean isOverlayVisible = false;
    private ServiceData data;
    private int difficulty = DigiConstants.DIFFICULTY_LEVEL_EASY;

    public void performAction(ServiceData data){

        if(isOverlayVisible){
            return;
        }

        difficulty = data.getDifficulty();


        this.data = data;

        
        if(SurvivalModeManager.isSurvivalModeActive(data.getService())){
                pressHome();
                return;
         }
        
        
        for (String blockedPackageName : data.getBlockedApps()) {
            if(blockedPackageName.equals(data.getPackageName())){
               punish();
                break;
            }
        }
        
        if(data.isReelsBlocked()){
            if(BlockerData.shortsApplications.containsKey(data.getPackageName())){
                punish();
            }
           }
        
    }
    
   public void punish(){
        if(!DelayManager.isDelayOver(removeOverlayTimestamp,1000) || isOverlayVisible){
            return;
        }
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
                        removeOverlayTimestamp = SystemClock.uptimeMillis();
                    },
                    ()->{
                        // Close button clicked
                        pressHome();
                        overlayManager.removeOverlay();
                        isOverlayVisible = false;
                        removeOverlayTimestamp = SystemClock.uptimeMillis();
                    }
                    );
                    isOverlayVisible = true;
                }
                break;
            
            case(DigiConstants.DIFFICULTY_LEVEL_EXTREME):
                pressHome();
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
                                isOverlayVisible = false;
                                overlayManager.removeOverlay();
                                removeOverlayTimestamp = SystemClock.uptimeMillis();
                                },
                            ()->{
                                // Close button clicked
                                pressHome();
                                overlayManager.removeOverlay();
                                isOverlayVisible = false;
                                removeOverlayTimestamp = SystemClock.uptimeMillis();
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
                        pressHome();
                        overlayManager.removeOverlay();
                        removeOverlayTimestamp = SystemClock.uptimeMillis();
                        isOverlayVisible = false;
                    }
                    );
                    isOverlayVisible = true;
                }
                break;
		}
	}
    private void pressHome(){
         if(DelayManager.isDelayOver(lastGlobalActionTimestamp,DigiConstants.GLOBAL_ACTION_COOLDOWN)){
                   DigiUtils.pressHome(data.getService());
                    lastGlobalActionTimestamp = SystemClock.uptimeMillis();
            }
    }
    
}