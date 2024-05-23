package nethical.digipaws.data;
import nethical.digipaws.utils.DigiConstants;

public class BlockerData {
    
    public static final String[] shortsViewIds = {
        "com.google.android.youtube" + DigiConstants.VIEWID_SEPERATOR + "reel_recycler",
        "com.instagram.android" + DigiConstants.VIEWID_SEPERATOR + "root_clips_layout"
    };
    
    public static final String[] engagementPanelViewIds = {
        "com.google.android.youtube" + DigiConstants.VIEWID_SEPERATOR + "engagement_panel_content",
        "com.instagram.android" + DigiConstants.VIEWID_SEPERATOR + "comment_composer_parent"
  
    };
    
    public static final String[] nonBlockedPackages = {
        DigiConstants.SETTINGS_PACKAGE_NAME ,
        "com.google.android.dialer",
        "nethical.digipaws",
        "com.google.android.deskclock",
        "com.android.systemui",
        "com.android.phone"
  
    };
    
}
