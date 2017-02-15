package com.tomsky.androiddemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.tomsky.androiddemo.R;

/**
 * Created by j-wangzhitao on 17-2-14.
 */

public class CameraActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_camera);
    }
}
