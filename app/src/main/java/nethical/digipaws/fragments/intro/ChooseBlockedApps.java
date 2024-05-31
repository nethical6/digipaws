package nethical.digipaws.fragments.intro;

import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.os.Looper;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.github.appintro.SlidePolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import nethical.digipaws.R;
import nethical.digipaws.adapters.SelectBlockedAppsAdapter;
import nethical.digipaws.data.AppData;
import nethical.digipaws.fragments.dialogs.LoadingDialog;
import nethical.digipaws.utils.DigiConstants;
import nethical.digipaws.utils.LoadAppList;

public class ChooseBlockedApps extends Fragment implements SlidePolicy {

    private SharedPreferences sharedPreferences;
    private RecyclerView recyclerView;
    private HandlerThread handlerThread;
    private SelectBlockedAppsAdapter adapter;
    
    public ChooseBlockedApps(SharedPreferences sp) {
        sharedPreferences = sp;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.choose_blocked_apps, container, false);
        recyclerView = view.findViewById(R.id.app_list);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LoadingDialog loadingDialog = new LoadingDialog("Fetching Packages");
        loadingDialog.show(getActivity().getSupportFragmentManager(), "loading_dialog");

        adapter = new SelectBlockedAppsAdapter(requireContext());
        
        handlerThread = new HandlerThread("AppListLoader");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());

        // Create a Handler for the main thread
        Handler mainHandler = new Handler(Looper.getMainLooper());
        
        handler.post(
                new Runnable() {
                    @Override
                    public void run() {
                        List<String> appList = LoadAppList.getPackageNames(requireContext());
                        List<AppData> appData = new ArrayList<AppData>();

                        PackageManager pm =
                                requireContext()
                                        .getPackageManager(); // Replace 'context' with your
                                                              // activity or context

                        for (String packageName : appList) {
                            try {
                                ApplicationInfo info = pm.getApplicationInfo(packageName, 0);
                                appData.add(
                                        new AppData(
                                                info.loadLabel(pm).toString(),
                                                pm.getApplicationIcon(packageName),packageName));
                            } catch (PackageManager.NameNotFoundException e) {
                                continue;
                            }
                        }

                        mainHandler.post(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        recyclerView.setLayoutManager(
                                                new LinearLayoutManager(requireContext()));
                                        // Update the UI with the result
                                        adapter.setData(appData);
                                        recyclerView.setAdapter(adapter);
                                        loadingDialog.dismiss();
                                    }
                                });
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Quit the HandlerThread to free up resources
        handlerThread.quitSafely();
    }

    @Override
    public boolean isPolicyRespected() {
       Set<String> newBlockedAppsSet = adapter.getSelectedAppList();
        if(!newBlockedAppsSet.isEmpty()){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putStringSet(DigiConstants.PREF_BLOCKED_APPS_LIST_KEY, newBlockedAppsSet);
            editor.apply();
        }
        return true;
    }

    @Override
    public void onUserIllegallyRequestedNextPage() {
       
    }
}
