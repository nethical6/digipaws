package nethical.digipaws.itemblockers;

import android.os.SystemClock;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.io.IOException;

import nethical.digipaws.data.BlockerData;
import nethical.digipaws.data.ServiceData;
import nethical.digipaws.utils.CoinManager;
import nethical.digipaws.utils.DelayManager;
import nethical.digipaws.utils.DigiConstants;
import nethical.digipaws.utils.DigiUtils;
import nethical.digipaws.utils.OverlayManager;

public class ViewBlocker {

    private boolean isViewingFirstReelAllowed = false;
    private int difficulty = DigiConstants.DIFFICULTY_LEVEL_EASY;

    private float lastWarningTimestamp = 0f;
    private float removeOverlayTimestamp = 0f;
    private float lastGlobalActionTimestamp = 0f;

    private boolean isOverlayVisible = false;

    private boolean isReelsTabOpened = false;
    private ServiceData data;
    private int scrollEventCounter = 0;

    public void performAction(ServiceData data) {
        if (isOverlayVisible) {
            return;
        }

        if (!DelayManager.isDelayOver(removeOverlayTimestamp, 1000)) {
            //return;
        }
        this.data = data;

        difficulty = data.getDifficulty();
        boolean isReelsBlocked = data.isReelsBlocked();
        boolean isEngagementBlocked = data.isEngagementBlocked();
        isViewingFirstReelAllowed = data.isViewingFirstReelAllowed();

        if (isReelsTabOpened) {
            scrollEventCounter++;
            if (scrollEventCounter > 5) {
                scrollEventCounter = 0;
                isReelsTabOpened = false;
                performShortsAction(true);
            }
        }
        // block short-form content
        if (isReelsBlocked) {
            if (isViewingFirstReelAllowed) {
                performShortsAction(false);
            } else {
                performShortsAction(true);
            }
        }


        // block comments and video descriptions
        if (isEngagementBlocked) {
            performEngagementAction();
        }
        if (data.getIsRebootBlocked()) {
            performRebootBlock();
        }
    }


    private void performShortsAction(boolean triggerPunish) {

        AccessibilityNodeInfo rootNode = data.getService().getRootInActiveWindow();


        for (int i = 0; i < BlockerData.shortsViewIds.length; i++) {
            if (isViewOpened(rootNode, BlockerData.shortsViewIds[i])) {
                if (triggerPunish) {
                    punish();
                } else {
                    if (isViewingFirstReelAllowed) {
                        isReelsTabOpened = true;
                    }
                }
                return;
            }
        }
        if (isOverlayVisible) {
            data.getOverlayManager().removeOverlay();
            isOverlayVisible = false;
        }

    }

    private void performRebootBlock() {

        AccessibilityNodeInfo rootNode = data.getService().getRootInActiveWindow();

        for (int i = 0; i < BlockerData.rebootViewIds.length; i++) {
            if (isViewOpened(rootNode, BlockerData.rebootViewIds[i])) {
                DigiUtils.pressHome(data.getService());
                return;
            }
        }
        if (isViewOpened(rootNode, data.getPackageName() + DigiConstants.VIEWID_SEPERATOR + "sec_global_actions_icon")) {
            DigiUtils.pressHome(data.getService());
            return;
        }
        if (isOverlayVisible) {
            data.getOverlayManager().removeOverlay();
            isOverlayVisible = false;
        }

    }


    private void performEngagementAction() {
        AccessibilityNodeInfo rootNode = data.getService().getRootInActiveWindow();
        for (int i = 0; i < BlockerData.engagementPanelViewIds.length; i++) {
            if (isViewOpened(rootNode, BlockerData.engagementPanelViewIds[i])) {
                pressBack();
            }
        }


    }

    public void punish() {
        switch (difficulty) {
            case (DigiConstants.DIFFICULTY_LEVEL_EASY):
                // Check if warning cooldown is over
                if (DelayManager.isDelayOver(lastWarningTimestamp, data.getDelay())) {
                    // prevents creating multiple instances of overlays
                    if (isOverlayVisible) {
                        break;
                    }
                    OverlayManager overlayManager = data.getOverlayManager();
                    overlayManager.showOverlay(difficulty, () -> {
                                // Proceed Button clicked
                                overlayManager.removeOverlay();
                                isOverlayVisible = false;
                                lastWarningTimestamp = SystemClock.uptimeMillis();
                            },
                            () -> {
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

            case (DigiConstants.DIFFICULTY_LEVEL_EXTREME):
                try {
                    Toast.makeText(data.getService(), data.taskPicker.getRandomTask(), Toast.LENGTH_LONG).show();
                } catch (IOException ignored) {

                }
                pressBack();
                break;


            case (DigiConstants.DIFFICULTY_LEVEL_NORMAL):
                // Check if warning cooldown is over
                if (DelayManager.isDelayOver(lastWarningTimestamp, DigiConstants.ADVENTURE_MODE_COOLDOWN)) {
                    // prevents creating multiple instances of overlays
                    if (isOverlayVisible) {
                        break;
                    }
                    OverlayManager overlayManager = data.getOverlayManager();
                    int crnt_coins = CoinManager.getCoinCount(data.getService());
                    if (crnt_coins <= 0) {
                        overlayManager.showNoCoinsOverlay(() -> {
                                    //proceed button
                                    overlayManager.removeOverlay();
                                    isOverlayVisible = false;
                                    removeOverlayTimestamp = SystemClock.uptimeMillis();

                                },
                                () -> {
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


                    overlayManager.showOverlay(difficulty, () -> {
                                // Proceed Button clickdd
                                CoinManager.decrementCoin(data.getService());
                                overlayManager.removeOverlay();
                                removeOverlayTimestamp = SystemClock.uptimeMillis();
                                isOverlayVisible = false;
                                lastWarningTimestamp = SystemClock.uptimeMillis();
                            },
                            () -> {
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


    private static boolean isViewOpened(AccessibilityNodeInfo rootNode, String viewId) {
        AccessibilityNodeInfo viewNode =
                findElementById(rootNode, viewId);
        // view found
        return viewNode != null;
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

    private void pressBack() {
        if (DelayManager.isDelayOver(lastGlobalActionTimestamp, DigiConstants.GLOBAL_ACTION_COOLDOWN)) {
            DigiUtils.pressBack(data.getService());
            lastGlobalActionTimestamp = SystemClock.uptimeMillis();
        }
    }

}