package nethical.digipaws.utils;

public class DigiConstants {
	
	public static final int RADAR_RADIUS = 200; // in meters
	
	public static final String PREF_PACKAGES_FILE = "package_names";
	public static final String PREF_PACKAGES_KEY = "packages";
	
	public static final String PREF_VIEWBLOCKER_CONFIG_FILE = "view_blocker_config";
	public static final String PREF_VIEWBLOCKER_SHORTS_KEY = "isShortsBlocked";
	public static final String PREF_VIEWBLOCKER_ENGAGEMENT_KEY = "isEngagementBlocked";
	
	public static final String PREF_PUNISHMENT_FILE = "punishment_info";
	public static final String PREF_PUNISHMENT_DIFFICULTY_KEY = "difficulty";
	public static final int DIFFICULTY_LEVEL_EASY = 0;
	public static final int DIFFICULTY_LEVEL_NORMAL = 1;
	public static final int DIFFICULTY_LEVEL_EXTREME = 1;
	
	public static final String VIEWID_SEPERATOR = ":id/";
	
	private static final String YOUTUBE_PACKAGE_NAME = "com.google.android.youtube";
	public static final String YOUTUBE_SHORTS_VIEWID = YOUTUBE_PACKAGE_NAME + VIEWID_SEPERATOR + "reel_recycler";
	public static final String YOUTUBE_ENGAGEMENT_VIEWID = YOUTUBE_PACKAGE_NAME + VIEWID_SEPERATOR + "engagement_panel_content"; // Blocks comment and description panel
	
	private static final String INSTAGRAM_PACKAGE_NAME = "com.instagram.android";
	public static final String INSTAGRAM_REELS_VIEWID = INSTAGRAM_PACKAGE_NAME + VIEWID_SEPERATOR + "root_clips_layout";
	public static final String INSTAGRAM_COMMENTS_VIEWID = INSTAGRAM_PACKAGE_NAME + VIEWID_SEPERATOR + "comment_composer_parent";
	
	public static final String PREF_BLOCKER_COOLDOWN_KEY = "cooldown";
	public static final String PREF_BLOCKER_LAST_WARNING_TIME_KEY = "last_warning_time";
	
	public static final String PREF_BLOCKER_DELAY_FILE = "delay_data";
	public static final String PREF_BLOCKER_DELAY_VIEWID_KEY = "current_running_delay_viewid";
	
}