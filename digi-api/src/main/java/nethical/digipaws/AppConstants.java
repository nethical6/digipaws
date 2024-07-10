package nethical.digipaws;

import android.net.Uri;

public class AppConstants {
    
    public static final String PERMISSION_MANAGE_QUESTS = "nethical.digipaws.permission.API_V1";
    
    public static final String PROVIDER_AUTHORITY = "nethical.digipaws.questprovider";
    
    // get coin count (returns int)
    public static final Uri CONTENT_URI_COIN =
            Uri.parse("content://" + PROVIDER_AUTHORITY + "/coin");
    
    // get what mode the user is currently using (returns int)
    public static final Uri CONTENT_URI_MODE =
            Uri.parse("content://" + PROVIDER_AUTHORITY + "/mode");
    
    // check if focus mode/quest has been enabled (returns boolean)
    public static final Uri CONTENT_URI_FOCUS_QUEST =
            Uri.parse("content://" + PROVIDER_AUTHORITY + "/focus");
    public static final Uri CONTENT_URI_UPDATE_DATA_DELAY =
            Uri.parse("content://" + PROVIDER_AUTHORITY + "/get_delay_details");
    
    // add a quest to the list of quests inside the app
    public static final Uri CONTENT_URI_ADD_TO_QUESTS =
            Uri.parse("content://" + PROVIDER_AUTHORITY + "/add_to_quests");
    // check if quest is added
    public static final Uri CONTENT_URI_IS_ADDED_TO_QUESTS =
            Uri.parse("content://" + PROVIDER_AUTHORITY + "/is_quest");
    
    
    // These are the possible modes
    public static final int DIFFICULTY_LEVEL_EASY = 0;
	public static final int DIFFICULTY_LEVEL_NORMAL = 1; // NORMAL MODE == ADVENTURE MODE
	public static final int DIFFICULTY_LEVEL_EXTREME = 2;
	
    
    
    
}
