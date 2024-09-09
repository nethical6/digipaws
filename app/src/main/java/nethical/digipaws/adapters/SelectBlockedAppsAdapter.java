package nethical.digipaws.adapters;

import android.content.Context;
import android.content.SharedPreferences;
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

public class SelectBlockedAppsAdapter extends RecyclerView.Adapter<SelectBlockedAppsAdapter.ViewHolder> {


    private Context context;
    private List<AppData> appData;
    private final SharedPreferences userConfigs;

    private final Set<String> newBlockedAppsSet;

    
	public SelectBlockedAppsAdapter(Context context, SharedPreferences userConfigs) {
        this.context = context;
        this.userConfigs = userConfigs;
        newBlockedAppsSet = userConfigs.getStringSet(DigiConstants.PREF_BLOCKED_APPS_LIST_KEY, new HashSet<>());

    }
    public void setData(List<AppData> appDataL){
        appData = appDataL;
    }
	
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.choose_blocked_apps_list_item, parent, false);
		context=parent.getContext();
		return new ViewHolder(view);
	}
	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        
        holder.textView.setText(appData.get(position).getLabel());
        holder.icon.setImageDrawable(appData.get(position).getIcon());

        if(newBlockedAppsSet.contains(appData.get(position).getPackageName())){
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
                        newBlockedAppsSet.add(appData.get(position).getPackageName());
                        userConfigs.edit().putStringSet(DigiConstants.PREF_BLOCKED_APPS_LIST_KEY, newBlockedAppsSet).apply();
                    }else{
                        newBlockedAppsSet.remove(appData.get(position).getPackageName());
                    }
           }
        });
		}
		
	@Override
	public int getItemCount() {
		return appData.size();
	}
    
    public Set<String> getSelectedAppList(){
        return this.newBlockedAppsSet;
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
}