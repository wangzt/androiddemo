package com.tomsky.androiddemo.util;

import android.hardware.Camera;

/**
 * Created by j-wangzhitao on 17-2-14.
 */

public class CameraUtils {

    public static Camera openCamera(int id) {
        if (id < 0 || id >= getNumberOfCameras()) {
            return null;
        }
        Camera camera = null;
        try {
            camera = Camera.open(id);
        } catch (Throwable e) {
            e.printStackTrace();
            camera = null;
        }
        return camera;
    }

    public static int getNumberOfCameras() {
        return Camera.getNumberOfCameras();
    }

    public static boolean hasFrontCamera() {
        return getCameraId(Camera.CameraInfo.CAMERA_FACING_FRONT) != -1;
    }

    public static int getCameraId(final int facing) {
        int numberOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo info = new Camera.CameraInfo();
        for (int id = 0; id < numberOfCameras; id++) {
            Camera.getCameraInfo(id, info);
            if (info.facing == facing) {
                return id;
            }
        }
        return -1;
    }
}
