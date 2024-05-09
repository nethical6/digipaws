package nethical.digipaws.utils.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import nethical.digipaws.R;
import nethical.digipaws.ui.SettingsFragment;
import nethical.digipaws.utils.FragmentChangeListener;
import nethical.digipaws.utils.KeyboardUtils;
import nethical.digipaws.utils.ListApps;

import java.util.ArrayList;
import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppViewHolder> {

    private List<ListApps.AppModel> appsList;
    private List<ListApps.AppModel> originalList; // To hold the original unfiltered list
    private Context context;
    private FragmentChangeListener fragmentChangeListener;

    public AppAdapter(
            Context context, List<ListApps.AppModel> appsList, FragmentChangeListener listener) {
        this.context = context;
        this.appsList = appsList;
        this.originalList = new ArrayList<>(appsList); // Make a copy of the original list
        this.fragmentChangeListener = listener;
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_app, parent, false);
        return new AppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {
        final ListApps.AppModel appInfo = appsList.get(position);
        holder.appName.setText(appInfo.getAppLabel());

        // Set click listener to open the app or settings activity
        holder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        KeyboardUtils.hideKeyboard(context, v);

                        if ("nethical.digipaws".equals(appInfo.getPackageName())) {
                            openSettingsActivity();
                        } else {
                            openApp(appInfo.getPackageName());
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return appsList.size();
    }

    private void openApp(String packageName) {
        PackageManager pm = context.getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(packageName);
        if (intent != null) {
            context.startActivity(intent);
        } else {
            // App not found or unable to launch
            // You can handle this case as needed
        }
    }

    private void openSettingsActivity() {

        // Create a new instance of the fragment you want to replace with
        Fragment x = new SettingsFragment();
        // Replace the existing fragment in the container view (replace with your container ID)
        if (fragmentChangeListener != null) {
            fragmentChangeListener.onFragmentChange(x);
        }
    }

    public static class AppViewHolder extends RecyclerView.ViewHolder {
        ImageView appIcon;
        TextView appName;

        public AppViewHolder(@NonNull View itemView) {
            super(itemView);

            appName = itemView.findViewById(R.id.app_name);
        }
    }

    public void filterList(List<ListApps.AppModel> filteredList) {
        appsList = filteredList;
        notifyDataSetChanged();
    }

    public void resetList() {
        appsList = new ArrayList<>(originalList);
        notifyDataSetChanged();
    }
}
