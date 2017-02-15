package com.tomsky.androiddemo.view;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.TextureView;

import com.tomsky.androiddemo.util.CameraUtils;

/**
 * Created by j-wangzhitao on 17-2-14.
 */

public class CameraView extends TextureView implements TextureView.SurfaceTextureListener {

    private Camera mCamera;

    private int mCameraId = -1;
    public CameraView(Context context) {
        this(context, null);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        if (CameraUtils.hasFrontCamera()) {
            mCameraId = CameraUtils.getCameraId(Camera.CameraInfo.CAMERA_FACING_FRONT);
        } else {
            mCameraId = CameraUtils.getCameraId(Camera.CameraInfo.CAMERA_FACING_BACK);
        }
        setSurfaceTextureListener(this);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        try {
            mCamera = CameraUtils.openCamera(mCameraId);
            if (mCamera != null) {
                mCamera.setPreviewTexture(surface);
                mCamera.startPreview();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
        }
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}
