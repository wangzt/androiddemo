package com.tomsky.androiddemo.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tomsky.androiddemo.R;
import com.tomsky.androiddemo.view.banner.BannerView;
import com.tomsky.androiddemo.widget.switcher.ISwitcher;
import com.tomsky.androiddemo.widget.switcher.MixViewSwitcher;
import com.tomsky.androiddemo.widget.switcher.SwitcherWrapper;
import com.tomsky.androiddemo.widget.switcher.TextViewSwitcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by j-wangzhitao on 17-5-16.
 */

public class BannerActivity extends Activity {

    private BannerView mBannerView;

    private SwitcherWrapper mSwitcherWrapper = new SwitcherWrapper();
    private TextViewSwitcher mTextSwitcher;

    private List<String> mList1 = new ArrayList<>();
    private List<String> mList2 = new ArrayList<>();

    private boolean first = true;


    private Button mMixBtn;
    private Button mMixResetBtn;
    private MixViewSwitcher mMixViewSwitcher;
    private boolean mixFirst = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);

        mBannerView = (BannerView) findViewById(R.id.banner_view);

        List<String> items = new ArrayList<String>();
        for (int i = 1; i < 3; i++) {
            items.add(String.valueOf(i));
        }
        mBannerView.setData(items);

        initData();

        mTextSwitcher = findViewById(R.id.text_switcher);
        mTextSwitcher.setTextList(mList1);
        first = true;
        mTextSwitcher.setSwitcherClickListener(new ISwitcher.SwitcherClickListener() {
            @Override
            public void onItemClick(int pos) {
                Log.d("wzt-switcher", "onClick pos:" +pos);
            }
        });

        mSwitcherWrapper.setSwitcher(mTextSwitcher);

        final Button btn1 = findViewById(R.id.btn_switcher);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSwitcherWrapper.isPause()) {
                    mSwitcherWrapper.start();
                    btn1.setText("滚动开");
                } else {
                    mSwitcherWrapper.pause();
                    btn1.setText("滚动关");
                }
            }
        });

        final Button btn2 = findViewById(R.id.btn_switcher_reset);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwitcherWrapper.pause();
                btn1.setText("滚动关");
                if (first) {
                    first = false;
                    mTextSwitcher.setTextList(mList2);
                    btn2.setText("列表2");
                } else {
                    first = true;
                    mTextSwitcher.setTextList(mList1);
                    btn2.setText("列表1");
                }
                int size = mTextSwitcher.size();
                if (size > 0) {
                    btn1.setText("滚动开");
                    if (size > 1) {
                        mSwitcherWrapper.start();
                    } else {
                        mTextSwitcher.next();
                    }
                }
            }
        });


        mMixViewSwitcher = findViewById(R.id.mix_switcher);
        initMixData(Color.GREEN, 0, 0);
        mMixBtn = findViewById(R.id.mix_switcher_btn);
        mMixBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMixViewSwitcher.isFlipping()) {
                    mMixViewSwitcher.stopFlipping();
                    mMixBtn.setText("滚动关");
                } else {
                    mMixViewSwitcher.startFlipping();
                    mMixBtn.setText("滚动开");
                }
            }
        });

        mMixResetBtn = findViewById(R.id.mix_switcher_reset);
        mMixResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMixViewSwitcher.stopFlipping();
                mMixViewSwitcher.removeAllViews();
                if (mixFirst) {
                    initMixData(Color.RED, 2, 1);
                } else {
                    initMixData(Color.GREEN, 0, 0);
                }
                mixFirst = !mixFirst;

                if (mMixViewSwitcher.getChildCount() > 1) {
                    mMixViewSwitcher.startFlipping();
                }
            }
        });
    }

    private void initMixData(int color, int textSize, int listIndex) {
        View view1 = new View(BannerActivity.this);
        view1.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        view1.setBackgroundColor(color);
        mMixViewSwitcher.addView(view1);

        for (int i = 0; i < textSize; i++) {
            TextView view2 = new TextView(BannerActivity.this);
            view2.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            view2.setText(listIndex+"--我是文字"+i);
            mMixViewSwitcher.addView(view2);
        }
    }

    private void initData() {
        for (int i = 0; i < 2; i++) {
            mList1.add("队列一我是第 "+i+" 个");
        }

        for (int i = 0; i < 3; i++) {
            mList2.add("队列二我是第 "+i+" 个");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBannerView != null) {
            mBannerView.stopLoop();
        }
    }
}
