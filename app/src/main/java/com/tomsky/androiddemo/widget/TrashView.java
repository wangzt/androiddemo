package com.tomsky.androiddemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.tomsky.androiddemo.R;

/**
 * Created by j-wangzhitao on 17-6-13.
 */

public class TrashView extends RelativeLayout {

    private View mBottomView;

    public TrashView(Context context) {
        super(context);
        initView(context);
    }

    public TrashView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TrashView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        inflate(context, R.layout.trash_view, this);
        mBottomView = findViewById(R.id.trash_bottom_view);
    }


}
