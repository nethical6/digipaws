package nethical.digipaws.fragments.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;
import nethical.digipaws.R;
import nethical.digipaws.adapters.SelectQuestAdapter;
public class SelectQuestDialog extends DialogFragment {
	
	private RecyclerView recyclerView;
	
	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext())
		.setTitle("Select a quest");
		
		View view = LayoutInflater.from(requireContext()).inflate(R.layout.select_quest_dialog, null);
		recyclerView = view.findViewById(R.id.list_items);
		recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
		builder.setView(view);
		
		return builder.create();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		String[] listItems = getResources().getStringArray(R.array.Quests); 
		
		recyclerView.setAdapter(new SelectQuestAdapter(listItems,this));
		
		}
		
		}
		