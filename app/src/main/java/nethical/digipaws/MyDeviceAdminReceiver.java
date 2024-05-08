package nethical.digipaws;
import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class MyDeviceAdminReceiver extends DeviceAdminReceiver {
	
	@Override
	public void onDisabled(Context context, Intent intent) {
		super.onDisabled(context, intent);
		showToast(context.getApplicationContext(), "Uninstallation attempt detected");
	}
	
	private void showToast(final Context context, final String message) {
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
			}
		});
	}
}