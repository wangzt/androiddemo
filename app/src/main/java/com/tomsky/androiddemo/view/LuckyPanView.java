package com.tomsky.androiddemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by wangzhitao on 17-4-15.
 */

public class LuckyPanView extends View implements Runnable {

    private int mRadius; // 圆的半径

    private Paint mArcPaint; // 盘块的画笔

    private Paint mTextPaint; // 文字画笔

    private RectF mRange; // 圆弧的范围

    private int mItemCount = 6; // 盘块的个数

    private int[] mColors = new int[] { 0xFFFFC300, 0xFFF17E01, 0xFFFFC300,
            0xFFF17E01, 0xFFFFC300, 0xFFF17E01 }; // 盘块的颜色

    private String []mStrs = {"华为手机","魅族手机","iPhone 6s","mac book","谢谢惠顾","小米手机"}; // 盘块文字

    private static final int DEFAULT_INDEX = 4;

    private float mSweepAngle; // 盘块的角度

    private volatile float mStartAngle; // 起始角度

    private static final int STATE_IDLE = 0;  // 静止
    private static final int STATE_START = 1; // 开始
    private static final int STATE_STOP = 2;  // 停止
    private volatile int mState = STATE_IDLE;

    private float mSpeed; // 滚动的速度
    private Thread mThread = null;

    private long mDrawStartTime = 0;
    private int mDrawInterval = 0;

    public LuckyPanView(Context context) {
        super(context);
        initView(context);
    }

    public LuckyPanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LuckyPanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setDither(true);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        mTextPaint.setColor(0xFFffffff);
        mTextPaint.setTextSize(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 18, getResources().getDisplayMetrics()));

        mSweepAngle = (float) 360 / mItemCount;
        mStartAngle = 0;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = Math.min(getMeasuredWidth(), getMeasuredHeight());
        mRadius = width / 2;

        if (width > 0) {
            mRange = new RectF(0, 0, width, width);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mRadius == 0) return;

        mDrawStartTime = System.currentTimeMillis();
        float tmpAngle = mStartAngle;
        for (int i = 0; i < mItemCount; i++) {
            mArcPaint.setColor(mColors[i]);
            canvas.drawArc(mRange, tmpAngle, mSweepAngle, true, mArcPaint);
            drawText(canvas, tmpAngle, mSweepAngle, mStrs[i]);

            tmpAngle += mSweepAngle;
        }

        mStartAngle += mSpeed;

        if (mState == STATE_STOP) {
            mSpeed -= 1;
        }
        if (mSpeed <= 0) {
            mState = STATE_IDLE;
        }

        mDrawInterval = (int) (System.currentTimeMillis() - mDrawStartTime);
    }

    /**
     * 绘制文本
     *
     * @param startAngle
     * @param sweepAngle
     * @param string
     */
    private void drawText(Canvas canvas, float startAngle, float sweepAngle, String string)
    {
        Path path = new Path();
        path.addArc(mRange, startAngle, sweepAngle);
        float textWidth = mTextPaint.measureText(string);
        // 利用水平偏移让文字居中
        float hOffset = (float) (mRadius * Math.PI / mItemCount - textWidth / 2);// 水平偏移
        float vOffset = mRadius / 6;// 垂直偏移
        canvas.drawTextOnPath(string, path, hOffset, vOffset, mTextPaint);
    }

    public void start() {
        if (mState != STATE_IDLE) {
            return;
        }
        mState = STATE_START;

        calcSpeed(DEFAULT_INDEX);

        try {
            if (mThread != null) {
                mThread.interrupt();
                mThread = null;
            }
        } catch (Exception e) {
            mThread = null;
        }

        mThread = new Thread(this);
        mThread.start();

    }

    public void stop(int index) {
        Log.d("wzt-lucky", "-------------lucky index:"+index);
        if (mState == STATE_START) {
            calcSpeed(index);

            mStartAngle = 0;
            mState = STATE_STOP;

        }
    }

    public boolean isPlay() {
        return mState == STATE_START;
    }


    private void calcSpeed(int index) {
        // 每项角度大小
        float angle = (float) (360 / mItemCount);
        // 中奖角度范围（因为指针向上，所以水平第一项旋转到指针指向，需要旋转210-270；）
        float from = 270 - (index + 1) * angle;
        float to = from + angle;
        // 停下来时旋转的距离
        float targetFrom = 4 * 360 + from;
        /**
         * <pre>
         *  (v1 + 0) * (v1+1) / 2 = target ;
         *  v1*v1 + v1 - 2target = 0 ;
         *  v1=-1+(1*1 + 8 *1 * target)/2;
         * </pre>
         */
        float v1 = (float) (Math.sqrt(1 * 1 + 8 * 1 * targetFrom) - 1) / 2;
        float targetTo = 4 * 360 + to;
        float v2 = (float) (Math.sqrt(1 * 1 + 8 * 1 * targetTo) - 1) / 2;

        mSpeed = (v1+v2)/2;//停留在正中央
    }

    @Override
    public void run() {
        while (mState != STATE_IDLE) {
            try {
                if (mDrawInterval < 50) {
                    Thread.sleep(50 - mDrawInterval);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            postInvalidate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mState = STATE_IDLE;
    }

    public void onDestroy() {
        mState = STATE_IDLE;
    }
}
