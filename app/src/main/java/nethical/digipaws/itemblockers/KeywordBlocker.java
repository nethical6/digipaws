package nethical.digipaws.itemblockers;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import java.util.regex.Pattern;
import nethical.digipaws.data.BlockerData;
import nethical.digipaws.data.ServiceData;
import nethical.digipaws.utils.DigiUtils;

public class KeywordBlocker {

    public boolean isFocused = false;

    public void performAction(ServiceData data) {
        AccessibilityNodeInfo node = data.getEvent().getSource();

        if (node == null) {
            return;
        }
        if (!data.isPornBlocked()) {
            return;
        }

        if (isFocused) {
            if (data.getEvent().getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
                traverseNodesForKeywords(
                        data.getService().getRootInActiveWindow(), data.getService());
            }
        }
        
        
    }

    public void checkIfEditext(ServiceData data){
        AccessibilityNodeInfo node = data.getEvent().getSource();

        if (node == null) {
            return;
        }
        if (!data.isPornBlocked()) {
            return;
        }
        
        if (data.getEvent().getEventType() == AccessibilityEvent.TYPE_VIEW_FOCUSED) {
            if (node.getClassName() != null
                    && node.getClassName().equals("android.widget.EditText")) {
                isFocused = true;
            }
        }
    }
    public void traverseNodesForKeywords(
            AccessibilityNodeInfo node, AccessibilityService context) {
        if (node == null) {
            return;
        }
        if (node.getClassName() != null && node.getClassName().equals("android.widget.EditText")) {
            CharSequence nodeText = node.getText();
            if (nodeText != null) {
                String editTextContent = nodeText.toString().toLowerCase();
                if (containsKeyword(editTextContent)) {
                    // DefaultAction.showToast(servicex, "Keyword found in EditText: ");
                    DigiUtils.pressHome(context);
                    isFocused = false;
                }
            }
        }

        for (int i = 0; i < node.getChildCount(); i++) {
            AccessibilityNodeInfo childNode = node.getChild(i);
            traverseNodesForKeywords(childNode, context);
        }
    }

    private boolean containsKeyword(String text) {
        String lowerCaseText = text.toLowerCase().trim();

        // Split text into words and check each word against the HashMap
        String[] words = lowerCaseText.split("\\W+"); // Split by non-word characters

        for (String word : words) {
            if (BlockerData.pornKeywords.containsKey(word)) {
                return true;
            }
        }
        return false;
    }

    public boolean isEdFocused() {
        return this.isFocused;
    }

    
}
