package com.tomsky.androiddemo.widget.expand;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tomsky.androiddemo.util.UIUtils;

/**
 * Created by wangzhitao on 2020/09/22
 **/
public class TagItemView extends androidx.appcompat.widget.AppCompatTextView {

    private int index = 0;
    private String text;

    private TagItemView.OnTagSelectListener onTagSelectListener;
    public TagItemView(Context context, int index, String text) {
        this(context, null);
        setText(text);
        this.index = index;
        this.text = text;
    }

    public TagItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setPadding(UIUtils.dp2px(14), UIUtils.dp2px(7), UIUtils.dp2px(7), UIUtils.dp2px(14));
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (selected) {
            setBackgroundColor(Color.RED);
        } else {
            setBackgroundColor(Color.TRANSPARENT);
        }
    }

    public void setOnTagSelectListener(TagItemView.OnTagSelectListener onTagSelectListener) {
        this.onTagSelectListener = onTagSelectListener;

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTagSelectListener != null) {
                    onTagSelectListener.onSelect(index, text);
                }
            }
        });
    }

    public interface OnTagSelectListener {
        void onSelect(int index, String text);
    }
}
