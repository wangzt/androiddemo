package com.tomsky.androiddemo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.tomsky.androiddemo.R;
import com.tomsky.androiddemo.util.WeakHandler;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;


public class QueueActivity extends Activity implements WeakHandler.IHandler {
    private static final String TAG = "test-queue";

    private LinkedList<String> mQueue = new LinkedList<>();


    private int[] mCount = new int[4];
    private String[] mType = {"A-", "B-", "C-", "D-"};
    private int mTypeIndex = 0;
    private AtomicBoolean[] mRunning = {new AtomicBoolean(false), new AtomicBoolean(false), new AtomicBoolean(false), new AtomicBoolean(false)};

    private static final int MSG_NEXT = 100;
    private static final int MSG_TIMEOUT = 200;
    private static final int TIMEOUT = 30*1000;

    private WeakHandler mHandler = new WeakHandler(this);

    private LinkedList<String> mStopList = new LinkedList<>();
    private String mCurrentMsg;
    private AtomicBoolean mStop = new AtomicBoolean(false);


    private boolean mIsVisibile = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);

        findViewById(R.id.queue_test_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product();

            }
        });

        findViewById(R.id.queue_visible_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsVisibile) {
                    mIsVisibile = false;
                } else {
                    mIsVisibile = true;
                }
            }
        });
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_NEXT:
                mHandler.removeMessages(MSG_NEXT);
                mHandler.removeMessages(MSG_TIMEOUT);
                consume();
                break;
            case MSG_TIMEOUT:
                Log.d(TAG, "TIMEOUT, mStop:"+mStop.get());
                mHandler.removeMessages(MSG_TIMEOUT);
                if (!mStop.get()) {
                    mHandler.sendEmptyMessage(MSG_NEXT);
                }
                break;
        }
    }

    public void clear() {
        mQueue.clear();
    }

    private void product() {
        if (mTypeIndex > 3) {
            mTypeIndex = 0;
        }
        mCount[mTypeIndex]++;

        String msg = mType[mTypeIndex]+mCount[mTypeIndex];
        mQueue.add(msg);
        mTypeIndex++;
        Log.d(TAG, "add msg "+msg);

        consume();
    }

    private void consume() {
        for (int i = 0; i < 4; i++) {
            if (mRunning[i].get()) {
                return;
            }
        }

        int type = 0;
        int time = 3000;
        String msg = mQueue.poll();
        if (TextUtils.isEmpty(msg)) return;

        if (msg.startsWith("A-")) {
            time = 3000;
            type = 0;
        } else if (msg.startsWith("B-")) {
            time = 5000;
            type = 1;
        } else if (msg.startsWith("C-")) {
            time = 4000;
            type = 2;
        } else {
            time = 6000;
            type = 3;
        }

        doThread(msg, time, type);
    }

    private void doThread(final  String msg, final int time, final int type) {
        mRunning[type].set(true);
        mCurrentMsg = msg;
        mHandler.sendEmptyMessageDelayed(MSG_TIMEOUT, TIMEOUT);
        new Thread() {
            @Override
            public void run() {
                Log.d(TAG, "---start do " + msg);
                try {
                    Thread.sleep(time);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "###error for "+msg);
                }
                if (mIsVisibile) {
                    Log.d(TAG, "===end do " + msg);
                }
                mRunning[type].set(false);

                boolean sendMsg = true;
                if (mStop.get()) {
                    sendMsg = false;
                }
                if (sendMsg) {
                    mCurrentMsg = null;
                    mHandler.sendEmptyMessage(MSG_NEXT);
                }
            }
        }.start();
    }

    @Override
    protected void onStop() {
        super.onStop();

        boolean isRun = false;
        for (int i = 0; i < 4; i++) {
            if (mRunning[i].get()) {
                isRun = true;
                break;
            }
        }
        if (isRun && !TextUtils.isEmpty(mCurrentMsg)) {
            Log.d(TAG, "*****************stop, msg:"+mCurrentMsg);
            mStopList.add(mCurrentMsg);
        }

        for (int i = 0; i < 4; i++) {
            mRunning[i].set(false);
        }

        mStop.set(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mStopList.size() > 0) {
            Log.d(TAG, "*****************start, msg:"+mCurrentMsg);
            mStopList.clear();
            mHandler.sendEmptyMessage(MSG_NEXT);
        }
        mStop.set(false);
    }


}
