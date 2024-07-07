package nethical.digipaws.utils;

public class DigiConstants {
	
	public static final int RADAR_RADIUS = 200; // in meters (distance to run in marathon quest)
	public static final int DEFAULT_REPS = 5; // default number of squats for 1 aura
    
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
	public static final int DIFFICULTY_LEVEL_NORMAL = 1; // NORMAL MODE == ADVENTURE MODE
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
    public static final String PREF_IS_ANTI_UNINSTALL = "is_uninstall_blocked";
    public static final String PREF_ANTI_UNINSTALL_START = "anti_uninstall_day";
    
    public static final String PREF_WARN_FILE = "warn_info";
    public static final String PREF_WARN_MESSAGE = "warn_msg";
    public static final String PREF_WARN_TITLE = "warn_title";
    
    
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    
    public static final String PREF_QUEST_MANAGING_PERM = "perm_config";
    
    
    public static final String PREF_IS_INTRO_SHOWN ="intro";
    
    
    public static final String PERMISSION_MANAGE_QUEST = "nethical.digipaws.permission.API_V1";
    public static final String COIN_MANAGER_INCREMENT = "increment_coin";
    public static final String COIN_MANAGER_DECREMENT = "decrement_coin";
    public static final String COIN_MANAGER_NOTIF_DESC = "coin_desc";
    
    // specifies how many minutes to waste does 1 digicoin provide
    public static final int ADVENTURE_MODE_COOLDOWN = 20 * 60 * 1000; // 20 minutes
    
    // required to prevent repeated back presses 
    public static final int GLOBAL_ACTION_COOLDOWN = 1500; // 1.5 seconds
    
    //number of days anti-uninstall would run
    public static final int CHALLENGE_TIME = 21; // 3 weeks
    
    
    public static final String KEY_WORKOUT_TYPE = "workout_type";
    
    public static final int FOCUS_MODE_LENGTH = 5400000; // 1 hour in ms
    
    public static final String PROVIDER_AUTHORITY = "nethical.digipaws.questprovider";
    public static final int API_COIN_INC_COOLDOWN = 60 * 60 * 1000; // in ms 
    
    public static final String WEBSITE_ROOT = "https://nethical6.github.io/digipaws/";
}