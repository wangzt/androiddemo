package com.tomsky.androiddemo;

import android.view.View;

/**
 * 防止重复点击事件
 */
public abstract class NoDoubleClickListener implements View.OnClickListener {

    private long delayTime = 1000; // 默认延迟1s

    private long lastClickTime = 0;

    public NoDoubleClickListener() {
    }

    public NoDoubleClickListener(long delayTime) {
        this.delayTime = delayTime;
    }

    @Override
    public void onClick(View v) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime > delayTime) {
            lastClickTime = currentTime;
            onNoDoubleClick(v);
        }
    }

    public abstract void onNoDoubleClick(View v);
}
