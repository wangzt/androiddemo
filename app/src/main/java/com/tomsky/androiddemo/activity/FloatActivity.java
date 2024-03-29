package com.tomsky.androiddemo.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.tomsky.androiddemo.R;
import com.tomsky.androiddemo.service.FloatWindowService;
import com.tomsky.androiddemo.util.LogUtils;

/**
 * Created by wangzhitao on 2020/06/05
 **/
public class FloatActivity extends FragmentActivity {
    private static final String TAG = "FloatActivity";
    private static final int OVERLAY_PERMISSION_REQ_CODE = 1010;

    public static final String EXTRA_NAME = "extra_name";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_float);

        Intent intent = new Intent(getApplicationContext(), FloatWindowService.class);//开启服务显示悬浮框
        stopService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.e(TAG, "---onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.e(TAG, "---onPause");
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.e(TAG, "---onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.e(TAG, "---onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtils.e(TAG, "---onRestart");

//        unbindService(mVideoServiceConnection); // 不显示悬浮窗
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if(Build.VERSION.SDK_INT>=23) {
                if (!Settings.canDrawOverlays(this)) {
                    Toast.makeText(this, "权限授予失败，无法开启悬浮窗", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "权限授予成功！", Toast.LENGTH_SHORT).show();
                    //有悬浮窗权限开启服务绑定 绑定权限
                    startFloatService();
                }
            }
        }
    }


    public void minimize(View view) {
//            WindowManager.LayoutParams lp = getWindow().getAttributes();
//            lp.alpha = 0.1f;
//            lp.width = 10;
//            lp.height = 10;
//            lp.dimAmount = 0;
//            lp.flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS |
//                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
//            getWindow().setAttributes(lp);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

            tryToMinimize();
    }

    private void startFloatService() {
//        moveTaskToBack(true);

        finish();

        Intent intent = new Intent(getApplicationContext(), FloatWindowService.class);//开启服务显示悬浮框
        intent.putExtra(EXTRA_NAME, "float activity");
        startService(intent);
//        bindService(intent, mVideoServiceConnection, Context.BIND_AUTO_CREATE);
    }

    //判断权限
    private void tryToMinimize() {

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            if (Settings.canDrawOverlays(this)) {
                //有悬浮窗权限开启服务绑定 绑定权限
//                Intent intent = new Intent(this, MyService.class);
//                startService(intent);

                startFloatService();
            } else {
                //没有悬浮窗权限m,去开启悬浮窗权限
                try {
                    Intent  intent=new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else { // 6.0以下机型不支持
            // 默认有悬浮窗权限  但是 华为, 小米,oppo 等手机会有自己的一套 Android6.0 以下
            // 会有自己的一套悬浮窗权限管理 也需要做适配

            Toast.makeText(this, "Your system is bellow android 6.0, not support minimize!", Toast.LENGTH_LONG).show();
//            startFloatService();
        }
    }

    ServiceConnection mVideoServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 获取服务的操作对象
            FloatWindowService.MyBinder binder = (FloatWindowService.MyBinder) service;
            binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };
}
