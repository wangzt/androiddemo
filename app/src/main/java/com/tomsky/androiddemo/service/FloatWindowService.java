package com.tomsky.androiddemo.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Binder;
import android.os.IBinder;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.tomsky.androiddemo.R;
import com.tomsky.androiddemo.activity.FloatActivity;
import com.tomsky.androiddemo.util.LogUtils;

/**
 * Created by wangzhitao on 2020/06/05
 **/
public class FloatWindowService extends Service {

    private static final String TAG = "float-window";

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams wmParams;
    private LayoutInflater inflater;

    //constant
    private boolean clickflag;

    //view
    private View mFloatingLayout;    //浮动布局
    private RelativeLayout smallSizePreviewLayout; //容器父布局
    private WebView mWebView; // H5界面

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    public class MyBinder extends Binder {
        public FloatWindowService getService() {
            return FloatWindowService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initWindow();//设置悬浮窗基本参数（位置、宽高等）
        initFloating();//悬浮框点击事件的处理
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int result = super.onStartCommand(intent, flags, startId);

        if (intent != null) {
            if (intent.hasExtra(FloatActivity.EXTRA_NAME)) {
                String name = intent.getStringExtra(FloatActivity.EXTRA_NAME);
                LogUtils.i(TAG, "name: "+name);
            }
        }
        return result;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mFloatingLayout != null) {
            // 移除悬浮窗口
            mWindowManager.removeView(mFloatingLayout);
        }
    }

    /**
     * 设置悬浮框基本参数（位置、宽高等）
     */
    private void initWindow() {
        mWindowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        wmParams = getParams();//设置好悬浮窗的参数
        // 悬浮窗默认显示以左上角为起始坐标
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        //悬浮窗的开始位置，因为设置的是从左上角开始，所以屏幕左上角是x=0;y=0
        wmParams.x = 70;
        wmParams.y = 210;
        //得到容器，通过这个inflater来获得悬浮窗控件
        inflater = LayoutInflater.from(getApplicationContext());
        // 获取浮动窗口视图所在布局
        mFloatingLayout = inflater.inflate(R.layout.alert_float_window_layout, null);
        // 添加悬浮窗的视图
        mWindowManager.addView(mFloatingLayout, wmParams);

        mWebView = mFloatingLayout.findViewById(R.id.float_webview);

        mFloatingLayout.findViewById(R.id.float_load_btn).setOnClickListener(v -> {
            if (mWebView != null) {
                mWebView.loadUrl("https://wenku.baidu.com/topic/composition2020?channel=pcbd");
            }
        });
    }


    private WindowManager.LayoutParams getParams() {
        wmParams = new WindowManager.LayoutParams();
        //设置window type 下面变量2002是在屏幕区域显示，2003则可以显示在状态栏之上
        wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        //设置可以显示在状态栏上
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR |
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;

        //设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        return wmParams;
    }


    private void initFloating() {
        smallSizePreviewLayout = mFloatingLayout.findViewById(R.id.small_size_preview);

        //悬浮框点击事件
        smallSizePreviewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //在这里实现点击重新回到Activity
                Intent intent = new Intent();
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClass(getApplicationContext(), FloatActivity.class);
                startActivity(intent);
            }
        });

        //悬浮框触摸事件，设置悬浮框可拖动
        smallSizePreviewLayout.setOnTouchListener(new FloatingListener());
    }

    //开始触控的坐标，移动时的坐标（相对于屏幕左上角的坐标）
    private int mTouchStartX, mTouchStartY, mTouchCurrentX, mTouchCurrentY;
    //开始时的坐标和结束时的坐标（相对于自身控件的坐标）
    private int mStartX, mStartY, mStopX, mStopY;
    //判断悬浮窗口是否移动，这里做个标记，防止移动后松手触发了点击事件
    private boolean isMove;

    private int tempY = 0;
    private class FloatingListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    isMove = false;
                    mTouchStartX = (int) event.getRawX();
                    mTouchStartY = (int) event.getRawY();
                    mStartX = mTouchStartX;
                    mStartY = mTouchStartY;
                    break;
                case MotionEvent.ACTION_MOVE:
                    mTouchCurrentX = (int) event.getRawX();
                    mTouchCurrentY = (int) event.getRawY();
                    wmParams.x += mTouchCurrentX - mTouchStartX;
//                    wmParams.y += mTouchCurrentY - mTouchStartY;

                    tempY = wmParams.y + mTouchCurrentY - mTouchStartY;
                    if (tempY < 80) {
                        tempY = 80;
                    }
                    wmParams.y = tempY;

                    mWindowManager.updateViewLayout(mFloatingLayout, wmParams);

                    mTouchStartX = mTouchCurrentX;
                    mTouchStartY = mTouchCurrentY;
                    break;
                case MotionEvent.ACTION_UP:
                    mStopX = (int) event.getRawX();
                    mStopY = (int) event.getRawY();
                    if (Math.abs(mStartX - mStopX) >= 2 || Math.abs(mStartY - mStopY) >= 2) {
                        isMove = true;
                    }
                    if (isMove) {
                        slideToBorder();
                    }
                    break;
            }

            //如果是移动事件不触发OnClick事件，防止移动的时候一放手形成点击事件
            return isMove;
        }
    }

    private void slideToBorder() {
        Point point = new Point();
        mWindowManager.getDefaultDisplay().getSize(point);
        int width = mFloatingLayout.getWidth();
//        Log.i(TAG, "width:"+width+", pWidth:"+point.x+", pHeight:"+point.y+", wmParams.x:"+wmParams.x+", wmParams.y:"+wmParams.y);
    }
}

//    int leftLimit = (getWidth() - dragView.getWidth()) / 2;
//    //在最中间的时候动画所需最大执行时间
//    int maxDuration = 500;
//    int duration;
//                        if (dragView.getLeft() < leftLimit) {
//        //根据距离边界的距离，弹性计算动画执行时间，防止距离边界很近的时候执行时间仍是过长
//        duration = maxDuration * (dragView.getLeft() + hideSize) / (leftLimit + hideSize);
//        animSlide(dragView, dragView.getLeft(), -hideSize, duration);
//        } else {
//        duration = maxDuration * (getWidth() + hideSize - dragView.getRight()) / (leftLimit + hideSize);
//        animSlide(dragView, dragView.getLeft(), getWidth() - dragView.getWidth() + hideSize, duration);
//        }
