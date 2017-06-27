package com.tomsky.androiddemo.widget.sticker;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;

/**
 * Created by j-wangzhitao on 17-6-23.
 */

public abstract class Sticker {

    public static final int TYPE_IMAGE = 0;
    public static final int TYPE_TEXT = 1;

    protected int mType = TYPE_IMAGE;

    protected RectF mRect = new RectF();
    protected PointF mStartPoint = new PointF(); // move操作的起始点
    protected PointF mDownPoint = new PointF(); // 点击操作的起始点

    protected Paint mLinePaint = new Paint();

    public Sticker(int type) {
        this.mType = type;
        init();
    }

    private void init() {
        DashPathEffect effects = new DashPathEffect(new float[]{20, 10, 20, 10}, 1);

        mLinePaint.setColor(Color.BLACK);
        mLinePaint.setPathEffect(effects);
        mLinePaint.setStrokeWidth(4);
        mLinePaint.setStyle(Paint.Style.STROKE);
    }

    public int getType() {
        return mType;
    }

    public void setRect(float left, float top, float right, float bottom) {
        mRect.set(left, top, right, bottom);
    }

    public boolean isPointInRect(float x, float y) {
        return mRect.contains(x, y);
    }

    public void onDown(float x, float y) {
        mStartPoint.set(x, y);
        mDownPoint.set(x, y);
    }

    public void onMove(float toX, float toY, float width, float height) {
        float dx = toX - mStartPoint.x;
        float dy = toY - mStartPoint.y;
        if (mRect.left + dx < 0) {
            dx = 0 - mRect.left;
            toX = dx + mStartPoint.x;
        } else if (mRect.right + dx > width) {
            dx = width - mRect.right;
            toX = dx + mStartPoint.x;
        }
        if (mRect.top + dy < 0) {
            dy = 0 - mRect.top;
            toY = dy + mStartPoint.y;
        } else if (mRect.bottom + dy > height) {
            dy = height - mRect.bottom;
            toY = dy + mStartPoint.y;
        }

        mRect.offset(dx, dy);
        mStartPoint.set(toX, toY);
    }

    public float centerX() {
        return mRect.centerX();
    }

    public float centerY() {
        return mRect.centerY();
    }

    public boolean isClick(float x, float y) {
        float dx = x - mDownPoint.x;
        float dy = y - mDownPoint.y;
        if (Math.sqrt(dx*dx + dy*dy) < 20) {
            return true;
        }
        return false;
    }

    public boolean isMove(float x, float y) {
        float dx = x - mDownPoint.x;
        float dy = y - mDownPoint.y;
        if (Math.sqrt(dx*dx + dy*dy) > 30) {
            return true;
        }
        return false;
    }

    public abstract void onDraw(Canvas canvas);

    public abstract Bitmap capture();
}
