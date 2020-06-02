package com.tomsky.androiddemo.util;

import android.util.Log;

import com.tomsky.androiddemo.BuildConfig;

/**
 * Created by j-wangzhitao on 17-2-14.
 */

public class LogUtils {
    public static final boolean DEBUG = BuildConfig.DEBUG;

    public static void i(String tag, String msg) {
        if (DEBUG) {
            Log.i(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (DEBUG) {
            Log.e(tag, msg);
        }
    }
}
