package nethical.digipaws;
import android.app.Application;
import com.google.android.material.color.DynamicColors;

public class DigiApplication extends Application{
    
    
    @Override
    public void onCreate() {
        
        DynamicColors.applyToActivitiesIfAvailable(this);
            // Set the default uncaught exception handler
              
        super.onCreate();
        }
    
    }
    

