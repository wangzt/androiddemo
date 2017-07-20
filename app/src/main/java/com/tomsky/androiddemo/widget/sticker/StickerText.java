package com.tomsky.androiddemo.widget.sticker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tomsky.androiddemo.R;
import com.tomsky.androiddemo.util.UIUtils;

/**
 * Created by j-wangzhitao on 17-6-22.
 */

public class StickerText extends Sticker {

    private Paint mTextPaint = new Paint();

    private float mTextHeight;

    private TextView mTextView;

    public StickerText(Context context, ViewGroup parent) {
        super(TYPE_TEXT);

        mTextView = new TextView(context);
        mTextView.setVisibility(View.INVISIBLE);
        parent.addView(mTextView);

        int textSize = UIUtils.dp2px(18);
        mTextPaint.setColor(Color.RED);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextHeight = Math.abs(mTextPaint.ascent() + mTextPaint.descent())/2f;
    }

    @Override
    public void setRect(float left, float top, float right, float bottom) {
        super.setRect(left, top, right, bottom);
        if (mTextView != null) {
            mTextView.setMaxLines(1);
            mTextView.setSingleLine(true);
            mTextView.setHeight((int) (bottom - top));
            mTextView.setWidth((int) (right - left));
            mTextView.setPadding(70, 0, 70, 0);
            mTextView.setTextColor(Color.RED);
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            mTextView.setGravity(Gravity.CENTER);
            mTextView.setText("我是文字Aggfasdpfaspfaspdfjsa");
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawRect(mRect, mLinePaint);
        canvas.drawText("我是文字Ag", mRect.centerX(), mRect.centerY() + mTextHeight, mTextPaint);
    }

    @Override
    public Bitmap capture() {

        Bitmap bitmap = Bitmap.createBitmap((int)mRect.width(), (int)mRect.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
//        mTextView.draw(canvas);
//
        canvas.drawRect(0, 0, mRect.width(), mRect.height(), mLinePaint);
//        canvas.drawText("我是文字Ag", mRect.centerX() - mRect.left, mRect.centerY() - mRect.top + mTextHeight, mTextPaint);
//
        mTextView.setDrawingCacheEnabled(true);
        Bitmap bmp = mTextView.getDrawingCache();
        canvas.drawBitmap(bmp, 0, 0, null);
        mTextView.setDrawingCacheEnabled(false);
        return bitmap;


    }
}
