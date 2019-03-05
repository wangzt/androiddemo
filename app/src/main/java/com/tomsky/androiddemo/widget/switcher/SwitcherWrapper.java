package com.tomsky.androiddemo.widget.switcher;

import android.os.Message;

import com.tomsky.androiddemo.util.WeakHandler;

public class SwitcherWrapper implements WeakHandler.IHandler {

    private static final int SHOW_NEXT = 100;
    private boolean mIsPause = true; // 是否暂停
    private int mDuration = 2000; // 滚动间隔，默认为2s
    private ISwitcher mSwitcher; // 要滚动的view

    private WeakHandler mHandler = new WeakHandler(this);

    public SwitcherWrapper() {
    }

    public SwitcherWrapper setSwitcher(ISwitcher switcher) {
        this.mSwitcher = switcher;
        return this;
    }

    public SwitcherWrapper setDuration(int duration) {
        this.mDuration = duration;
        return this;
    }

    public void pause() {
        this.mIsPause = true;
        mHandler.removeMessages(SHOW_NEXT);
    }

    public boolean isPause() {
        return mIsPause;
    }

    public void start() {
        this.mIsPause = false;
        if (mSwitcher != null) {
            mHandler.removeMessages(SHOW_NEXT);
            mHandler.sendEmptyMessageDelayed(SHOW_NEXT, mDuration);
        }
    }

    @Override
    public void handleMessage(Message msg) {
        if (msg.what == SHOW_NEXT && !mIsPause) {
            if (mSwitcher != null) {
                mSwitcher.next();
            }
            mHandler.removeMessages(SHOW_NEXT);
            mHandler.sendEmptyMessageDelayed(SHOW_NEXT, mDuration);
        }
    }
}
