package nethical.digipaws.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import nethical.digipaws.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import nethical.digipaws.fragments.quests.FocusQuest;
import nethical.digipaws.utils.DigiConstants;
import nethical.digipaws.utils.DigiUtils;
import nethical.digipaws.utils.InstalledQuestsManager;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class SelectQuestAdapter extends RecyclerView.Adapter<SelectQuestAdapter.ViewHolder> {
	
	
	private final String[] listItems;
	private Context context;
	private DialogFragment dialog;
    private InstalledQuestsManager iqm;
    
	public SelectQuestAdapter(String[] listItems,DialogFragment dialog) {
		this.context = dialog.requireContext();
        iqm = new InstalledQuestsManager(context);
      //  iqm.append("nethical.digipaws.api");
        List<String> apiUsers = iqm.getList();
        List<String> tempList = new ArrayList<>(Arrays.asList(listItems));
        tempList.addAll(apiUsers);
        this.listItems = tempList.toArray(new String[0]);
		this.dialog=dialog;
	}
	
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_quest_list_item, parent, false);
		context=parent.getContext();
		return new ViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		PackageManager packageManager = context.getPackageManager();
        if(position>1){
           try {
            ApplicationInfo appInfo = packageManager.getApplicationInfo(listItems[position], 0);
            holder.textView.setText((String) packageManager.getApplicationLabel(appInfo));
        } catch (PackageManager.NameNotFoundException e) {
            holder.textView.setText(listItems[position]);
            iqm.remove(listItems[position]);    
        }
        } else {
            holder.textView.setText(listItems[position]);
        }
        
		holder.textView.setOnClickListener(v -> {
			switch(position){
                case(0): //focus mode
                    
                   DigiUtils.replaceScreen(dialog.getParentFragmentManager(),new FocusQuest());
                    
				     dialog.dismiss();
				    break;
                    
                case(1):
                    Intent intent3 = new Intent(Intent.ACTION_VIEW, Uri.parse(DigiConstants.WEBSITE_ROOT + "partners"));
                    context.startActivity(intent3);
                    break;
                default:
                    Intent intent4 = packageManager.getLaunchIntentForPackage(listItems[position]);
                    if (intent4 != null) {
                        intent4.addCategory(Intent.CATEGORY_LAUNCHER);
                        intent4.putExtra(DigiConstants.KEY_START_QUEST,true);
                         intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent4);
                    } else {
                        iqm.remove(listItems[position]);
                        Toast.makeText(context, "App not found", Toast.LENGTH_SHORT).show();
                    }    
			}
		});
		
		
	}
	
	@Override
	public int getItemCount() {
		return listItems.length;
	}
	
	
	public static class ViewHolder extends RecyclerView.ViewHolder {
		TextView textView;
		
		public ViewHolder(View itemView) {
			super(itemView);
			textView = itemView.findViewById(R.id.text_view_item);
		}
	}
}