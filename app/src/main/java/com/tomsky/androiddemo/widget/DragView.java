package com.tomsky.androiddemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.tomsky.androiddemo.util.LogUtils;

/**
 * Created by j-wangzhitao on 17-6-13.
 */

public class DragView extends View implements MyDragListener2.OnDragTouchCallback {

    private MyDragListener2 mDragListener;

    public DragView(Context context) {
        super(context);
        initView();
    }

    public DragView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DragView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mDragListener = new MyDragListener2(this);
        setOnTouchListener(mDragListener);
        mDragListener.setOnDragTouchCallback(this);
        mDragListener.setSupportDrag(true);
        setClickable(true);
        mDragListener.setScreenSize(getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);
    }

    @Override
    public void setX(float x) {
        super.setX(x);
        LogUtils.d("wzt-dnd", "setX:"+x);
    }

    @Override
    public void setY(float y) {
        super.setY(y);
        LogUtils.d("wzt-dnd", "setY:"+y);
    }

    @Override
    public void onDragTouchDown() {

    }

    @Override
    public void onDragTouchUp() {

    }
}
