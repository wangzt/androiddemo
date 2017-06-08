package com.tomsky.androiddemo.view.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.tomsky.androiddemo.R;

/**
 * Created by jialiwei on 2015/10/12.
 *
 * draw point to indicator how many pagers and which position current page is
 *
 */
public class BannerIndicator extends View {

    private static final int DEFAULT_RADIUS = 10;

    private static final int DEFAULT_GAP = 14;

    private static final float DEFAULT_HILIGHT_SCALE = 1.3f;

    private static final int DEFAULT_COLOR = 0x66ffffff;

    private static final int DEFAULT_HIGHT_COLOR = 0xffffffff;

    private int color;

    private int highlightColor;

    private int radius; // 圆点半径

    private double highlightScale;

    private int count;

    private int position;

    private int gap; // 圆点间距

    private Paint paint;

    private int centerY;

    public BannerIndicator(Context context) {
        super(context);
        this.paint = new Paint();
    }

    public BannerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BannerIndicator);
        color = a.getColor(R.styleable.BannerIndicator_hideColor, DEFAULT_COLOR);
        highlightColor = a.getColor(R.styleable.BannerIndicator_highLightColor, DEFAULT_HIGHT_COLOR);
        radius = a.getDimensionPixelSize(R.styleable.BannerIndicator_cycleRadius, DEFAULT_RADIUS);
        gap = a.getDimensionPixelSize(R.styleable.BannerIndicator_cycleGap, DEFAULT_GAP);
        highlightScale = a.getFloat(R.styleable.BannerIndicator_highlightScale, DEFAULT_HILIGHT_SCALE);
        count = a.getInt(R.styleable.BannerIndicator_count, 0);
        position = a.getInt(R.styleable.BannerIndicator_position, 0);

        a.recycle();

        this.paint = new Paint();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        if (count < 2) return;

        if (centerY == 0) {
            centerY = getHeight() / 2;
        }

        int width = getWidth();
        int startX = (width - radius * 2 * count - gap * (count - 1)) / 2 + radius;

        for (int i = 0; i < count; i++) {
            int x = startX + i * (radius + gap);
            int drawRadius = radius;
            int drawColor = color;
            if (position == i) {
                drawRadius = (int) (radius * highlightScale);
                drawColor = highlightColor;
            }
            paint.setColor(drawColor);
            canvas.drawCircle(x, centerY, drawRadius, paint);
        }

    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
        invalidate();
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public double getHighlightScale() {
        return highlightScale;
    }

    public void setHighlightScale(double highlightScale) {
        this.highlightScale = highlightScale;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getHighlightColor() {
        return highlightColor;
    }

    public void setHighlightColor(int highlightColor) {
        this.highlightColor = highlightColor;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    @Override
    public String toString() {
        return "BannerIndicator{" +
                "color=" + color +
                ", highlightColor=" + highlightColor +
                ", radius=" + radius +
                ", highlightScale=" + highlightScale +
                ", count=" + count +
                ", position=" + position +
                '}';
    }
}
