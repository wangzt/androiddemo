package com.tomsky.androiddemo.animation.evaluator;

/**
 * Created by j-wangzhitao on 17-12-19.
 */

public class ScalePos {
    public float scale;
    public float x;
    public float y;

    public ScalePos(float scale, float x, float y) {
        this.scale = scale;
        this.x = x;
        this.y = y;
    }

    public void set(float scale, float x, float y) {
        this.scale = scale;
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "ScalePos{" +
                "scale=" + scale +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
