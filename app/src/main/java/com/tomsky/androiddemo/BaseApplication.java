package com.tomsky.androiddemo;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.tencent.mmkv.MMKV;

/**
 * Created by j-wangzhitao on 16-12-20.
 */

public class BaseApplication extends Application {
    private static Application mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        String rootDir = MMKV.initialize(this);
        Log.d("wzt-kv", "kv root: "+rootDir);
        Fresco.initialize(this);
    }

    public static Context getContext() {
        return mContext;
    }
}
