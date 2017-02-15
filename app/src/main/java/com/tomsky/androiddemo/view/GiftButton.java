package com.tomsky.androiddemo.view;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.tomsky.androiddemo.util.LogUtils;
import com.tomsky.androiddemo.util.UIUtils;

/**
 * Created by j-wangzhitao on 17-2-15.
 */

public class GiftButton extends View {

    private static final String TAG = "rotation";
    private int currentOrientation = -1;

    private RelativeLayout.LayoutParams layoutParams;
    public GiftButton(Context context) {
        this(context, null);
    }

    public GiftButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        currentOrientation = getResources().getConfiguration().orientation;
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LogUtils.d(TAG, "gift button, orientation:"+newConfig.orientation);

        int newOrientation = newConfig.orientation;
        if (currentOrientation != newOrientation) {
            if (newOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                layoutParams = new RelativeLayout.LayoutParams(UIUtils.dp2px(50), UIUtils.dp2px(50));
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
                setLayoutParams(layoutParams);
            } else if (newOrientation == Configuration.ORIENTATION_PORTRAIT) {
                layoutParams = new RelativeLayout.LayoutParams(UIUtils.dp2px(50), UIUtils.dp2px(50));

                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                setLayoutParams(layoutParams);
            }

        }
        currentOrientation = newConfig.orientation;
    }
}
