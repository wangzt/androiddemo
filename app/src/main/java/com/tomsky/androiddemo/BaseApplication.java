package com.tomsky.androiddemo;

import android.app.Application;
import android.content.Context;

/**
 * Created by j-wangzhitao on 16-12-20.
 */

public class BaseApplication extends Application {
    private static Application mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext() {
        return mContext;
    }
}
