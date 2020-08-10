package com.tomsky.androiddemo.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.tomsky.androiddemo.R;
import com.tomsky.androiddemo.fragment.Camera2BasicFragment;
import com.tomsky.androiddemo.util.LogUtils;

/**
 * Created by j-wangzhitao on 17-4-11.
 */

public class Camera2Activity extends FragmentActivity {

    private static final String TAG = "wzt-camera2";

    private static final int REQ_PERMISSION_CODE = 1020;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.actitity_camera2);

        int hasCameraPermission = checkPermission(Manifest.permission.CAMERA, android.os.Process.myPid(), android.os.Process.myUid());
        int hasAudioPermission = checkPermission(Manifest.permission.RECORD_AUDIO, android.os.Process.myPid(), android.os.Process.myUid());
        LogUtils.i(TAG, "onCreate, hasCameraPermission:"+hasCameraPermission+", hasAudioPermission:"+hasAudioPermission);

        if (hasCameraPermission == PackageManager.PERMISSION_GRANTED && hasAudioPermission == PackageManager.PERMISSION_GRANTED) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.camera2_container, Camera2BasicFragment.newInstance())
                    .commit();
        } else {
            String []permissions = {
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO
            };
            ActivityCompat.requestPermissions(this, permissions, REQ_PERMISSION_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        StringBuilder sb = new StringBuilder();
        if (permissions != null && grantResults != null && permissions.length == grantResults.length) {
            int size = permissions.length;
            for (int i = 0; i < size; i++) {
                sb.append(", permission:"+permissions[i]+" result:"+ grantResults[i]);
            }
        }
        LogUtils.i(TAG, "onRequestPermissionsResult"+sb);

        int hasCameraPermission = checkPermission(Manifest.permission.CAMERA, android.os.Process.myPid(), android.os.Process.myUid());
        int hasAudioPermission = checkPermission(Manifest.permission.RECORD_AUDIO, android.os.Process.myPid(), android.os.Process.myUid());

        LogUtils.i(TAG, "onRequestPermissionsResult, hasCameraPermission:"+hasCameraPermission+", hasAudioPermission:"+hasAudioPermission);
        if (hasCameraPermission == PackageManager.PERMISSION_GRANTED && hasAudioPermission == PackageManager.PERMISSION_GRANTED) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.camera2_container, Camera2BasicFragment.newInstance())
                    .commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int hasCameraPermission = checkPermission(Manifest.permission.CAMERA, android.os.Process.myPid(), android.os.Process.myUid());
        int hasAudioPermission = checkPermission(Manifest.permission.RECORD_AUDIO, android.os.Process.myPid(), android.os.Process.myUid());

        LogUtils.i(TAG, "onActivityResult, hasCameraPermission:"+hasCameraPermission+", hasAudioPermission:"+hasAudioPermission);
    }
}
