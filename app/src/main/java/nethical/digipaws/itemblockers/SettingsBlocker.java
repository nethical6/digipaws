package nethical.digipaws.itemblockers;

import android.accessibilityservice.AccessibilityService;
import android.widget.Toast;
import nethical.digipaws.data.BlockerData;
import java.util.regex.Pattern;
import android.view.accessibility.AccessibilityNodeInfo;
import nethical.digipaws.data.ServiceData;
import nethical.digipaws.utils.DigiUtils;

public class SettingsBlocker extends KeywordBlocker{
    
    private ServiceData data;
    
    public void performAction(ServiceData data) {
        this.data = data;
        AccessibilityNodeInfo rootNode = data.getService().getRootInActiveWindow();
        traverseNodesForKeywords(
                        data.getService().getRootInActiveWindow(), data.getService());
    }
    
   public void traverseNodesForKeywords(
            AccessibilityNodeInfo node, AccessibilityService context) {
        if (node == null) {
            return;
        }
        if (node.getClassName() != null && node.getClassName().equals("android.widget.TextView")) {
            CharSequence nodeText = node.getText();
            if (nodeText != null) {
                String editTextContent = nodeText.toString().toLowerCase();
                if (editTextContent.toLowerCase().contains("digipaws")) {
                    DigiUtils.pressHome(context);
                }
            }
        }

        for (int i = 0; i < node.getChildCount(); i++) {
            AccessibilityNodeInfo childNode = node.getChild(i);
            traverseNodesForKeywords(childNode, context);
        }
    }

    
}
