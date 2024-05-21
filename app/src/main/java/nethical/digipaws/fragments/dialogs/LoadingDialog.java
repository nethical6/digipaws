package nethical.digipaws.fragments.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;
import nethical.digipaws.R;

public class LoadingDialog extends DialogFragment {
	
	private String title = "Loading...";
	
	public LoadingDialog(String title){
		this.title=title;
	}
	
	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext())
		.setTitle(title)
		.setCancelable(false);
		
		View view = LayoutInflater.from(requireContext()).inflate(R.layout.loading_dialog, null);
		builder.setView(view);
		
		return builder.create();
	}
	
	@Override
	public void onStart() {
		super.onStart();
	}
	
}
