package com.tomsky.androiddemo.activity;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.tomsky.androiddemo.R;

/**
 * Created by j-wangzhitao on 17-6-13.
 */

public class TrashActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash);
    }
}
