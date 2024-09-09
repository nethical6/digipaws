package nethical.digipaws.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nethical.digipaws.R;
import nethical.digipaws.data.AppData;
import nethical.digipaws.utils.DigiConstants;

public class SelectWhiteListedAppsAdapter extends RecyclerView.Adapter<SelectWhiteListedAppsAdapter.ViewHolder> {


    private Context context;
    private List<AppData> appData;
    private final SharedPreferences userConfigs;

    private final Set<String> newwhitelistAppsSet;


	public SelectWhiteListedAppsAdapter(Context context, SharedPreferences userConfigs) {
        this.context = context;
        this.userConfigs = userConfigs;
        newwhitelistAppsSet = userConfigs.getStringSet(DigiConstants.PREF_WHITELISTED_APPS_LIST_KEY, new HashSet<>());

    }
    public void setData(List<AppData> appDataL){
        appData = appDataL;
    }
	
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.choose_blocked_apps_list_item, parent, false);
		context=parent.getContext();
        newwhitelistAppsSet.add(getLauncherPackageName());
		return new ViewHolder(view);
	}
	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        
        holder.textView.setText(appData.get(position).getLabel());
        holder.icon.setImageDrawable(appData.get(position).getIcon());
        if(newwhitelistAppsSet.contains(appData.get(position).getPackageName())){
            holder.cb.setChecked(true);
        } else {
            holder.cb.setChecked(appData.get(position).getChecked());
        }
        
        holder.itemView.setOnClickListener(null);
        holder.cb.setOnCheckedChangeListener(null);

        holder.itemView.setOnClickListener((view)-> holder.cb.setChecked(!holder.cb.isChecked()));
        
        // Set new listener
        holder.cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                appData.get(adapterPosition).setIsChecked(isChecked);
                    if(isChecked){
                        newwhitelistAppsSet.add(appData.get(position).getPackageName());
                        userConfigs.edit().putStringSet(DigiConstants.PREF_WHITELISTED_APPS_LIST_KEY, newwhitelistAppsSet).apply();
                    }else{
                        newwhitelistAppsSet.remove(appData.get(position).getPackageName());
                        newwhitelistAppsSet.remove(appData.get(position).getPackageName());
                    }
           }
        });
		}
		
	@Override
	public int getItemCount() {
		return appData.size();
	}
    
    public Set<String> getSelectedAppList(){
        return this.newwhitelistAppsSet;
    }
	
	
	public static class ViewHolder extends RecyclerView.ViewHolder {
		TextView textView;
		ImageView icon;
        CheckBox cb;
        
		public ViewHolder(View itemView) {
			super(itemView);
			textView = itemView.findViewById(R.id.app_name);
            icon = itemView.findViewById(R.id.app_icon);
            cb = itemView.findViewById(R.id.app_cb);
    	}
	}
    private String getLauncherPackageName() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        PackageManager pm = context.getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);

        if (resolveInfo != null && resolveInfo.activityInfo != null) {
            return resolveInfo.activityInfo.packageName;
        }
        return null;
    }
}