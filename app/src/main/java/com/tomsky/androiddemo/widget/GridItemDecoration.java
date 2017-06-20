package com.tomsky.androiddemo.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by j-wangzhitao on 17-6-20.
 */

public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private int mLeft, mRight, mTop, mBottom;

    public GridItemDecoration(int margin) {
        this.mLeft = margin;
        this.mRight = margin;
        this.mTop = margin;
        this.mBottom = margin;
    }

    public GridItemDecoration(int left, int right, int top, int bottom) {
        this.mLeft = left;
        this.mRight = right;
        this.mTop = top;
        this.mBottom = bottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(mLeft, mRight, mTop, mBottom);
    }
}
