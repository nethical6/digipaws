package nethical.digipaws.fragments.intro;

import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.heinrichreimersoftware.materialintro.app.SlideFragment;

import java.util.ArrayList;
import java.util.List;

import nethical.digipaws.R;
import nethical.digipaws.adapters.SelectBlockedAppsAdapter;
import nethical.digipaws.data.AppData;
import nethical.digipaws.fragments.dialogs.LoadingDialog;
import nethical.digipaws.utils.LoadAppList;

public class ChooseBlockedApps extends SlideFragment {

    private final SharedPreferences userConfigs;
    private RecyclerView recyclerView;
    private HandlerThread handlerThread;
    private SelectBlockedAppsAdapter adapter;
    private final boolean canGoAhead = false;

    public ChooseBlockedApps(SharedPreferences sp) {
        userConfigs = sp;
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Quit the HandlerThread to free up resources
        handlerThread.quitSafely();
    }


    public void loadAppsAndDisplay() {

        handlerThread = new HandlerThread("AppListLoader");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());

        // Create a Handler for the main thread
        Handler mainHandler = new Handler(Looper.getMainLooper());

        handler.postDelayed(
                new Runnable() {

                    @Override
                    public void run() {

                        LoadingDialog loadingDialog = new LoadingDialog("Fetching Packages");

                        loadingDialog.show(requireActivity().getSupportFragmentManager(), "loading_dialog");

                        adapter = new SelectBlockedAppsAdapter(requireContext(), userConfigs);

                        final PackageManager pm = requireContext().getPackageManager();
                        List<ApplicationInfo> packages = pm.getInstalledApplications(0);

                        List<AppData> appData = new ArrayList<>();
                        for (ApplicationInfo info : packages) {
                            if (LoadAppList.isSystemPackage(info)) {
                                continue;
                            }
                            try {
                                appData.add(
                                        new AppData(
                                                info.loadLabel(pm).toString(),
                                                pm.getApplicationIcon(info.packageName), info.packageName));
                            } catch (PackageManager.NameNotFoundException ignored) {
                            }
                        }

                        mainHandler.post(
                                () -> {
                                    try {
                                        recyclerView.setLayoutManager(
                                                new LinearLayoutManager(requireContext()));
                                        adapter.setData(appData);
                                        recyclerView.setAdapter(adapter);
                                        loadingDialog.dismiss();
                                    } catch (Exception ignored) {

                                    }
                                });
                    }
                }, 200);

    }

}
