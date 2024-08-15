package nethical.digipaws.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import nethical.digipaws.MainActivity;
import nethical.digipaws.R;
import android.view.View;
import android.view.WindowManager;
import android.view.LayoutInflater;
import nethical.digipaws.data.ServiceData;
import nethical.digipaws.services.FocusModeTimerService;

public class OverlayManager {
	
	private WindowManager windowManager;
	private Context context;
	private View overlayView;

    private Button closeButton;
    private Button proceedButton;
    private TextView textTitle;
    private TextView textDescription;

    private WindowManager.LayoutParams params;
    
    // somehow implement material3 design here
	public OverlayManager(ServiceData data){
		this.context = data.getService();
		windowManager = data.getWindowManager();
        init();
	}
    
    public void init(){
        LayoutInflater inflater = LayoutInflater.from(context);
		overlayView = inflater.inflate(R.layout.warning_overlay_default, null);
        closeButton = overlayView.findViewById(R.id.close_overlay);
        proceedButton = overlayView.findViewById(R.id.proceed_overlay);
        textTitle = overlayView.findViewById(R.id.text_title);
        textDescription = overlayView.findViewById(R.id.text_desc);
        
        params = new WindowManager.LayoutParams(
		WindowManager.LayoutParams.MATCH_PARENT,
		WindowManager.LayoutParams.MATCH_PARENT,
		WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
		WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
		PixelFormat.OPAQUE);
        
    }
    
	
    public void showOverlay(int difficulty,OnProceedClicked proceed, OnCloseClicked close){
        SharedPreferences sp = context.getSharedPreferences(DigiConstants.PREF_WARN_FILE,Context.MODE_PRIVATE);


        closeButton.setOnClickListener(v -> {
			close.onCloseClicked();
		}); 
        
		proceedButton.setOnClickListener(v -> {
            proceed.onProceedClicked();
			
		});
        setUpDelayToProceedOn(proceedButton);
        
        String msg = sp.getString(DigiConstants.PREF_WARN_MESSAGE,"");
        String titleT = sp.getString(DigiConstants.PREF_WARN_TITLE,"Are you sure?");
        
        if(difficulty == DigiConstants.DIFFICULTY_LEVEL_NORMAL){
            int crnt_coins = CoinManager.getCoinCount(context);
            
            textTitle.setText(titleT);
            textDescription.setText("");
            textDescription.append("Purchase 20 minutes to continue.");
            textDescription.append("\nBALANCE: " + String.valueOf(crnt_coins) +"\n\n");
            textDescription.append(msg);
            proceedButton.setText(context.getString(R.string.proceed) + " (1 Aura)");
        } else {
            textTitle.setText(titleT);
            textDescription.setText(msg);
        }
		windowManager.addView(overlayView, params);
		
        
    }
    
    public void showNoCoinsOverlay(OnProceedClicked proceed, OnCloseClicked close){
        SharedPreferences sp = context.getSharedPreferences(DigiConstants.PREF_WARN_FILE,Context.MODE_PRIVATE);
        closeButton.setOnClickListener(v -> {
			close.onCloseClicked();
		}); 
        
		proceedButton.setOnClickListener(v -> {
            Intent intent = new Intent(context,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    context.startActivity(intent);
            proceed.onProceedClicked();    
		});
        
        String msg = sp.getString(DigiConstants.PREF_WARN_MESSAGE,"");
        String titleT = sp.getString(DigiConstants.PREF_WARN_TITLE,"Are you sure?");

        textTitle.setText(R.string.no_coins_title);

        textDescription.setText("");
        textDescription.append(context.getString(R.string.desc_no_coins_overlay));
        proceedButton.setText("Perform Quest");
		windowManager.addView(overlayView, params);
    }
    
	public void removeOverlay() {
		if (windowManager != null && overlayView!= null) {
			try {
				windowManager.removeView(overlayView);
				} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

    private void setUpDelayToProceedOn(Button button){
        if(!button.isEnabled()){
            return;
        }
        button.setEnabled(false);
        button.setBackgroundColor(Color.TRANSPARENT);
        new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                button.setText("Proceed in " + millisUntilFinished/1000);
            }

            @Override
            public void onFinish() {
                button.setEnabled(true);
                button.setText(R.string.proceed);
            }
        }.start();

    }

    public interface OnProceedClicked {
        void onProceedClicked();
    }
    
    
    public interface OnCloseClicked {
        void onCloseClicked();
    }

	
	
}