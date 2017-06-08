package com.tomsky.androiddemo.view.banner;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.tomsky.androiddemo.R;
import com.tomsky.androiddemo.util.WeakHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by j-wangzhitao on 17-5-16.
 */

public class BannerView extends RelativeLayout implements ViewPager.OnPageChangeListener, WeakHandler.IHandler {

    private static final String TAG = "wzt-banner";

    private static final int MSG_SHOW_NEXT = 101;
    private static final int DEFAULT_DELAY_TIME = 5000;

    private ViewPager mViewPager;
    private BannerAdapter mBannerAdapter;

    private BannerIndicator mIndicator;

    private boolean mCanCircle = true;

    private boolean mBeCircle = false; // 是否循环，内部使用

    private int mSize;

    private int mCurrentIndex;

    private WeakHandler mHandler = new WeakHandler(this);

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.banner_layout, this, true);
        initViews();
    }

    private void initViews() {
        mViewPager = (ViewPager) findViewById(R.id.banner_view_pager);
        mIndicator = (BannerIndicator) findViewById(R.id.banner_indicator);
        mBannerAdapter = new BannerAdapter();
        mViewPager.setAdapter(mBannerAdapter);
        mViewPager.addOnPageChangeListener(this);
    }

    /**
     * 是否允许循环，默认允许
     * @param canCircle
     */
    public void setCanCircle(boolean canCircle) {
        this.mCanCircle = canCircle;
    }

    public void setData(List<String> datas) {
        if (datas == null) return;

        mCurrentIndex = 0;
        mSize = datas.size();
        mIndicator.setCount(mSize);

        stopLoop();

        if (datas.size() > 1) {
            if (mCanCircle) {
                mBeCircle = true;
                String first = datas.get(0);
                String last = datas.get(datas.size() - 1);
                List<String> circleDatas = new ArrayList<String>();
                circleDatas.add(last);
                circleDatas.addAll(datas);
                circleDatas.add(first);
                mBannerAdapter.setData(circleDatas);
                mViewPager.setCurrentItem(1);
                mCurrentIndex = 1;
                startLoop();
            } else {
                mBeCircle = false;
                mBannerAdapter.setData(datas);
            }
        } else if (datas.size() == 1) { // 不显示indicator，且不可循环
            mBeCircle = false;
            mBannerAdapter.setData(datas);
        }
        mIndicator.setPosition(0);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        int pointPos = position;
        if (mBeCircle) {
            if (position < 1) {
                position = mSize;
                mViewPager.setCurrentItem(position, false);
            } else if (position > mSize) {
                position = 1;
                mViewPager.setCurrentItem(position, false);
            }
            pointPos = position - 1;
        }
        mIndicator.setPosition(pointPos);
        Log.d(TAG, "onPageSelected, position:" +position+", pointPos:"+pointPos);
        mCurrentIndex = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.d(TAG, "-----onPageScrollStateChanged:"+state);
        if (state == ViewPager.SCROLL_STATE_DRAGGING) {
            stopLoop();
        }
    }

    public void showNext() {
        int next = mCurrentIndex + 1;
        if (mBeCircle) {
            if (next < mSize + 2) {
                mCurrentIndex = next;
                mViewPager.setCurrentItem(next);
            }
        } else {
            if (next < mSize) {
                mCurrentIndex = next;
                mViewPager.setCurrentItem(next);
            }
        }
    }

    @Override
    public void handleMessage(Message msg) {
        if (msg.what == MSG_SHOW_NEXT) {
            showNext();
            if (mHandler != null) {
                mHandler.sendEmptyMessageDelayed(MSG_SHOW_NEXT, DEFAULT_DELAY_TIME);
            }
        }
    }

    public void startLoop(){
        if (mHandler != null) {
            mHandler.sendEmptyMessageDelayed(MSG_SHOW_NEXT, DEFAULT_DELAY_TIME);
        }
    }

    public void stopLoop(){
        if (mHandler != null) {
            mHandler.removeMessages(MSG_SHOW_NEXT);
        }

    }
}
