package nethical.digipaws.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import nethical.digipaws.MainActivity;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            // Reset the alarm here
            MainActivity.setDailyAlarm(context);
        }
    }
}
