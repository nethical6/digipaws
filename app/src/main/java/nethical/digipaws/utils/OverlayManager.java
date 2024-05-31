package nethical.digipaws.utils;

import android.content.Context;
import android.graphics.PixelFormat;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import nethical.digipaws.R;
import android.view.View;
import android.view.WindowManager;
import android.view.LayoutInflater;
import nethical.digipaws.data.ServiceData;

public class OverlayManager {
	
	private WindowManager windowManager;
	private Context context;
	private View overlayView;
    
    private MaterialButton mb;
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
		overlayView = inflater.inflate(R.layout.warning_overlay, null);
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
        
        closeButton.setOnClickListener(v -> {
			close.onCloseClicked();
		}); 
        
		proceedButton.setOnClickListener(v -> {
            proceed.onProceedClicked();
			
		});
        
        if(difficulty == DigiConstants.DIFFICULTY_LEVEL_NORMAL){
            textTitle.setText(R.string.buy_20_mins);
            textDescription.setText(R.string.desc_sd_overlay);
            textDescription.append("\n"+"BALANCE: " + String.valueOf(CoinManager.getCoinCount(context)));
        }
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

    public interface OnProceedClicked {
        void onProceedClicked();
    }
    
    
    public interface OnCloseClicked {
        void onCloseClicked();
    }

	
	
}