package com.tomsky.androiddemo.activity;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.tomsky.androiddemo.R;

/**
 * Created by j-wangzhitao on 17-7-20.
 */

public class OpenGLActivity extends Activity {

    private static String TAG = "wzt-gl";

    private GLSurfaceView mGLSurfaceView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_opengl);
        initViews();

    }

    private void initViews() {
        mGLSurfaceView = (GLSurfaceView) findViewById(R.id.gl_surface_view);
    }


}
