package com.tomsky.androiddemo.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

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
