package com.tomsky.androiddemo.activity;

import android.animation.IntArrayEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.tomsky.androiddemo.R;
import com.tomsky.androiddemo.view.ClipView;
import com.tomsky.androiddemo.view.SimpleRatingBar;

/**
 * Created by j-wangzhitao on 17-1-12.
 */

public class ConstraintActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constraint);

        TextView bannerView = (TextView) findViewById(R.id.test_banner);
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) bannerView.getLayoutParams();
        lp.dimensionRatio = "H,16:6";
    }
}
