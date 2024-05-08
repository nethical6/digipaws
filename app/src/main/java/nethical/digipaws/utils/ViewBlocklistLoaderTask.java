package nethical.digipaws.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import java.util.concurrent.atomic.AtomicInteger;
import nethical.digipaws.Constants;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ViewBlocklistLoaderTask {

    private static final String BASE_URL = Constants.BASE_URL;
    private OkHttpClient client;
    private Context context;

    public ViewBlocklistLoaderTask(Context context) {
        this.context = context.getApplicationContext();
        this.client = new OkHttpClient();
    }

    public void startSearch(String[] packageList, Runnable onAllCompleted) {
    if (packageList == null || packageList.length == 0) {
        ListApps.updateCache(context);
        packageList = ListApps.getPackageNames(context);
    }

    int requestCount = packageList.length;
    AtomicInteger completedCount = new AtomicInteger(0);

    for (String packageName : packageList) {
        String url = BASE_URL + "viewBlockers/" + packageName;
        enqueueRequest(url, packageName, () -> {
            if (completedCount.incrementAndGet() == requestCount && onAllCompleted != null) {
                // All requests have completed, invoke the callback
                onAllCompleted.run();
            }
        });
    }
}


    private void enqueueRequest(String url, String packageName, Runnable onComplete) {
    Request request = new Request.Builder()
            .url(url)
            .build();

    client.newCall(request).enqueue(new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            e.printStackTrace();
            // Handle failure if needed
            if (onComplete != null) {
                onComplete.run(); // Ensure onComplete is still called on failure
            }
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response.isSuccessful()) {
                String responseData = response.body().string();
                saveDataToPreferences(packageName, responseData);
            } else {
                // Handle unsuccessful response
                // showToast("Failed to fetch data for " + packageName);
            }
            // Invoke onComplete when request is completed
            if (onComplete != null) {
                onComplete.run();
            }
        }
    });
}


    private void saveDataToPreferences(String packageName, String data) {
		SharedPreferences preferences =
        PreferenceManager.getDefaultSharedPreferences(context);

        	if(preferences.contains(packageName)){
			return;
		}
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(packageName, data);
        editor.apply();
    }

    private void showToast(String message) {
        // Display a toast message in the UI thread
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
	
	public interface OnAllCompleted {
    void onCompleted();
}

}
