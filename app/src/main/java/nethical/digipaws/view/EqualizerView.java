package nethical.digipaws.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import nethical.digipaws.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EqualizerView extends View {

    private static final int MIN_BAR_WIDTH_DP = 9;
    private static final int MAX_BAR_HEIGHT_PERCENT = 80;
    private static final int ANIMATION_DURATION_MS = 500;
    private static final int MIN_DELAY_MS = 100;
    private static final int MAX_DELAY_MS = 700;
    private static final int BAR_MARGIN_DP = 3;

    private List<ValueAnimator> animators;
    private List<Integer> barHeights;
    private Paint paint;
    private int barWidthPx;
    private int barMarginPx;
    private Random random;
    private boolean isAnimating;

    public EqualizerView(Context context) {
        super(context);
    }

    public EqualizerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        paint = new Paint();
        paint.setColor(ContextCompat.getColor(getContext(), R.color.lgreen));
        paint.setStyle(Paint.Style.FILL);

        random = new Random();
        animators = new ArrayList<>();
        barHeights = new ArrayList<>();
        isAnimating = false; // Initialize animation state

        // Start animation only when the view is visible and has valid dimensions
        post(
                () -> {
                    if (getWidth() > 0 && getHeight() > 0) {
                        setupAnimation();
                        startAnimation();
                    }
                });
    }

    private void setupAnimation() {
        int viewWidth = getWidth();

        // Calculate effective bar width including margin
        int dpToPx = (int) (BAR_MARGIN_DP * getResources().getDisplayMetrics().density);
        barMarginPx = dpToPx(BAR_MARGIN_DP);
        int totalBarWidthPx = dpToPx(MIN_BAR_WIDTH_DP) + barMarginPx;

        // Calculate number of bars that can fit within the view width
        int numBars = Math.max(1, viewWidth / totalBarWidthPx);

        barWidthPx = (viewWidth - (numBars - 1) * barMarginPx) / numBars;
        int maxBarHeight = (int) (getHeight() * (MAX_BAR_HEIGHT_PERCENT / 100.0f));

        for (int i = 0; i < numBars; i++) {
            barHeights.add(0);

            final int index = i;
            ValueAnimator animator = ValueAnimator.ofInt(0, maxBarHeight);
            animator.setDuration(ANIMATION_DURATION_MS);
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.setRepeatMode(ValueAnimator.REVERSE);
            animator.addUpdateListener(
                    animation -> {
                        barHeights.set(index, (int) animation.getAnimatedValue());
                        invalidate();
                    });

            // Random delay for starting each animator
            animator.setStartDelay(random.nextInt(MAX_DELAY_MS - MIN_DELAY_MS) + MIN_DELAY_MS);

            animators.add(animator);
        }
    }

    private void startAnimation() {
        for (ValueAnimator animator : animators) {
            animator.start();
        }
        isAnimating = true;
    }

    public void pauseAnimation() {
        try {
            if (isAnimating) {
                for (ValueAnimator animator : animators) {
                    animator.pause();
                }
                isAnimating = false;
            }
        } catch (Exception e) {
        }
    }

    public void resumeAnimation() {
        try {
            if (!isAnimating) {
                for (ValueAnimator animator : animators) {
                    animator.resume();
                }
                isAnimating = true;
            }
        } catch (Exception e) {
        }
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // Re-setup animation if view dimensions change
        setupAnimation();
        if (isAnimating) {
            startAnimation();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int startX = 0;
        int bottom = getHeight();

        for (int i = 0; i < barHeights.size(); i++) {
            int barHeight = barHeights.get(i);
            canvas.drawRect(startX, bottom - barHeight, startX + barWidthPx, bottom, paint);
            startX += barWidthPx + barMarginPx; // Add margin between bars
        }
    }
}
