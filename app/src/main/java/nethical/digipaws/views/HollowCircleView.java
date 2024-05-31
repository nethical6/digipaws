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


import com.google.android.material.color.MaterialColors;

import nethical.digipaws.R;

public class HollowCircleView extends View {
	
	private Paint paint;
	private int strokeWidth = 5;
	private int strokeColor = Color.BLACK;
	private Bitmap image;
	private int colorPrimary;
    
	public HollowCircleView(Context context) {
		super(context);
		init(null,context);
	}
	
	public HollowCircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
        init(attrs,context);
	}
	
	public HollowCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(attrs,context);
        
	}
	
    public void setColor(int color){
        this.colorPrimary = color;
    }
    
	private void init(AttributeSet attrs,Context context) {
        
        
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
        colorPrimary = MaterialColors.getColor(context, com.google.android.material.R.attr.colorPrimary, strokeColor);
		
        
		paint.setColor(colorPrimary);
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
			imagePaint.setColorFilter(new PorterDuffColorFilter(colorPrimary, PorterDuff.Mode.SRC_ATOP));
			canvas.drawBitmap(image, centerX, centerY, imagePaint);
		}else{
           Paint textPaint = new Paint();
            textPaint.setColor(colorPrimary); 
            textPaint.setTextSize(56); 
            
            // Get the TextView text
            String text = "QUESTS";
            // Calculate center coordinates for the text
            float textWidth = textPaint.measureText(text);
            int xPos = width / 2 - (int) (textWidth / 2);
            int yPos = (int) ((getHeight() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2));
        
            canvas.drawText(text, xPos, yPos, textPaint);
   
        }
	}
}