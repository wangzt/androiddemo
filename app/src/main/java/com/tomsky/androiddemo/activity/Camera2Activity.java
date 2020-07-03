package com.tomsky.androiddemo.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.tomsky.androiddemo.R;
import com.tomsky.androiddemo.fragment.Camera2BasicFragment;

/**
 * Created by j-wangzhitao on 17-4-11.
 */

public class Camera2Activity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actitity_camera2);
        getFragmentManager().beginTransaction()
                .replace(R.id.camera2_container, Camera2BasicFragment.newInstance())
                .commit();
    }
}
