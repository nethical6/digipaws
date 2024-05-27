package nethical.digipaws.utils;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.view.View;
import android.animation.ObjectAnimator;
import android.view.WindowManager;

public class AnimationHelper {
	
	public static void fadeOut(View view, int duration){
		ObjectAnimator fadeOut = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
		fadeOut.setDuration(duration);
		fadeOut.start();
	}
	
	public static void fadeIn(View view, int duration){
		ObjectAnimator fadeIn = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
		fadeIn.setDuration(duration);
		fadeIn.start();
	}
	// Todo: smoothen this animation through any other means
	public static void expandWindow(View view,int duration,WindowManager.LayoutParams params,WindowManager windowManager){
		int initialWidth = view.getWidth();
		int initialHeight = view.getHeight();
		
		ValueAnimator widthAnimator = ValueAnimator.ofInt(initialWidth, windowManager.getDefaultDisplay().getWidth());
		ValueAnimator heightAnimator = ValueAnimator.ofInt(initialHeight, windowManager.getDefaultDisplay().getHeight());
		
		widthAnimator.addUpdateListener(animation -> {
			params.width = (int) animation.getAnimatedValue();
			//view.getLayoutParams().width= (int) animation.getAnimatedValue();
			windowManager.updateViewLayout(view, params);
		});
		
		heightAnimator.addUpdateListener(animation -> {
			params.height = (int) animation.getAnimatedValue();
			windowManager.updateViewLayout(view, params);
			//view.getLayoutParams().height= (int) animation.getAnimatedValue();
		});
		
		
		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.playTogether(widthAnimator, heightAnimator);
		animatorSet.setDuration(duration); // Animation duration in milliseconds
		animatorSet.setStartDelay(100);
		
		animatorSet.start();
		
	

	}
	
	public static void fexpandWindow(View view, int duration, WindowManager.LayoutParams params, WindowManager windowManager, AnimationListener listener) {
		// Set the WindowManager.LayoutParams to full width and height initially
		params.width = WindowManager.LayoutParams.MATCH_PARENT;
		params.height = WindowManager.LayoutParams.MATCH_PARENT;
		windowManager.updateViewLayout(view, params);
		
		// Get the initial dimensions and positions
		int initialWidth = view.getWidth();
		int initialHeight = view.getHeight();
		int screenWidth = windowManager.getDefaultDisplay().getWidth();
		int screenHeight = windowManager.getDefaultDisplay().getHeight();
		
		// Calculate the initial and final positions for the animation
		int initialX = (screenWidth - initialWidth) / 2;
		int initialY = (screenHeight - initialHeight) / 2;
		int finalX = 0;
		int finalY = 0;
		
		// Set the initial position
		view.setX(initialX);
		view.setY(initialY);
		
		// Animate the position changes
		view.animate()
		.x(finalX)
		.y(finalY)
		.scaleX((float) screenWidth / initialWidth)
		.scaleY((float) screenHeight / initialHeight)
		.setDuration(duration)
		.withEndAction(() -> {
			// Notify listener when animation ends
			if (listener != null) {
				listener.onAnimationOver();
			}
		})
		.start();
	}
	
	
	
	
	public interface AnimationListener{
		public void onAnimationOver();
	}
}