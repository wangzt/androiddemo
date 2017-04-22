package com.tomsky.androiddemo.activity;

import android.animation.FloatArrayEvaluator;
import android.animation.IntArrayEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.tomsky.androiddemo.R;
import com.tomsky.androiddemo.view.ClipView;

/**
 * Created by j-wangzhitao on 17-1-12.
 */

public class AndroidUIActivity extends Activity {

    private ClipView mClipView;

    private int left = 0;
    private int right = 0;

    private boolean start = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_ui);
        mClipView = (ClipView) findViewById(R.id.my_clip_view);

        findViewById(R.id.btn_start_anim).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int start[] = {0, 10000};
                int center[] = {0, 0};
                int center2[] = {0, 0};
                int end[] = {10000, 0};

                ValueAnimator valueAnimator = ValueAnimator.ofObject(new IntArrayEvaluator(), start, center, center2, end);
                valueAnimator.setDuration(2000);
                valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int []value = (int[]) animation.getAnimatedValue();

                        mClipView.setClipPadding(value[0], value[1]);
                    }
                });
                valueAnimator.start();
            }
        });
    }
}
