package com.tomsky.androiddemo.util;

import android.util.Log;

/**
 * Created by j-wangzhitao on 17-2-14.
 */

public class LogUtils {
    static final boolean DEBUG = true;

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
