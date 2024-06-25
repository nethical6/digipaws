package nethical.digipaws.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import com.google.mlkit.vision.demo.java.posedetector.classification.PoseClassifierProcessor;
import nethical.digipaws.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import nethical.digipaws.fragments.quests.FocusQuest;
import nethical.digipaws.fragments.quests.MarathonQuest;
import nethical.digipaws.utils.DigiConstants;
import nethical.digipaws.utils.DigiUtils;
import nethical.digipaws.WorkoutActivity;

public class SelectQuestAdapter extends RecyclerView.Adapter<SelectQuestAdapter.ViewHolder> {
	
	
	private final String[] listItems;
	private Context context;
	private DialogFragment dialog;
	public SelectQuestAdapter(String[] listItems,DialogFragment dialog) {
		this.listItems = listItems;
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
		holder.textView.setText(listItems[position]);
		
		holder.textView.setOnClickListener(v -> {
			switch(position){
				case(0)://Touch Grass Quest
				    DigiUtils.replaceScreen(((AppCompatActivity)context).getSupportFragmentManager(),new MarathonQuest());
				    dialog.dismiss();
				    break;
				case(1): // squats
				    Intent intent = new Intent(context,WorkoutActivity.class);
                    intent.putExtra(DigiConstants.KEY_WORKOUT_TYPE,PoseClassifierProcessor.SQUATS_CLASS);
                    context.startActivity(intent);
                    break;
                case(2): // pushups
				    Intent intent2 = new Intent(context,WorkoutActivity.class);
                    intent2.putExtra(DigiConstants.KEY_WORKOUT_TYPE,PoseClassifierProcessor.PUSHUPS_CLASS);
                    context.startActivity(intent2);    
				    break;
                case(3): //focus mode
				    DigiUtils.replaceScreen(((AppCompatActivity)context).getSupportFragmentManager(),new FocusQuest());
				    dialog.dismiss();
				    break;
                case(4):
                    Intent intent3 = new Intent(Intent.ACTION_VIEW, Uri.parse(DigiConstants.WEBSITE_ROOT + "partners"));
                    context.startActivity(intent3);
                    break;
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