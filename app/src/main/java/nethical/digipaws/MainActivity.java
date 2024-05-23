package nethical.digipaws;

import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.util.List;
import nethical.digipaws.fragments.HomeFragment;
import nethical.digipaws.fragments.dialogs.LoadingDialog;
import nethical.digipaws.fragments.dialogs.SelectQuestDialog;
import nethical.digipaws.fragments.quests.MarathonQuest;
import nethical.digipaws.R;
import nethical.digipaws.utils.DigiUtils;
import nethical.digipaws.utils.LoadAppList;
import nethical.digipaws.utils.OverlayManager;
import nethical.digipaws.utils.SurvivalModeManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		
        SurvivalModeManager.enableSurvivalMode(this);
        
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.fragment_container, new HomeFragment());
		//transaction.addToBackStack(null);
		transaction.commit();
		
		
		
    }
	
	
	public void SelectQuest(View view){
		SelectQuestDialog dialog = new SelectQuestDialog();
		dialog.show(getSupportFragmentManager(), "select_quest"); // Use a unique tag for the dialog
		
	}
}