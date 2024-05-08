package nethical.digipaws.services;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.SharedPreferences;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;
import androidx.preference.PreferenceManager;
import nethical.digipaws.blockers.AppBlocker;
import nethical.digipaws.blockers.KeywordAction;
import nethical.digipaws.blockers.ViewBlocker;
import nethical.digipaws.Constants;
import nethical.digipaws.utils.DigiUtils;
import nethical.digipaws.utils.ListApps;

public class BlockerService extends AccessibilityService {

  @Override
  public void onAccessibilityEvent(AccessibilityEvent event) {
    String packageName = String.valueOf(event.getPackageName());
    SharedPreferences preferences =
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

    ViewBlocker.performAction(this, packageName);

    // the keyword blocker seems to have some problem idk
    boolean isPornBlocked = preferences.getBoolean(Constants.PORN_PREF, false);
    if (isPornBlocked) {
      KeywordAction.performAction(this, event);
    }
    AppBlocker.performAction(this, packageName);
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
        AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
            | AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
    info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
    info.packageNames = ListApps.getNonIgnoredPackageNames(getApplicationContext());

    setServiceInfo(info);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }
}
