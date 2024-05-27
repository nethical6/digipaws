package nethical.digipaws.utils;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.accessibilityservice.AccessibilityService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import java.util.List;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentManager;
import nethical.digipaws.MainActivity;
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
	
	public static boolean isAccessibilityServiceEnabled(Context context, Class<? extends AccessibilityService> service) {
		AccessibilityManager am = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
		List<AccessibilityServiceInfo> enabledServices = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK);
		
		for (AccessibilityServiceInfo enabledService : enabledServices) {
			ServiceInfo enabledServiceInfo = enabledService.getResolveInfo().serviceInfo;
			if (enabledServiceInfo.packageName.equals(context.getPackageName()) && enabledServiceInfo.name.equals(service.getName()))
			return true;
		}
		
		return false;
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