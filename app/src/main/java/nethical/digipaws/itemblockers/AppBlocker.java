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
    
    private boolean isSettingsBlocked=false;
    
    private float lastWarningTimestamp = 0f;
    private float lastOverlayTimestamp = 0f;
    private float lastGlobalActionTimestamp = 0f;
    
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
        
        if(SurvivalModeManager.isSurvivalModeActive(data.getService())){
           for (String blockedPackageName : BlockerData.nonBlockedPackages) {
            if(!blockedPackageName.equals(data.getPackageName())){
               pressHome();
                return;
                }
            }
         }
        
        
        for (String blockedPackageName : data.getBlockedApps()) {
            if(blockedPackageName.equals(data.getPackageName())){
               punish();
                break;
            }
        }
        
        if(data.isReelsBlocked()){
           for (String blockedPackageName : BlockerData.shortsApplications) {
            // using contains instead of equals cuz tikok has multiple package names :sad:
            if(data.getPackageName().contains(blockedPackageName)){ 
               punish();
                break;
            }
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
                        pressHome();
                        overlayManager.removeOverlay();
                        isOverlayVisible = false;
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
                                overlayManager.removeOverlay();
                                isOverlayVisible = false;
                                },
                            ()->{
                                // Close button clicked
                                pressHome();
                                overlayManager.removeOverlay();
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
                        isOverlayVisible = false;
                        lastWarningTimestamp = SystemClock.uptimeMillis();
                    },
                    ()->{
                        // Close button clicked
                        pressHome();
                        overlayManager.removeOverlay();
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