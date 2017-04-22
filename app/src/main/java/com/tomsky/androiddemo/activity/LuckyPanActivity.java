package com.tomsky.androiddemo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tomsky.androiddemo.R;
import com.tomsky.androiddemo.view.LuckyPanView;

import java.util.Random;

/**
 * Created by wangzhitao on 17-4-15.
 */

public class LuckyPanActivity extends Activity {

    private LuckyPanView mLuckyPanView;
    private Button mPlayBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_luckpan);

        mLuckyPanView = (LuckyPanView) findViewById(R.id.view_lucky_pan);
        mPlayBtn = (Button) findViewById(R.id.btn_play);

        mPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLuckyPanView.isPlay()) {
                    mPlayBtn.setText("START");
                    Random random = new Random();
                    int index = random.nextInt(6);
                    mLuckyPanView.stop(index);

                } else {
                    mPlayBtn.setText("STOP");
                    mLuckyPanView.start();

                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLuckyPanView.isPlay()) {
            mLuckyPanView.onDestroy();
        }
    }
}
