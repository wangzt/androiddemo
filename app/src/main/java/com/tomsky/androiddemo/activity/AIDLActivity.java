package com.tomsky.androiddemo.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.tomsky.androiddemo.R;
import com.tomsky.androiddemo.service.IUploadCallback;
import com.tomsky.androiddemo.service.IUploadInterface;
import com.tomsky.androiddemo.util.LogUtils;

/**
 * Created by j-wangzhitao on 17-9-20.
 */

public class AIDLActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AIDLActivity";

    private IUploadInterface mUploadInterface;
    private boolean mBound = false;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                service.linkToDeath(mDeathRecipient, 0);
                mUploadInterface = IUploadInterface.Stub.asInterface(service);
                mUploadInterface.registerCallback(mCallback);
            } catch (Exception e) {
                e.printStackTrace();
            }

            LogUtils.e(TAG, "---onServiceConnected");
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtils.e(TAG, "---onServiceDisconnected");
            mBound = false;
        }
    };

    private IUploadCallback mCallback = new IUploadCallback.Stub() {

        @Override
        public void uploadCallback(Bundle bundle) throws RemoteException {
            String key = bundle.getString("ack");
            int status = bundle.getInt("st");
            LogUtils.e(TAG, "uploadCallback, status:"+status+", msg:"+key);
        }
    };

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (mUploadInterface == null) return;

            mUploadInterface.asBinder().unlinkToDeath(mDeathRecipient, 0);
            mUploadInterface = null;

            // 重新绑定远程服务
            attempBindService();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl);

        findViewById(R.id.btn_service).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_service:
                tryUpload();
                break;
        }
    }

    private void tryUpload() {
        if (!mBound) {
            attempBindService();
            LogUtils.e(TAG, "当前与服务端处于未连接状态，正在尝试重连，请稍后再试");
        }
        if (mUploadInterface == null) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString("key", "hello world");
        bundle.putInt("status", 1);
        try {
            mUploadInterface.startUpload(bundle);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService();
    }

    private void attempBindService() {
        Intent intent = new Intent();
        intent.setPackage("com.tomsky.androiddemo");
        intent.setAction("com.tomsky.androiddemo.upload");
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void stopService() {
        if (mBound) {
            unbindService(mServiceConnection);
            mBound = false;
        }
    }

}
