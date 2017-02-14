package com.huajiao.utils;

import android.content.Context;

/**
 * Created by wangzhitao on 2/17/16.
 */
public class Security {
    static {
        System.loadLibrary("hjsecurity");
    }

    public native static String init(Context context, ExtraInfo input, String sec);
    public native static String convertKey(String input);
    public native static String decode(String input, String key);
}
