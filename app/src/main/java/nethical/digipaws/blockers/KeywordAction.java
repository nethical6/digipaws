package nethical.digipaws.blockers;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;
import androidx.preference.PreferenceManager;
import java.util.Arrays;
import java.util.List;
import nethical.digipaws.Constants;
import nethical.digipaws.utils.DigiUtils;
import nethical.digipaws.utils.data.SurvivalModeData;
import java.util.regex.Pattern;
import nethical.digipaws.utils.SurvivalModeConfig;

public class KeywordAction{

  private static AccessibilityNodeInfo focusedEditText;

//todo: load it from a file instead
  private static final List<String> KEYWORDS =
      Arrays.asList(
          "adult",
          "porn",
          "sex",
          "xxx",
          "nude",
          "naked",
          "erotic",
          "lust",
          "sensual",
          "intimate",
          "sexy",
          "breast",
          "boobs",
          "vagina",
          "penis",
          "dick",
          "cock",
          "pussy",
          "fuck",
          "ass",
          "butt",
          "orgasm",
          "masturbation",
          "anal",
          "bondage",
          "fetish",
          "kinky",
          "swinger",
          "orgy",
          "cum",
          "ejaculate",
          "sperm",
          "prostitute",
          "escort",
          "stripper",
          "nudity",
          "sexual",
          "erogenous",
          "seduce",
          "seduction",
          "lustful",
          "erect",
          "threesome",
          "gangbang",
          "hooker",
          "milf",
          "cougar",
          "hardcore",
          "softcore",
          "pleasure",
          "desire",
          "intimacy",
          "romance",
          "love",
          "bedroom",
          "bed",
          "undress",
          "bikini",
          "lingerie",
          "condom",
          "blowjob",
          "cunnilingus",
          "fellatio",
          "handjob",
          "rimming",
          "strap-on",
          "vibrator",
          "dildo",
          "playboy",
          "playgirl",
          "playmate",
          "topless",
          "bottomless",
          "horny",
          "naughty",
          "dirty",
          "kiss",
          "foreplay",
          "suck",
          "lick",
          "swallow",
          "squirt",
          "tits",
          "clitoris",
          "labia",
          "scrotum",
          "testicles",
          "wet",
          "moist",
          "ejaculation",
          "stimulate",
          "stimulation",
          "erectile",
          "orgasmic",
          "libido",
          "pubic",
          "genital",
          "sexualized",
          "lewd",
          "obscene",
          "pervert",
          "orgiastic",
          "busty",
          "groping",
          "kink",
          "peep",
          "voyeur",
          "underwear",
          "arousal",
          "nipple",
          "masturbate",
          "prostitution",
          "sodomy",
          "incest",
          "bareback",
          "bestiality",
          "exhibitionism",
          "fornication",
          "obscenity",
          "promiscuous",
          "rapist",
          "sadism",
          "masochism",
          "swinging",
          "unprotected");
  private static long lastBackPressTime = 0;
  private static AccessibilityService servicex;
  private static SharedPreferences preferences;
  private static Context context;
  
  public static void performAction(AccessibilityService service, AccessibilityEvent event) {
    preferences = PreferenceManager.getDefaultSharedPreferences(service);
	context=service;
    if (preferences == null) {
      preferences = PreferenceManager.getDefaultSharedPreferences(service);
    }
    if (DigiUtils.isCooldownActive(service)) {
      return; // Ignore action if cooldown is active
    }

  
    AccessibilityNodeInfo rootNode = service.getRootInActiveWindow();
    if (rootNode != null) {
      traverseNodesForKeywords(rootNode);
    }
  }

  private static void traverseNodesForKeywords(AccessibilityNodeInfo node) {
    if (node == null) {
      return;
    }

    if (node.getClassName() != null && node.getClassName().equals("android.widget.EditText")) {
      CharSequence nodeText = node.getText();
      if (nodeText != null) {
        focusedEditText = node;
        String editTextContent = nodeText.toString().toLowerCase();
        if (containsKeyword(editTextContent)) {
         // DefaultAction.showToast(servicex, "Keyword found in EditText: ");
          DigiUtils.sendNotification(context,"Keword Blocked","Survival Mode Enabled");         
                    
		  SurvivalModeConfig.start(context,new SurvivalModeData(true,SurvivalModeConfig.generateEndTime(servicex,0,30),"null","null"));
		  DigiUtils.updateStatCount(context,Constants.BLOCK_TYPE_KEYWORD);
		
		servicex.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
          
        }
      }
    }

    for (int i = 0; i < node.getChildCount(); i++) {
      AccessibilityNodeInfo childNode = node.getChild(i);
      traverseNodesForKeywords(childNode);
    }
  }



  private static boolean containsKeyword(String text) {
    String lowerCaseText = text.toLowerCase().trim();

    for (String keyword : KEYWORDS) {
      String trimmedKeyword = keyword.trim().toLowerCase();
      if (lowerCaseText.matches(".*\\b" + Pattern.quote(trimmedKeyword) + "\\b.*")) {
        return true;
      }
    }
    return false;
  }
}
