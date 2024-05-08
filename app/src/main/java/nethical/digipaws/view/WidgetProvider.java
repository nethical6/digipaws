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

public class WidgetProvider extends AppWidgetProvider {

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Perform this loop procedure for each widget that belongs to this
        // provider.
        int appWidgetId = appWidgetIds[0];
            // Create an Intent to launch ExampleActivity
            

            // Get the layout for the widget and attach an onClick listener to
            // the button.
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            
            if (SurvivalModeConfig.isEnabled(context)) {
                String survivalModeEndTime = SurvivalModeConfig.getUnlockTime(context);
                
                views.setTextViewText(R.id.stats_info,"Device locked until "+survivalModeEndTime+" hours. Attempt a quest to unlock device now!");
		}else{
            views.setTextViewText(R.id.stats_info,generateStats(context));
        }
            
            // Tell the AppWidgetManager to perform an update on the current app
            // widget.
            appWidgetManager.updateAppWidget(appWidgetId, views);
        
    }
    
    	private String generateStats(Context context){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

		int streak = preferences.getInt("streak",0);
		int viewBlocked = preferences.getInt("viewblock_count",0);
		int keywordBlocked = preferences.getInt("keywordblock_count",0);
		
		String formattedValue =( "STREAK: "+ String.valueOf(streak) + "\n" +
								"Blocked Content: " + String.valueOf(viewBlocked) + "\n" +
								"Blocked Keyword: " + String.valueOf(viewBlocked) + "\n" );
								
		return formattedValue;
	}
}