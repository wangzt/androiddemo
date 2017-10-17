package com.tomsky.androiddemo;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.tomsky.androiddemo.service.IUploadCallback;
import com.tomsky.androiddemo.service.IUploadInterface;
import com.tomsky.androiddemo.util.LogUtils;

/**
 * Created by j-wangzhitao on 17-9-20.
 */

public class UploadService extends Service {

    private RemoteCallbackList<IUploadCallback> mCallbackList = new RemoteCallbackList<>();

    private final IUploadInterface.Stub mUploadManager = new IUploadInterface.Stub() {

        @Override
        public void startUpload(Bundle bundle) throws RemoteException {
            LogUtils.e("AIDLActivity", "---startUpload");
            new Thread(new UploadThread(bundle)).start();
        }

        @Override
        public void registerCallback(IUploadCallback callback) throws RemoteException {
            mCallbackList.register(callback);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mUploadManager;
    }

    private class UploadThread implements Runnable {
        private String key;
        private int status;
        public UploadThread(Bundle bundle) {
            key = bundle.getString("key");
            status = bundle.getInt("status");
        }
        @Override
        public void run() {
            try {
                Thread.sleep(5000);
                Bundle bundle = new Bundle();
                bundle.putString("ack", key);
                bundle.putInt("st", status);
                LogUtils.e("AIDLActivity", "---onServer callback-----");
                callback(bundle);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void callback(Bundle bundle) throws RemoteException {
        final int N = mCallbackList.beginBroadcast();

        for (int i = 0; i < N; i++) {
            IUploadCallback l = mCallbackList.getBroadcastItem(i);
            if (l != null) {
                l.uploadCallback(bundle);
            }
        }

        mCallbackList.finishBroadcast();
    }

    private void test2() {
        LogUtils.d("AIDLActivity", "--------------test2");
    }
}
