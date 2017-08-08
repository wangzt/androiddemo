package com.tomsky.androiddemo.view.opengl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.AttributeSet;

import com.tomsky.androiddemo.util.LogUtils;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by j-wangzhitao on 17-8-4.
 */

public class SixView extends GLSurfaceView {
    public SixView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        setRenderer(new MyRender());
    }

    public SixView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        setRenderer(new MyRender());
    }

    class MyRender implements GLSurfaceView.Renderer {
        private SixShape circle;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1);
            circle = new SixShape(0.5f);
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        }

        // 投影矩阵
        private final float[] mProjectionMatrix = new float[16];
        // 视图矩阵
        private final float[] mViewMatrix = new float[16];
        // 模型矩阵
        private final float[] mMMatrix = new float[16];
        private final float[] mViewProjectionMatrix = new float[16];
        private final float[] mMVPMatrix = new float[16];

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES20.glViewport(0, 0, width, height); // 设置视口，View左下角为(0,0)
            if (width < height) {
                float ratio= (float) height / width;
                // 设置正交投影
                Matrix.orthoM(mProjectionMatrix, 0, -1, 1, -ratio, ratio, 0, 5);
            } else {
                float ratio= (float) width / height;
                // 设置正交投影
                Matrix.orthoM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 0, 5);
            }
            // 设置视图矩阵
            Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 0,  0, 0, 1, 0, 1, 0);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            Matrix.multiplyMM(mViewProjectionMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
            // 设置模型矩阵
            Matrix.setIdentityM(mMMatrix, 0);
            Matrix.translateM(mMMatrix,0,0,0,1);
//            Matrix.rotateM(mMMatrix, 0, 30, 0, 0, 1);
            Matrix.multiplyMM(mMVPMatrix, 0, mViewProjectionMatrix, 0, mMMatrix, 0);
            circle.draw(mMVPMatrix);
        }
    }
}
