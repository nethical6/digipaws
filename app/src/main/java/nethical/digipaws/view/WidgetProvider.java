package nethical.digipaws.view;

import android.app.LauncherActivity;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.app.PendingIntent;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.TextView;
import androidx.constraintlayout.solver.state.helpers.BarrierReference;
import nethical.digipaws.R;
import nethical.digipaws.utils.SurvivalModeConfig;

// shit not working idk why
public class WidgetProvider extends AppWidgetProvider {

  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    int appWidgetId = appWidgetIds[0];
    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

    if (SurvivalModeConfig.isEnabled(context)) {
      String survivalModeEndTime = SurvivalModeConfig.getUnlockTime(context);

      views.setTextViewText(
          R.id.stats_info,
          "Device locked until "
              + survivalModeEndTime
              + " hours. Attempt a quest to unlock device now!");
    } else {
      views.setTextViewText(R.id.stats_info, generateStats(context));
    }
    appWidgetManager.updateAppWidget(appWidgetId, views);
  }

  private String generateStats(Context context) {
    SharedPreferences preferences = context.getSharedPreferences("stats", Context.MODE_PRIVATE);

    int streak = preferences.getInt("streak", 0);
    int viewBlocked = preferences.getInt("viewblock_count", 0);
    int keywordBlocked = preferences.getInt("keywordblock_count", 0);

    String formattedValue =
        ("STREAK: "
            + String.valueOf(streak)
            + "\n"
            + "Blocked Content: "
            + String.valueOf(viewBlocked)
            + "\n"
            + "Blocked Keyword: "
            + String.valueOf(viewBlocked)
            + "\n");

    return formattedValue;
  }
}
