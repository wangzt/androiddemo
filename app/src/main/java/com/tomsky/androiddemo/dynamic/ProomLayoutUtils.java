package com.tomsky.androiddemo.dynamic;

import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.tomsky.androiddemo.BaseApplication;

public class ProomLayoutUtils {
    private static int BASE_WIDTH = 375;
    private static float scale = -1;

    public static int scaleSize(float size) {
        if (scale < 0) {
            DisplayMetrics metrics = BaseApplication.getContext().getResources().getDisplayMetrics();
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;
            if (width > height) {
                width = height;
            }
            scale = (width * 1.0f)/BASE_WIDTH;
        }
        return (int) (scale * size);
    }

    public static int parseColor(String colorString, float alpha) {
        if (TextUtils.isEmpty(colorString) || colorString.length() != 6) {
            throw new IllegalArgumentException("Unknown color");
        }
        long color = Long.parseLong(colorString, 16);

        if (alpha < 0 || alpha > 1) {
            color |= 0x00000000ff000000;
        } else {
            color |= (long)(alpha * 255) << 24;
        }
        return (int)color;
    }

    public static boolean parseBoolean(String str, boolean defaultBoolean) {
        if (TextUtils.isEmpty(str)) {
            return defaultBoolean;
        }

        if (!"0".equals(str) && !"false".equalsIgnoreCase(str)) {
            return true;
        }

        return false;
    }
}
