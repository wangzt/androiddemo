package com.tomsky.androiddemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class CircleProgressBar extends View {
    private final TextPaint b = new TextPaint();
    private int mProgress = 100;
    private int mMax = 100;
    private Paint mPaint = new Paint();
    private RectF mRect = new RectF();
    private int g = 20;
    private int h = 75;

    public int getMax() {
        return this.mMax;
    }

    public void setMax(int var1) {
        this.mMax = var1;
    }

    public int getProgress() {
        return this.mProgress;
    }

    public void setProgress(int var1) {
        this.mProgress = var1;
        Log.d("wzt-circle", "progress:"+ mProgress);
        this.invalidate();
    }

    protected void onMeasure(int var1, int var2) {
        int var3 = MeasureSpec.getSize(var1);
        int var4 = MeasureSpec.getSize(var2);
        int var5 = var3 > var4 ? var4 : var3;
        int var6 = this.g + this.h;
        this.g = var5 / 2 * this.g / var6;
        this.h = var5 / 2 * this.h / var6;
        this.setMeasuredDimension(var5, var5);
    }

    public CircleProgressBar(Context var1, AttributeSet var2) {
        super(var1, var2);
    }

    protected void onDraw(Canvas var1) {
        super.onDraw(var1);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setFlags(1);
//        this.mPaint.setColor(-16777216);
        this.mPaint.setStrokeWidth((float)this.g);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeCap(Paint.Cap.ROUND);
//        var1.drawCircle((float)(this.h + this.g), (float)(this.h + this.g), (float)this.h, this.mPaint);
        this.mPaint.setColor(-12594716);
        this.mRect.set((float)this.g, (float)this.g, (float)(this.h * 2 + this.g), (float)(this.h * 2 + this.g));
        var1.drawArc(this.mRect, -90.0F, (float)this.mProgress / (float)this.mMax * 360.0F, false, this.mPaint);
        this.mPaint.reset();
    }
}
