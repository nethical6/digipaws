package nethical.digipaws;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;
import com.google.android.material.color.DynamicColors;

public class DigiApplication extends Application{
    
    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;
    private static Context mApplicationContext;
    public static Context getContext() {
        return mApplicationContext;
    }
    @Override
    public void onCreate() {
        mApplicationContext = getApplicationContext();
      
        DynamicColors.applyToActivitiesIfAvailable(this);
            // Set the default uncaught exception handler
              this.uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler(
            new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread thread, Throwable throwable) {
                    Intent intent = new Intent(getApplicationContext(), DebugActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("error", Log.getStackTraceString(throwable));

                    PendingIntent pendingIntent =
                        PendingIntent.getActivity(
                            getApplicationContext(),
                            11111,
                            intent,
                            PendingIntent.FLAG_ONE_SHOT
                        );

                    AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 1000, pendingIntent);

                    SketchLogger.broadcastLog(Log.getStackTraceString(throwable));
                    Process.killProcess(Process.myPid());
                    System.exit(1);

                    uncaughtExceptionHandler.uncaughtException(thread, throwable);
                }
            });
        SketchLogger.startLogging();
        super.onCreate();
        }
    
    }
    

