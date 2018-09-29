package com.tomsky.androiddemo.util;

import android.content.Context;
import android.util.DisplayMetrics;

import com.tomsky.androiddemo.BaseApplication;

/**
 * @author Jungly
 * jungly.ik@gmail.com
 * 15/3/8 10:07
 */
public class UIUtils {

    public static int px2sp(float pxValue) {
        final float fontScale = BaseApplication.getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int dp2px(int dp) {
        DisplayMetrics displayMetrics = BaseApplication.getContext().getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + 0.5);
    }

    public static int getStatusBarHeight() {
        int height = 0;
        int resourceId = BaseApplication.getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = BaseApplication.getContext().getResources().getDimensionPixelSize(resourceId);
        }

        return height;
    }

}
