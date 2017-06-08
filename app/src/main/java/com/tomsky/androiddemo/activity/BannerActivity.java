package com.tomsky.androiddemo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tomsky.androiddemo.R;
import com.tomsky.androiddemo.view.banner.BannerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by j-wangzhitao on 17-5-16.
 */

public class BannerActivity extends Activity {

    private BannerView mBannerView;

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBannerView != null) {
            mBannerView.stopLoop();
        }
    }
}
