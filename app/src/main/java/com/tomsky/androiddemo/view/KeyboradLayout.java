package com.tomsky.androiddemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.tomsky.androiddemo.util.LogUtils;

/**
 * Created by j-wangzhitao on 17-9-5.
 */

public class KeyboradLayout extends RelativeLayout {

    private static final String TAG = "wzt-keyborad";
    public KeyboradLayout(Context context) {
        super(context);
        initLayoutListener();
    }

    public KeyboradLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayoutListener();
    }

    public KeyboradLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayoutListener();
    }

    private void initLayoutListener() {
        addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (LogUtils.DEBUG) {
                    Log.d(TAG, "--------onLayoutChange, (l:"+left+",t:"+top+",r:"+right+",b:"+bottom+") old (l:"+oldLeft+", t:"+oldTop+", r:"+oldRight+", b:"+oldBottom+")");
                }
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (LogUtils.DEBUG) {
            Log.d(TAG, "=====onLayout, l:"+l+", t:"+ t+", r:"+r+", b:"+b);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (LogUtils.DEBUG) {
            Log.d(TAG, "=====onMeasure, w:"+widthMeasureSpec+", h:"+ heightMeasureSpec);
        }
    }
}
