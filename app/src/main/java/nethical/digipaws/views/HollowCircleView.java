package nethical.digipaws.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.AttributeSet;
import android.view.View;
import nethical.digipaws.R;
public class HollowCircleView extends View {
	
	private Paint paint;
	private int strokeWidth = 5;
	private int strokeColor = Color.BLACK;
	private Bitmap image;
	
	public HollowCircleView(Context context) {
		super(context);
		init(null);
	}
	
	public HollowCircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}
	
	public HollowCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(attrs);
	}
	
	private void init(AttributeSet attrs) {
		paint = new Paint();
		paint.setAntiAlias(true);
		// Load attributes from XML
		if (attrs != null) {
			TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.HollowCircleView);
			strokeWidth = a.getDimensionPixelSize(R.styleable.HollowCircleView_strokeWidth, strokeWidth);
			strokeColor = a.getColor(R.styleable.HollowCircleView_strokeColor, strokeColor);
			int imageResId = a.getResourceId(R.styleable.HollowCircleView_icon, 0);
			if (imageResId != 0) {
				image = BitmapFactory.decodeResource(getResources(), imageResId);
			}
			a.recycle();
		}
		paint.setColor(strokeColor);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(strokeWidth);
	}
	
	// Method to set the image dynamically
	public void setImageBitmap(Bitmap bitmap) {
		this.image = bitmap;
		invalidate(); // Redraw the view with the new image
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int width = getWidth();
		int height = getHeight();
		int radius = Math.min(width, height) / 2 - strokeWidth / 2;
		
		// Draw the hollow circle
		canvas.drawCircle(width / 2, height / 2, radius, paint);
		
		// Draw the image at the center of the circle with stroke color
		if (image != null) {
			int centerX = width / 2 - image.getWidth() / 2;
			int centerY = height / 2 - image.getHeight() / 2;
			
			Paint imagePaint = new Paint();
			imagePaint.setColorFilter(new PorterDuffColorFilter(strokeColor, PorterDuff.Mode.SRC_ATOP));
			canvas.drawBitmap(image, centerX, centerY, imagePaint);
		}
	}
}