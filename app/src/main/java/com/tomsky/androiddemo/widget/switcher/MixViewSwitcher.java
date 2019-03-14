package com.tomsky.androiddemo.widget.switcher;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import com.tomsky.androiddemo.R;

public class MixViewSwitcher extends ViewFlipper {

    private int animIn = R.anim.fade_in_slide_in;
    private int animOut = R.anim.fade_out_slide_out;

    public MixViewSwitcher(Context context) {
        super(context);
        init(context);
    }

    public MixViewSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setInAnimation(AnimationUtils.loadAnimation(context.getApplicationContext(), animIn));
        setOutAnimation(AnimationUtils.loadAnimation(context.getApplicationContext(), animOut));
    }

}
