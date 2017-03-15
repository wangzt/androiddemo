package com.tomsky.androiddemo.util;

import android.hardware.Camera;
import android.text.TextUtils;

import java.util.List;

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

    /**
     * 设置对焦模式
     * @param paramCamera
     * @param paramParameters
     * @return
     */
    public static boolean setCameraFocusMode(Camera paramCamera, Camera.Parameters paramParameters, boolean bAutoFocus)
    {
        List localList = paramParameters.getSupportedFocusModes();
        if (localList == null)
        {
            return false;
        }
        String focusMode = null;
        if (bAutoFocus && localList.contains(Camera.Parameters.FOCUS_MODE_AUTO))
        {
            focusMode = Camera.Parameters.FOCUS_MODE_AUTO;
        }
        else if (localList.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO))
        {
            focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO;
        }
        else if (localList.contains(Camera.Parameters.FOCUS_MODE_INFINITY))
        {
            focusMode = Camera.Parameters.FOCUS_MODE_INFINITY;
        }
        try
        {
            if (!TextUtils.isEmpty(focusMode))
            {
                paramParameters.setFocusMode(focusMode);
                paramCamera.setParameters(paramParameters);
                return true;
            }
        }
        catch (Exception localException)
        {
            paramParameters.setFocusMode(paramParameters.getFocusMode());
        }
        return false;
    }
}
