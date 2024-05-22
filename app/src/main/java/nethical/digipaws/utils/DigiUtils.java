package nethical.digipaws.utils;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.pm.ServiceInfo;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import java.util.List;
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
        if(DelayManager.isGlobalActionCooldownActive(service)==false){
            DelayManager.updateGlobalActionDelay(service);
       	service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
            
        }
	}
    
    public static void pressHome(AccessibilityService service){
        if(DelayManager.isGlobalActionCooldownActive(service)==false){
            DelayManager.updateGlobalActionDelay(service);
       	service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
            
        }
	}
    
}