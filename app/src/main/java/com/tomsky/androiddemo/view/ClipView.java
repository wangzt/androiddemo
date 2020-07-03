package com.tomsky.androiddemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by j-wangzhitao on 17-4-22.
 */

public class ClipView extends ImageView {

    private Drawable mImageDrawable;

    private boolean mCropToPadding = false;

    private int mPaddingLeft = 0;
    private int mPaddingRight = 0;

    public ClipView(Context context) {
        this(context, null);
    }

    public ClipView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (mImageDrawable == null) {
            mImageDrawable = getDrawable();
        }

        if (mImageDrawable != null) {
            if (mPaddingLeft == 0 && mPaddingRight == 0) {
                mImageDrawable.draw(canvas);
                return;
            }
            int saveCount = canvas.getSaveCount();
            canvas.save();

            if (mCropToPadding) {
                final int scrollX = getScrollX();
                final int scrollY = getScrollY();
                canvas.clipRect(scrollX + mPaddingLeft, getScrollY(),
                        scrollX + getRight() - getLeft() - mPaddingRight,
                        scrollY + getBottom() - getTop());
            }

            canvas.translate(mPaddingLeft, 0);

            mImageDrawable.draw(canvas);
            canvas.restoreToCount(saveCount);
        }
    }

    public void setClipPadding(int paddingLeft, int paddingRight) {
        int width = getWidth();
        this.mPaddingLeft = paddingLeft*width/10000;
        this.mPaddingRight = paddingRight*width/10000;
        mCropToPadding = true;
        invalidate();
    }
}
