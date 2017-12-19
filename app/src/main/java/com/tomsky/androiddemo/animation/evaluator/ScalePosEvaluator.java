package com.tomsky.androiddemo.animation.evaluator;

import android.animation.TypeEvaluator;

/**
 * Created by j-wangzhitao on 17-12-19.
 */

public class ScalePosEvaluator implements TypeEvaluator<ScalePos> {

    private ScalePos mScalePos = new ScalePos(1, 0, 0);

    @Override
    public ScalePos evaluate(float fraction, ScalePos startValue, ScalePos endValue) {
        float scale = startValue.scale + (endValue.scale - startValue.scale) * fraction;
        float x = startValue.x + (endValue.x - startValue.x) * fraction;
        float y = startValue.y + (endValue.y - startValue.y) * fraction;
        mScalePos.set(scale, x, y);

        return mScalePos;
    }
}
