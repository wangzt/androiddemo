package com.tomsky.androiddemo.activity;

import android.animation.FloatArrayEvaluator;
import android.animation.IntArrayEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.androidadvance.topsnackbar.TSnackbar;
import com.tencent.mmkv.MMKV;
import com.tomsky.androiddemo.R;
import com.tomsky.androiddemo.view.CircleProgressBar;
import com.tomsky.androiddemo.view.ClipView;
import com.tomsky.androiddemo.view.SimpleRatingBar;

/**
 * Created by j-wangzhitao on 17-1-12.
 */

public class AndroidUIActivity extends AppCompatActivity {

    private ClipView mClipView;

    private SimpleRatingBar mRatingBar;

    private int left = 0;
    private int right = 0;

    private boolean start = false;

    private int mRate = 0;

    private ImageView mBreathView;
    private Animation mAnimation;

    private CircleProgressBar mProgressBar;

    private ClipDrawable mClipDrawable;
    private View mRootLayout;

    private boolean isAnimStart = false;

    private int level = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_ui);
        mClipView = (ClipView) findViewById(R.id.my_clip_view);
        mRatingBar = (SimpleRatingBar) findViewById(R.id.ratingBar1);

        mRootLayout = findViewById(R.id.my_ui_layout);

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

                mRatingBar.setRating(4.5f);
            }
        });

        mRatingBar.setOnRatingBarChangeListener(new SimpleRatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(SimpleRatingBar simpleRatingBar, float rating, boolean fromUser) {
                Log.d("wzt-rate", "---------new: rate:"+rating+", oldRate:"+mRate);
                int newRate = (int) rating;
                if (newRate == 0) {
                    mRatingBar.setRating(1);
                    return;
                }
                if (newRate > 1 && newRate == mRate) {
                    mRatingBar.setRating(newRate - 1);
                } else {
                    mRate = newRate;
                }
            }
        });

        mBreathView = (ImageView) findViewById(R.id.proom_cover_breath_light);
        mAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.breath_anim);

        findViewById(R.id.anim_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (isAnimStart) {
//                    isAnimStart = false;
//                    if (mBreathView != null) {
//                        mBreathView.clearAnimation();
//                    }
//                } else {
//                    isAnimStart = true;
//                    if (mBreathView != null && mAnimation != null) {
//                        mBreathView.startAnimation(mAnimation);
//                    }
//                }

                Log.d("wzt-clip", "click, clipDrawable:"+mClipDrawable+", level:"+level);
                if (mClipDrawable != null) {
                    if (level > 10000) {
                        level = 0;
                    } else {
                        level +=1000;
                    }
                    mClipDrawable.setLevel(level);
                }
            }
        });

        mProgressBar = (CircleProgressBar) findViewById(R.id.my_circle_bar);
        mProgressBar.setMax(100);

        findViewById(R.id.snackbar_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Snackbar snackbar = Snackbar.make(mRootLayout, "My first snack bar", Snackbar.LENGTH_LONG);
//                snackbar.show();

//                TSnackbar tSnackbar = TSnackbar.make(mRootLayout, "TSnackBar test", TSnackbar.LENGTH_LONG);
//                tSnackbar.show();
                mProgressBar.setProgress((int) (Math.random() * 100));
            }
        });

        MMKV kv = MMKV.defaultMMKV();
        kv.encode("name", "tomsky");
        Log.d("wzt-kv", "name:"+kv.decodeString("name"));

        ImageView clipView  = findViewById(R.id.my_clip_image);
        Drawable drawable = clipView.getDrawable();
        if (drawable instanceof LayerDrawable) {
            Drawable clipDrawable = ((LayerDrawable)drawable).getDrawable(0);
            if (clipDrawable instanceof ClipDrawable) {
                mClipDrawable = (ClipDrawable)clipDrawable;
            }
        }
    }
}
