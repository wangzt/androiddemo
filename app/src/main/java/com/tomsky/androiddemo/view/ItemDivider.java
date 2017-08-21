package com.tomsky.androiddemo.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by j-wangzhitao on 17-8-21.
 */

public class ItemDivider extends RecyclerView.ItemDecoration {

    int left, right, height;
    Paint p = new Paint();

    public ItemDivider() {
        p.setColor(Color.RED);
    }

    /**
     *
     * @param left
     * @param right
     * @param height
     */
    public ItemDivider(int left, int right, int height) {
        this.left = left;
        this.right = right;
        this.height = height;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(10, 0, 10, 10);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }
}

