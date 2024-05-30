package nethical.digipaws.adapters;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import nethical.digipaws.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import nethical.digipaws.data.AppData;
import nethical.digipaws.fragments.quests.MarathonQuest;
import nethical.digipaws.utils.DigiUtils;

public class SelectBlockedAppsAdapter extends RecyclerView.Adapter<SelectBlockedAppsAdapter.ViewHolder> {
	
	
	private final List<AppData> appData;
	private Context context;
	private List<Drawable> iconList;
    private PackageManager pm;
    
    private Set<String> newBlockedAppsSet = new HashSet<>();
    
    
	public SelectBlockedAppsAdapter(List<AppData> appData,Context context) {
		this.appData = appData;
        this.context = context;
        
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
        holder.cb.setChecked(appData.get(position).getChecked());
        
        holder.cb.setOnCheckedChangeListener(null);

        // Set new listener
        holder.cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                appData.get(adapterPosition).setIsChecked(isChecked);
               // notifyItemChanged(adapterPosition);
            }
        });
		}
		
	@Override
	public int getItemCount() {
		return appData.size();
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