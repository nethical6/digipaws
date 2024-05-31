package nethical.digipaws.utils;

public class DigiConstants {
	
	public static final int RADAR_RADIUS = 200; // in meters
	
	public static final String PREF_PACKAGES_FILE = "package_names";
	public static final String PREF_PACKAGES_KEY = "packages";
	
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
    
    public static final String PREF_APP_CONFIG = "app_config";
    public static final String PREF_MODE = "mode";
    public static final String PREF_DELAY = "delay";
    public static final String PREF_IS_SHORTS_BLOCKED = "is_shorts_blocked";
    public static final String PREF_IS_PORN_BLOCKED = "is_porn_blocked";
    public static final String PREF_IS_ENGMMT_BLOCKED = "is_engmnt_blocked";
    
    public static final String PREF_IS_INTRO_SHOWN ="intro";
    
    
    public static final String PERMISSION_MANAGE_COINS = "nethical.digipaws.permission.API_V0";
    public static final String COIN_MANAGER_INCREMENT = "increment_coin";
    public static final String COIN_MANAGER_DECREMENT = "decrement_coin";
    public static final String COIN_MANAGER_NOTIF_DESC = "coin_desc";
    
    
}