package com.tomsky.androiddemo.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.tomsky.androiddemo.R;
import com.tomsky.androiddemo.util.LogUtils;

/**
 * Created by j-wangzhitao on 17-2-14.
 */

public class RotationActivity extends FragmentActivity implements View.OnClickListener {
    private static final String TAG = "rotation";

    private TextView mIndicatorTv;

    private int currentOrientation = -1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentOrientation = getResources().getConfiguration().orientation;
        LogUtils.d(TAG, "onCreate, orientation:"+currentOrientation);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_rotation);

        initViews();
    }

    private void initViews() {
        mIndicatorTv = (TextView) findViewById(R.id.indicator_tv);
        findViewById(R.id.portrait_btn).setOnClickListener(this);
        findViewById(R.id.landscape_btn).setOnClickListener(this);
        mIndicatorTv.setText("orientation:"+currentOrientation);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.portrait_btn:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;

            case R.id.landscape_btn:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        LogUtils.d(TAG, "onConfigurationChanged, lastOrientation:"+currentOrientation+", new orientation:"+newConfig.orientation+", height:"+getResources().getDisplayMetrics().heightPixels);
        currentOrientation = newConfig.orientation;

        if (mIndicatorTv != null) {
            if (currentOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                mIndicatorTv.setVisibility(View.VISIBLE);
            } else {
                mIndicatorTv.setVisibility(View.GONE);
            }
            mIndicatorTv.setText("orientation:"+currentOrientation);
        }
    }
}
