package com.tomsky.androiddemo.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;

import com.tomsky.androiddemo.animation.evaluator.RectEvaluator;
import com.tomsky.androiddemo.animation.evaluator.ScalePos;
import com.tomsky.androiddemo.animation.evaluator.ScalePosEvaluator;

/**
 * Created by j-wangzhitao on 17-12-19.
 */

public class DrawingAnimView extends RelativeLayout {

    private static final String TAG = "DrawingAnimView";

    private static final int ANIM_DURATION = 500; // 500ms

    private Rect mOrigRect;
    private Rect mDestRect;
    private int mCount;

    public DrawingAnimView(Context context) {
        super(context);
    }

    public DrawingAnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initAnimRect(Rect destRect) {
        this.mDestRect = destRect;
        this.mOrigRect = new Rect(getLeft(), getTop(), getRight(), getBottom());
        setPivotX(0);
        setPivotY(0);
    }

    public void startAnim() {
        mCount = 0;
//        setVisibility(View.VISIBLE);

//        ValueAnimator animator = ValueAnimator.ofObject(new RectEvaluator(), mOrigRect, mDestRect);
//        animator.setDuration(ANIM_DURATION);
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                mCount++;
//
//                Rect value = (Rect) animation.getAnimatedValue();
//                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) getLayoutParams();
//                if (lp != null) {
//                    lp.width = value.width();
//                    lp.height = value.height();
//                    lp.leftMargin = value.left;
//                    lp.topMargin = value.top;
//                    lp.rightMargin = mDestRect.right - value.right;
//                    lp.bottomMargin = mDestRect.bottom - value.bottom;
//                    setLayoutParams(lp);
//                }
//                Log.d(TAG, "update, mCount:"+mCount+", scalePos:"+value);
//
//            }
//        });
//        animator.start();

        float scaleX = mDestRect.width()*1f/mOrigRect.width();
        float scaleY = mDestRect.height()*1f/mOrigRect.height();
        float scale = 1f;
        float destX = 0;
        float destY = 0;
        if (scaleX > scaleY) {
            scale = scaleX;
            destY = (mDestRect.height() - mOrigRect.height() * scale) * 0.5f;
        } else {
            scale = scaleY;
            destX = (mDestRect.width() - mOrigRect.width() * scale) * 0.5f;
        }
        ScalePos start = new ScalePos(1, mOrigRect.left, mOrigRect.top);
        ScalePos end = new ScalePos(scale, destX, destY);

        Log.d(TAG, "start, scalePos: "+start);
        Log.d(TAG, "end, scalePos: "+end);

        ValueAnimator animator = ValueAnimator.ofObject(new ScalePosEvaluator(), start, end);
        animator.setDuration(ANIM_DURATION);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCount++;

                ScalePos value = (ScalePos) animation.getAnimatedValue();
                setScaleX(value.scale);
                setScaleY(value.scale);
                setX(value.x);
                setY(value.y);
                Log.d(TAG, "update, mCount:"+mCount+", scalePos:"+value);
            }
        });
        animator.start();

    }

    public void reset() {
        if (mOrigRect == null) return;

//        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) getLayoutParams();
//        if (lp != null) {
//            lp.width = mOrigRect.width();
//            lp.height = mOrigRect.height();
//            lp.leftMargin = mOrigRect.left;
//            lp.topMargin = mOrigRect.top;
//            lp.rightMargin = mDestRect.right - mOrigRect.right;
//            lp.bottomMargin = mDestRect.bottom - mOrigRect.bottom;
//            setLayoutParams(lp);
//        }

        setScaleX(1f);
        setScaleY(1f);
        setX(mOrigRect.left);
        setY(mOrigRect.top);
//
//        Log.d(TAG, "------reset");
//        setVisibility(View.INVISIBLE);


    }
}
