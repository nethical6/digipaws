package nethical.digipaws.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import nethical.digipaws.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import nethical.digipaws.fragments.quests.MarathonQuest;
import nethical.digipaws.utils.DigiUtils;
import nethical.workout.WorkoutActivity;

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
				case(1):
				    Intent intent = new Intent(context,WorkoutActivity.class);
                    context.startActivity(intent);
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