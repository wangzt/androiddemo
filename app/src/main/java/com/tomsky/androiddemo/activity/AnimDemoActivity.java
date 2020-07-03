package com.tomsky.androiddemo.activity;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.tomsky.androiddemo.R;
import com.tomsky.androiddemo.view.DrawingAnimView;

/**
 * Created by j-wangzhitao on 17-12-19.
 */

public class AnimDemoActivity extends Activity implements View.OnClickListener {

    private View mRootView;
    private DrawingAnimView mAnimView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim);
        mRootView = findViewById(R.id.anim_root_layout);
        mAnimView = (DrawingAnimView) findViewById(R.id.anim_layout);

        findViewById(R.id.btn_init).setOnClickListener(this);
        findViewById(R.id.btn_start_anim).setOnClickListener(this);
        findViewById(R.id.btn_end_anim).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_init:
                mAnimView.initAnimRect(new Rect(0, 0, mRootView.getWidth(), mRootView.getHeight()));
                break;
            case R.id.btn_start_anim:
                mAnimView.startAnim();
                break;
            case R.id.btn_end_anim:
                mAnimView.reset();
                break;
        }
    }




}
