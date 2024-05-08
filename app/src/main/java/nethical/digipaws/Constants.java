package nethical.digipaws;

public class Constants {
  public static final String PORN_PREF = "porn_blocked";
  public static final String SETTINGS_PREF = "block_settings";
  public static final String NOTIF_PREF = "block_notification";
  public static final String LAUNCHER_PREF = "launcher_enabled";

  public static final String PREF_COOLDOWN_TIMESTAMP = "cooldown_timestamp";

  public static final String BASE_URL =
      "https://raw.githubusercontent.com/RealNethical/digi-web/main/";

  public static final int RESULT_ADMIN_PERM = 690;

  public static final int COOLDOWN_DELAY = 1000; // cooldown delay between blockers

  public static final int BLOCK_TYPE_VIEW = 0; // cooldown delay for blockers
  public static final int BLOCK_TYPE_KEYWORD = 1; // cooldown delay for blockers

  public static final String NOTIFICATION_CHANNEL = "blockers";
}
