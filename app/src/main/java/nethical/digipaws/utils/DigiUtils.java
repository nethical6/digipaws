package nethical.digipaws.utils;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.provider.Settings;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentManager;
import nethical.digipaws.R;
public class DigiUtils {
	
	public static void showToast(Context context,String message){
		Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
	}
	
	public static void replaceScreen(FragmentManager fm,Fragment fragment){
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.replace(R.id.fragment_container, fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	public static void replaceScreenWithoutAddingToBackStack(FragmentManager fm,Fragment fragment){
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.replace(R.id.fragment_container, fragment);
		transaction.commit();
	}
	
	public static boolean isAccessibilityServiceEnabled(Context context, Class<? extends AccessibilityService> service) {
        String prefString = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
        return prefString!= null && prefString.contains("nethical.digipaws/nethical.digipaws.services.BlockerService");
	}
	
    public static void pressBack(AccessibilityService service){
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
	}
    
    public static void pressHome(AccessibilityService service){
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
	}
    
    public static void sendNotification(Context context, String title, String content,int icon) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, DigiConstants.NOTIFICATION_CHANNEL)
                        .setSmallIcon(icon)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(69, builder.build());
    }
    
    
    
}