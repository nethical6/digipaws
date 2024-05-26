package nethical.digipaws.utils;

import android.accessibilityservice.AccessibilityService;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import nethical.digipaws.MainActivity;
import nethical.digipaws.R;
import android.view.View;
import android.view.WindowManager;
import android.view.LayoutInflater;
import nethical.digipaws.data.ServiceData;

public class OverlayManager {
	
	private WindowManager windowManager;
	private Context context;
    private String blockerId;
	private View overlayView;
    
    private MaterialButton mb;
    private Button closeButton;
    private Button proceedButton;
    private TextView textTitle;
    private TextView textDescription;
    
    private WindowManager.LayoutParams params;
    
    // somehow implement material3 design here
	public OverlayManager(Context context, String blockerId){
		this.context = context;
        this.blockerId = blockerId;
		windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
	}
    
    private void init(){
        LayoutInflater inflater = LayoutInflater.from(context);
		overlayView = inflater.inflate(R.layout.warning_overlay, null);
        closeButton = overlayView.findViewById(R.id.close_overlay);
        proceedButton = overlayView.findViewById(R.id.proceed_overlay);
        textTitle = overlayView.findViewById(R.id.text_title);
        textDescription = overlayView.findViewById(R.id.text_desc);
		
        
    }
    
	public void showWarningOverlay() {
        init();
        applyMaterial();
		closeButton.setOnClickListener(v -> {
			AccessibilityService service = (AccessibilityService) context;
			service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
			removeOverlay();
			
		}); 
		proceedButton.setOnClickListener(v -> {
			DelayManager.updateLastWarningTime(context,blockerId);
			removeOverlay();
			
		});
		
		
		Animation fadeInAnimation = new AlphaAnimation(0.0f, 1.0f);
		fadeInAnimation.setDuration(500);  
		
		overlayView.startAnimation(fadeInAnimation);
		windowManager.addView(overlayView, params);
		
		overlayView.post(() -> {
				AnimationHelper.fadeOut(textTitle,1500);
				AnimationHelper.expandWindow(overlayView,2000,params,windowManager,()->{
				textTitle.setText(R.string.warning_title);
				proceedButton.setVisibility(View.VISIBLE);
				closeButton.setVisibility(View.VISIBLE);
				textDescription.setVisibility(View.VISIBLE);
				
				AnimationHelper.fadeIn(textTitle,1000);
				AnimationHelper.fadeIn(textDescription,2000);
				
				AnimationHelper.fadeIn(closeButton,3000);
				AnimationHelper.fadeIn(proceedButton,3000);
			});
		});
		
	}
    
    // buy watch hours
    public void showSMUseCoinsOverlay(ServiceData data){
        init();
        closeButton.setOnClickListener(v -> {
			removeOverlay();
			
		}); 
		proceedButton.setOnClickListener(v -> {
			DelayManager.updateLastWarningTime(context,blockerId);
            CoinManager.decrementCoin(context);
			removeOverlay();
			
		});
		
		
        textTitle.setText(R.string.buy_20_mins);
        textDescription.setText(R.string.desc_sd_overlay);
        textDescription.append("\n"+"BALANCE: " + String.valueOf(CoinManager.getCoinCount(context)));
        textDescription.setVisibility(View.VISIBLE);
        proceedButton.setVisibility(View.VISIBLE);
        closeButton.setVisibility(View.VISIBLE);
        
        params = new WindowManager.LayoutParams(
		WindowManager.LayoutParams.WRAP_CONTENT,
		WindowManager.LayoutParams.WRAP_CONTENT,
		WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
		WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
		PixelFormat.OPAQUE);
        
		windowManager.addView(overlayView, params);
		
    }
    
    private void applyMaterial(){
        int surface = MaterialColors.getColor(context, com.google.android.material.R.attr.backgroundTint, R.color.md_theme_dark_background);
        overlayView.setBackgroundColor(surface);
        
        int textColour = MaterialColors.getColor(context, com.google.android.material.R.attr.colorPrimary, R.color.md_theme_dark_onSurface);
        textTitle.setTextColor(textColour);
        textDescription.setTextColor(textColour);
        
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
	
	
	
}