package nethical.digipaws.itemblockers;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    private boolean isOverlayVisible = false;
    private ServiceData data;
    
    private void init(){
		isSettingsBlocked = data.isReelsBlocked();
    }
    
    
    public void performAction(ServiceData data){
        this.data = data;
        init();
        
        
        for (String blockedPackageName : data.getBlockedApps()) {
            if(blockedPackageName.equals(data.getPackageName())){
                 // Check if warning cooldown is over
               if(DelayManager.isDelayOver(lastWarningTimestamp)){
                  // prevents creating multiple instances of overlays
                    if(isOverlayVisible){
                        break;
                    }
                    OverlayManager overlayManager = data.getOverlayManager();
				    overlayManager.showOverlay(data.getDifficulty(),()->{
                        // Proceed Button clickdd
                        CoinManager.decrementCoin(data.getService());
                        overlayManager.removeOverlay();
                        isOverlayVisible = false;
                        lastWarningTimestamp = SystemClock.uptimeMillis();},
                    ()->{
                        // Close button clicked
                        DigiUtils.pressHome(data.getService());
                        overlayManager.removeOverlay();
                        isOverlayVisible = false;
                    });
                    isOverlayVisible = true;
                }
                break;
        }
    
    }
        }
}