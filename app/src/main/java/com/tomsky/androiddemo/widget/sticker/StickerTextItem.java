package com.tomsky.androiddemo.widget.sticker;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.tomsky.androiddemo.util.UIUtils;

/**
 * Created by j-wangzhitao on 17-6-22.
 */

public class StickerTextItem extends StickerItem {

    private Paint mTextPaint = new Paint();

    private float mTextHeight;
    public StickerTextItem() {
        super(TYPE_TEXT);
        int textSize = UIUtils.dp2px(18);
        mTextPaint.setColor(Color.RED);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextHeight = Math.abs(mTextPaint.ascent() + mTextPaint.descent())/2f;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText("我是文字Ag", mRect.centerX(), mRect.centerY() + mTextHeight, mTextPaint);
    }

    @Override
    public Bitmap capture() {
        Bitmap bitmap = Bitmap.createBitmap((int)mRect.width(), (int)mRect.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRect(0, 0, mRect.width(), mRect.height(), mLinePaint);
        canvas.drawText("我是文字Ag", mRect.centerX() - mRect.left, mRect.centerY() - mRect.top + mTextHeight, mTextPaint);

        return bitmap;
    }
}
