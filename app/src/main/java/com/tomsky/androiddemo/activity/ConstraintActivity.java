package com.tomsky.androiddemo.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.tomsky.androiddemo.BaseApplication;
import com.tomsky.androiddemo.R;
import com.tomsky.androiddemo.util.UIUtils;
import com.tomsky.androiddemo.util.WeakHandler;
import com.tomsky.androiddemo.widget.FloatArrayEvaluator;

/**
 * Created by j-wangzhitao on 17-1-12.
 */

public class ConstraintActivity extends Activity implements WeakHandler.IHandler {

    private ConstraintLayout mConstraintLayout;

    private View mAnimRootView;
    private View mAnimIconView;

    private Animation firstAnim;
    private Animation secondAnim;
    private Animation exitAnim;

    private WeakHandler mHandler = new WeakHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constraint);

        TextView bannerView = (TextView) findViewById(R.id.test_banner);
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) bannerView.getLayoutParams();
        lp.dimensionRatio = "H,16:6";

        mConstraintLayout = findViewById(R.id.test_layout1);

        findViewById(R.id.test_pbtn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams lpp = mConstraintLayout.getLayoutParams();
                Log.d("wzt-con", "instance of:"+lpp);
            }
        });

        mAnimRootView = findViewById(R.id.anim_root);
        mAnimIconView = findViewById(R.id.anim_iv);

        findViewById(R.id.bt_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAnimation();
            }
        });

        firstAnim = AnimationUtils.loadAnimation(BaseApplication.getContext(), R.anim.left_gift_anim_1);
        secondAnim = AnimationUtils.loadAnimation(BaseApplication.getContext(), R.anim.left_gift_anim_2);
        exitAnim = AnimationUtils.loadAnimation(BaseApplication.getContext(), R.anim.left_gift_anim_4);



        firstAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mAnimIconView.clearAnimation();
                mAnimIconView.setVisibility(View.VISIBLE);
                mAnimIconView.startAnimation(secondAnim);
                mHandler.sendEmptyMessageDelayed(100, 3000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        exitAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mAnimRootView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void doAnimation() {
        mAnimRootView.setVisibility(View.VISIBLE);
        mAnimIconView.setVisibility(View.INVISIBLE);
        mAnimRootView.startAnimation(firstAnim);
    }

    private void doAnim() {
        mAnimIconView.setVisibility(View.INVISIBLE);
        AnimatorSet amSet = new AnimatorSet();
        ObjectAnimator bgAnimator = ObjectAnimator.ofFloat(mAnimRootView, "translationX", -UIUtils.dp2px(210), 0);
        bgAnimator.setDuration(3000);

        ObjectAnimator iconAnimator = ObjectAnimator.ofFloat(mAnimIconView, "translationX", -UIUtils.dp2px(160), 0);
        iconAnimator.setDuration(1000);
        iconAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mAnimIconView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        ValueAnimator pauseAnim = ValueAnimator.ofInt(0, 1);
        pauseAnim.setDuration(3000);

        float[] inParms = {0, 1};
        float[] endParms = {-UIUtils.dp2px(50), 0};
        ValueAnimator outAnimator = ValueAnimator.ofObject(new FloatArrayEvaluator(), inParms, endParms);
        outAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float[] values = (float[]) animation.getAnimatedValue();
                mAnimRootView.setTranslationY(values[0]);
                mAnimRootView.setAlpha(values[1]);
            }
        });
        outAnimator.setDuration(1000);

        amSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mAnimRootView.setVisibility(View.VISIBLE);
                mAnimRootView.setTranslationY(0);
                mAnimRootView.setAlpha(1);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimRootView.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mAnimRootView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        amSet.playSequentially(bgAnimator, iconAnimator, pauseAnim, outAnimator);
        amSet.start();

    }

    @Override
    public void handleMessage(Message msg) {
        if (msg.what == 100) {
            mAnimRootView.clearAnimation();
            mAnimRootView.startAnimation(exitAnim);
        }
    }
}
