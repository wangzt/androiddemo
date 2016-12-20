package com.tomsky.androiddemo.activity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.tomsky.androiddemo.R;
import com.tomsky.androiddemo.util.BitmapUtils;
import com.tomsky.androiddemo.util.FileUtils;
import com.tomsky.androiddemo.util.GifRecorder;
import com.tomsky.androiddemo.util.WeakHandler;
import com.tomsky.androiddemo.view.ENLoadingView;
import com.tomsky.androiddemo.view.ENSearchView;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by j-wangzhitao on 16-12-20.
 */

public class DrawingCacheActivity extends FragmentActivity implements View.OnClickListener, WeakHandler.IHandler {

    private View mContainerView;
    private ENSearchView mSearchView;
    private ENLoadingView mLoadingView;
    private Button mStartAnimBtn;

    private WeakHandler mHandler = new WeakHandler(this);
    private static final int MSG_CAPTURE = 100;
    private static final int CAPTURE_DELAY = 200; // 200ms一次
    private static final int MSG_ENCODE = 200;

    private static final int MAX_FRAME = 5;
    private AtomicInteger mCaptureCount = new AtomicInteger(0);

    private GifRecorder gifRecorder;

    private float scale = 1f;

    private Bitmap bgBmp, fgBmp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_drawingcache);

        mContainerView = findViewById(R.id.drawing_cache_layout);
        mSearchView = (ENSearchView) findViewById(R.id.view_search);
        mLoadingView = (ENLoadingView) findViewById(R.id.view_loading);
        mStartAnimBtn = (Button) findViewById(R.id.btn_anim);

        gifRecorder = new GifRecorder();

        mLoadingView.setCaptureCallback(new ENLoadingView.CaptureCallback() {
            @Override
            public void loadingCaptured(Bitmap bitmap) {
                if (bitmap != null && !bitmap.isRecycled()) {
                    fgBmp = bitmap;
                    int count = mCaptureCount.get();
                    if (count < MAX_FRAME) {
                        new Thread() {
                            @Override
                            public void run() {
                                if (bgBmp != null && fgBmp != null) {
                                    Log.d("wzt-gif", "========================capture, count:"+mCaptureCount.get());
                                    try {
                                        Bitmap bmp = fgBmp;
                                        if (scale < 1) {
                                            bmp = Bitmap.createScaledBitmap(fgBmp, (int)(fgBmp.getWidth()*scale), (int) (fgBmp.getHeight()*scale), true);
                                        }
                                        int dstWidth = bgBmp.getWidth();
                                        int dstHeight = bgBmp.getHeight();
                                        Bitmap newbmp = Bitmap
                                                .createBitmap(dstWidth, dstHeight, Bitmap.Config.ARGB_8888);
                                        Canvas cv = new Canvas(newbmp);
                                        cv.drawBitmap(bgBmp, 0, 0, null);
                                        cv.drawBitmap(bmp, 200, 400, null);
                                        // save all clip
                                        cv.save(Canvas.ALL_SAVE_FLAG);// 保存
                                        // store
                                        cv.restore();// 存储
                                        gifRecorder.onImageAvailable(newbmp);
                                        if (mCaptureCount.get() == 1) {
                                            mHandler.sendEmptyMessage(MSG_ENCODE);
                                        }
                                        mCaptureCount.addAndGet(1);
                                        mHandler.sendEmptyMessage(MSG_CAPTURE);
//                                        mHandler.sendEmptyMessageDelayed(MSG_CAPTURE, CAPTURE_DELAY);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }.start();

//                        String path = FileUtils.getCapturePath() + "capture_load_"+count+".jpg";
//                        File out = new File(path);
//                        BitmapUtils.writeToFile(bitmap, out, 100, false);
//                        mCaptureCount.addAndGet(1);
//                        mHandler.sendEmptyMessageDelayed(MSG_CAPTURE, CAPTURE_DELAY);
                    }
//                    else {
//                        gifRecorder.stop();
//                    }
                }
            }
        });
        mStartAnimBtn.setOnClickListener(this);

        findViewById(R.id.btn_capture).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_anim:
                mSearchView.start();
                if (mLoadingView.isShow()) {
                    mLoadingView.hide();
                } else {
                    mLoadingView.show();
                }
                break;

            case R.id.btn_capture:
                if (mCaptureCount.get() > 0) {
                    return;
                }
                mContainerView.setDrawingCacheEnabled(true);
                mHandler.sendEmptyMessage(MSG_CAPTURE);

                break;
        }
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_CAPTURE:
                int count = mCaptureCount.get();
                Log.d("wzt-gif", "--capture, count:"+count);
                if (count < MAX_FRAME) {
                    Bitmap bmp = mContainerView.getDrawingCache();
                    if (bmp != null) {
                        if (bmp.getWidth() > 720) {
                            scale = 720f / bmp.getWidth();
                            Bitmap bitmap = gifRecorder.scaleBitmap(bmp);
                            bgBmp = bitmap;
                        } else {
                            Bitmap bitmap = Bitmap.createBitmap(bmp);
                            bgBmp = bitmap;
//                            gifRecorder.onImageAvailable(bitmap);
                        }
                        mLoadingView.capture();

                    }

//                    String path = FileUtils.getCapturePath() + "capture_"+count+".jpg";
//                    File out = new File(path);
//                    if (bmp != null) {
//                        Log.d("wzt-capture", "no scaled, width:"+bmp.getWidth()+", height:"+bmp.getHeight() + ", path:"+path);
//                    }
//                    BitmapUtils.writeToFile(bmp, out, 80, false);

                    mContainerView.destroyDrawingCache();

//
//                    if (count == 1) {
//                        mHandler.sendEmptyMessage(MSG_ENCODE);
//                    }
//                    mHandler.sendEmptyMessageDelayed(MSG_CAPTURE, CAPTURE_DELAY);
                } else {
                    Log.d("wzt-gif", "--capture, stop, count:"+count);
                    mCaptureCount.set(0);
                    mContainerView.setDrawingCacheEnabled(false);
                }

                break;

            case MSG_ENCODE:
                gifRecorder.start();
                break;
        }
    }
}
