package nethical.digipaws.itemblockers;

import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import nethical.digipaws.data.ServiceData;
import nethical.digipaws.utils.DigiUtils;

public class KeywordBlocker {
    
    public static void performAction(ServiceData data){
        if (data.getEvent().getEventType() == AccessibilityEvent.TYPE_VIEW_FOCUSED) {
            AccessibilityNodeInfo source = data.getEvent().getSource();
            if (source != null) {
                if (source.getClassName().equals("android.widget.EditText")) {
                    Log.d("nig", "EditText lost focus: " + source.getViewIdResourceName());
                    DigiUtils.pressHome(data.getService());
                    // Do something when EditText loses focus
                }
            }
        }
    }
}
