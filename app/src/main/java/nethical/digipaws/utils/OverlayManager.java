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
import nethical.digipaws.R;
import android.view.View;
import android.view.WindowManager;
import android.view.LayoutInflater;

public class OverlayManager {
	
	private WindowManager windowManager;
	private Context context;
    private String blockerId;
	private View overlayView;
    
    
	public OverlayManager(Context context, String blockerId){
		this.context = context;
        this.blockerId = blockerId;
		windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
	}
	
	public void showWarningOverlay() {
		LayoutInflater inflater = LayoutInflater.from(context);
		overlayView = inflater.inflate(R.layout.warning_home_overlay, null);
		
		Button closeButton = overlayView.findViewById(R.id.close_overlay);
		closeButton.setOnClickListener(v -> {
			AccessibilityService service = (AccessibilityService) context;
			service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
			removeOverlay();
			
		}); 
		Button proceedButton = overlayView.findViewById(R.id.proceed_overlay);
		proceedButton.setOnClickListener(v -> {
			DelayManager.updateLastWarningTime(context,blockerId);
			removeOverlay();
			
		});
		
		TextView textTitle = overlayView.findViewById(R.id.text_title);
		TextView textDescription = overlayView.findViewById(R.id.text_desc);
		
		WindowManager.LayoutParams params = new WindowManager.LayoutParams(
		WindowManager.LayoutParams.WRAP_CONTENT,
		WindowManager.LayoutParams.WRAP_CONTENT,
		WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
		WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
		PixelFormat.TRANSLUCENT);
		
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