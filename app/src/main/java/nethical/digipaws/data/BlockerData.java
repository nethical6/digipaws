package nethical.digipaws.data;

import java.util.HashMap;
import nethical.digipaws.utils.DigiConstants;

public class BlockerData {


    public static final String[] shortsViewIds = {
            "com.google.android.youtube" + DigiConstants.VIEWID_SEPERATOR + "reel_watch_player",
            "app.revanced.android.youtube"+ DigiConstants.VIEWID_SEPERATOR +"reel_recycler",
            "com.instagram.android" + DigiConstants.VIEWID_SEPERATOR + "root_clips_layout"
    };

    public static final HashMap<String, Object> shortsApplications = new HashMap<String, Object>() {{
        put("com.ss.android.ugc", null); // TikTok Asia
        put("com.zhiliaoapp.musically", null); // TikTok Global
        put("com.instagram.lite", null); // Blocked entire app
    }};

    public static final String[] engagementPanelViewIds = {
            "com.google.android.youtube" + DigiConstants.VIEWID_SEPERATOR + "engagement_panel_content",
            "app.revanced.android.youtube"+ DigiConstants.VIEWID_SEPERATOR +"engagement_panel_content",
            "com.instagram.android" + DigiConstants.VIEWID_SEPERATOR + "comment_composer_parent",
            "com.ss.android.ugc" + DigiConstants.VIEWID_SEPERATOR + "c5g", //tiktok asia
            "com.zhiliaoapp.musically" + DigiConstants.VIEWID_SEPERATOR + "c5g" //tiktok global
    };

    public static final String[] rebootViewIds = {
            "oppo" + DigiConstants.VIEWID_SEPERATOR + "oppo_power_shutdown_tv_id",
            "androidhwext"+ DigiConstants.VIEWID_SEPERATOR +"hw_new_global_actions_view"
    };



    public static final HashMap<String, Object> nonBlockedPackages = new HashMap<String, Object>() {{
        put("com.google.android.dialer", null); // Google Dialer
        put("com.google.android.apps.messaging", null);
        put("nethical.digipaws", null); // DigiPaws
        put("com.google.android.deskclock", null); // Google Clock
        put("com.android.systemui", null); // Android System UI
        put("com.android.phone", null); // Android Phone



        // Productive apps
        put("com.google.android.keep", null); // Google Keep
        put("com.microsoft.todos", null); // Microsoft To Do
        put("com.evernote", null); // Evernote
        put("com.todoist", null); // Todoist
        put("com.ticktick.task", null); // TickTick
        put("com.google.android.apps.docs", null); // Google Docs
        put("com.google.android.apps.sheets", null); // Google Sheets
        put("com.google.android.apps.slides", null); // Google Slides
        put("com.google.android.calendar", null); // Google Calendar
        put("com.microsoft.office.officehubrow", null); // Microsoft Office
        put("com.google.android.apps.tasks", null); // Google Tasks
        put("com.asana.app", null); // Asana
        put("com.trello", null); // Trello
        put("com.nutomic.syncthingandroid", null); // Syncthing
        put("com.slack", null); // Slack
        put("com.google.android.gm", null); // Gmail
        put("com.pushbullet.android", null); // Pushbullet
        put("com.teams.microsoft", null); // Microsoft Teams
        put("com.dropbox.android", null); // Dropbox
        put("com.box.android", null); // Box
        put("com.google.android.apps.meetings", null); // Google Meet
        put("com.zoho.crm", null); // Zoho CRM
        put("com.anydo", null); // Any.do
        put("com.notion.id", null); // Notion
        put("com.mindjet.mindmanager", null); // MindManager
        put("com.miro.miro", null); // Miro
        put("com.moodle.moodlemobile", null); // Moodle
        put("com.zoom.us", null); // Zoom
        put("com.basecamp.bc3", null); // Basecamp
        put("com.campmobile.snow", null); // Snow
        put("com.gs.android", null); // GTasks
        put("com.habitrpg.android.habitica", null); // Habitica
        put("com.ticktick.task", null); // TickTick
    }};

    public static final HashMap<String, Object> pornKeywords = new HashMap<String, Object>() {{
        put("adult", null);
        put("porn", null);
        put("sex", null);
        put("xxx", null);
        put("nude", null);
        put("naked", null);
        put("erotic", null);
        put("lust", null);
        put("sensual", null);
        put("intimate", null);
        put("sexy", null);
        put("breast", null);
        put("boobs", null);
        put("vagina", null);
        put("penis", null);
        put("dick", null);
        put("cock", null);
        put("pussy", null);
        put("fuck", null);
        put("ass", null);
        put("butt", null);
        put("orgasm", null);
        put("masturbation", null);
        put("anal", null);
        put("bondage", null);
        put("fetish", null);
        put("kinky", null);
        put("swinger", null);
        put("orgy", null);
        put("cum", null);
        put("ejaculate", null);
        put("sperm", null);
        put("prostitute", null);
        put("escort", null);
        put("stripper", null);
        put("nudity", null);
        put("sexual", null);
        put("erogenous", null);
        put("seduce", null);
        put("seduction", null);
        put("lustful", null);
        put("erect", null);
        put("threesome", null);
        put("gangbang", null);
        put("hooker", null);
        put("milf", null);
        put("cougar", null);
        put("hardcore", null);
        put("softcore", null);
        put("pleasure", null);
        put("desire", null);
        put("intimacy", null);
        put("romance", null);
        put("love", null);
        put("bedroom", null);
        put("bed", null);
        put("undress", null);
        put("bikini", null);
        put("lingerie", null);
        put("condom", null);
        put("blowjob", null);
        put("cunnilingus", null);
        put("fellatio", null);
        put("handjob", null);
        put("rimming", null);
        put("strap-on", null);
        put("vibrator", null);
        put("dildo", null);
        put("playboy", null);
        put("playgirl", null);
        put("playmate", null);
        put("topless", null);
        put("bottomless", null);
        put("horny", null);
        put("naughty", null);
        put("dirty", null);
        put("kiss", null);
        put("foreplay", null);
        put("suck", null);
        put("lick", null);
        put("swallow", null);
        put("squirt", null);
        put("tits", null);
        put("clitoris", null);
        put("labia", null);
        put("scrotum", null);
        put("testicles", null);
        put("wet", null);
        put("moist", null);
        put("ejaculation", null);
        put("stimulate", null);
        put("stimulation", null);
        put("erectile", null);
        put("orgasmic", null);
        put("libido", null);
        put("pubic", null);
        put("genital", null);
        put("sexualized", null);
        put("lewd", null);
        put("obscene", null);
        put("pervert", null);
        put("orgiastic", null);
        put("busty", null);
        put("groping", null);
        put("kink", null);
        put("peep", null);
        put("voyeur", null);
        put("underwear", null);
        put("arousal", null);
        put("nipple", null);
        put("masturbate", null);
        put("prostitution", null);
        put("sodomy", null);
        put("incest", null);
        put("bareback", null);
        put("bestiality", null);
        put("exhibitionism", null);
        put("fornication", null);
        put("obscenity", null);
        put("promiscuous", null);
        put("rapist", null);
        put("sadism", null);
        put("masochism", null);
        put("swinging", null);
        put("unprotected", null);
    }};
}

