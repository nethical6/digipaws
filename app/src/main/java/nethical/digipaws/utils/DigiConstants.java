package nethical.digipaws.utils;

public class DigiConstants {
	
	public static final int RADAR_RADIUS = 200; // in meters
	
	public static final String PREF_PACKAGES_FILE = "package_names";
	public static final String PREF_PACKAGES_KEY = "packages";
	
	public static final String PREF_VIEWBLOCKER_CONFIG_FILE = "view_blocker_config";
	public static final String PREF_VIEWBLOCKER_SHORTS_KEY = "isShortsBlocked";
	public static final String PREF_VIEWBLOCKER_ENGAGEMENT_KEY = "isEngagementBlocked";
	
    public static final String PREF_APPBLOCKER_CONFIG_FILE = "app_blocker_config";
    public static final String PREF_APPBLOCKER_SETTINGS_BLOCKER_KEY = "is_settings_disabled";
    public static final String SETTINGS_PACKAGE_NAME = "com.android.settings";
    
    public static final String PREF_SURVIVAL_MODE_CONFIG_FILE = "survival_mode_config";
    public static final String PREF_SURVIVAL_MODE_IS_ENABLED_KEY = "is_survival_active";
    
    public static final String PREF_COIN_DATA_FILE = "coin_info";
    public static final String PREF_COIN_KEY = "coins";
    
	public static final String PREF_PUNISHMENT_FILE = "punishment_info";
	public static final String PREF_PUNISHMENT_DIFFICULTY_KEY = "difficulty";
	public static final int DIFFICULTY_LEVEL_EASY = 0;
	public static final int DIFFICULTY_LEVEL_NORMAL = 1;
	public static final int DIFFICULTY_LEVEL_EXTREME = 2;
	
	public static final String VIEWID_SEPERATOR = ":id/";
	
    public static final String SHORTS_BLOCKER_ID = "shorts";
    public static final String ENGAGEMENT_BLOCKER_ID = "engagement";
    
	public static final String PREF_BLOCKER_LAST_WARNING_TIME_KEY = "last_warning_time";
    
    // delay configs for actions like back press, home button etc...
    public static final String PREF_GLOBAL_ACTIONS_COOLDOWN_FILE = "global_action";
	public static final String PREF_GLOBAL_ACTION_TIME_KEY = "last_global_press_time";
    public static final int GLOBAL_ACTIONS_COOLDOWN_DELAY = 1500; // in ms
	
    // delay configs for overlays
    public static final String PREF_OVERLAY_COOLDOWN_FILE = "overlay_cooldown";
	public static final String PREF_LAST_OVERLAY_TIME_KEY = "last_overlay_time";
    public static final int OVERLAY_COOLDOWN_DELAY = 5000;
    
    
    public static final String NOTIFICATION_CHANNEL = "digipaws";
    
    public static final String KEY_RADAR_LATITUDE = "radar_lat";
    public static final String KEY_RADAR_LONGITUDE = "radar_lon";
    
    public static final String PREF_QUEST_INFO_FILE = "overlay_cooldown";
    public static final String PREF_IS_QUEST_RUNNING_KEY = "is_quest_running";
	public static final String PREF_QUEST_ID_KEY = "quest_id";
    
    public static final String QUEST_ID_MARATHON = "marathon";
	public static final String QUEST_ID_NULL = "null";
	
    public static final String PREF_BLOCKED_APPS_FILE = "blocked_apps";
    public static final String PREF_BLOCKED_APPS_LIST_KEY = "blocked";
    
}