package com.tomsky.androiddemo.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.TextView;

import com.tomsky.androiddemo.R;
import com.tomsky.androiddemo.util.LogUtils;
import com.tomsky.androiddemo.view.GiftButton;

/**
 * Created by j-wangzhitao on 17-2-14.
 */

public class RotationActivity extends FragmentActivity implements View.OnClickListener {
    private static final String TAG = "rotation";

    private TextView mIndicatorTv;
    private GiftButton mGiftBtn;

    private int currentOrientation = -1;

    private OrientationEventListener mOrientationListener; // 屏幕方向改变监听器
    private boolean mIsLand = false; // 是否是横屏
    private boolean mClick = false; // 是否点击
    private boolean mClickLand = true; // 点击进入横屏
    private boolean mClickPort = true; // 点击进入竖屏

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentOrientation = getResources().getConfiguration().orientation;
        LogUtils.d(TAG, "onCreate, orientation:"+currentOrientation);

//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_rotation);

        initViews();
        startListener();
    }

    private void initViews() {
        mIndicatorTv = (TextView) findViewById(R.id.indicator_tv);
        mGiftBtn = (GiftButton) findViewById(R.id.gift_btn);

        findViewById(R.id.portrait_btn).setOnClickListener(this);
        findViewById(R.id.landscape_btn).setOnClickListener(this);
        mIndicatorTv.setText("orientation:"+currentOrientation);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.portrait_btn:
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                mClick = true;
                if (mIsLand) {
//                    if (onClickOrientationListener != null) {
//                        onClickOrientationListener.portrait();
//                    }
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    mIsLand = false;
                    mClickPort = false;
                }
                break;

            case R.id.landscape_btn:
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                mClick = true;
                if (!mIsLand) {
//                    if (onClickOrientationListener != null) {
//                        onClickOrientationListener.landscape();
//                    }
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    mIsLand = true;
                    mClickLand = false;
                }
                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        LogUtils.d(TAG, "onConfigurationChanged, lastOrientation:"+currentOrientation+", new orientation:"+newConfig.orientation);
        currentOrientation = newConfig.orientation;

        if (mIndicatorTv != null) {
//            if (currentOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
//                mIndicatorTv.setVisibility(View.VISIBLE);
//            } else {
//                mIndicatorTv.setVisibility(View.GONE);
//            }
            mIndicatorTv.setText("orientation:"+currentOrientation);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopListener();
    }

    /**
     * 开启监听器
     */
    private final void startListener() {
        mOrientationListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int rotation) {
//                LogUtils.d(TAG, "rotation:"+rotation+", mClick:"+mClick+", mIsLand:"+mIsLand+", mClickedLand:"+mClickLand+", mClickedPort:"+mClickPort);
                // 设置竖屏
                if (((rotation >= 0) && (rotation <= 30)) || (rotation >= 330)) {
                    if (mClick) {
                        if (mIsLand && !mClickLand) {
                            return;
                        } else {
                            mClickPort = true;
                            mClick = false;
                            mIsLand = false;
                        }
                    } else {
                        if (mIsLand) {
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            mIsLand = false;
                            mClick = false;
                        }
                    }
                }
                // 设置横屏
                else if (((rotation >= 230) && (rotation <= 310))) {
                    if (mClick) {
                        if (!mIsLand && !mClickPort) {
                            return;
                        } else {
                            mClickLand = true;
                            mClick = false;
                            mIsLand = true;
                        }
                    } else {
                        if (!mIsLand) {
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                            mIsLand = true;
                            mClick = false;
                        }
                    }
                }
            }
        };
        mOrientationListener.enable();
    }

    private void stopListener() {
        if (mOrientationListener != null) {
            mOrientationListener.disable();
        }
    }
}
