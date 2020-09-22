package com.tomsky.androiddemo.widget.expand;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.tomsky.androiddemo.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangzhitao on 2020/09/22
 **/
public class TagExpandableLayout extends ExpandableLayout implements TagItemView.OnTagSelectListener {

    private static final int DEFAULT_SELECT = -1;

    private MarginLayoutParams lp;

    private List<String> mTags = new ArrayList<>();
    private int mSelectIndex = DEFAULT_SELECT;

    public TagExpandableLayout(Context context) {
        super(context);
    }

    public TagExpandableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TagExpandableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addTags(List<String> tags, String selectTag) {
        if (tags == null || tags.size() == 0) return;

        mTags = tags;
        if (!TextUtils.isEmpty(selectTag)) {
            int size = tags.size();
            for (int i = 0; i < size; i++ ) {
                String tag = tags.get(i);
                if (TextUtils.equals(tag, selectTag)) {
                    mSelectIndex = i;
                    break;
                }
            }
        } else {
            mSelectIndex = DEFAULT_SELECT;
        }

        addTagItems();

    }

    private void addTagItems() {
        if (lp == null) {
            lp = new MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int marginRight = UIUtils.dp2px(10);
        int marginTop = UIUtils.dp2px(10);
        lp.setMargins(0, marginTop, marginRight, 0);

        removeAllViews();

        for (int i = 0; i < mTags.size(); i++) {
            final String item = mTags.get(i);
            TagItemView tagLayout = new TagItemView(getContext(), i, item);
            if (i == mSelectIndex) {
                tagLayout.setSelected(true);
            }
            tagLayout.setOnTagSelectListener(this);
            addView(tagLayout, lp);
        }
    }

    @Override
    public void onSelect(int index, String text) {
        View view = getChildAt(mSelectIndex);
        if (view != null) {
            view.setSelected(false);
        }

        if (mSelectIndex == index) {
            mSelectIndex = DEFAULT_SELECT;
        } else {
            View viewSelect = getChildAt(index);
            if (viewSelect != null) {
                viewSelect.setSelected(true);
                mSelectIndex = index;
            } else {
                mSelectIndex = DEFAULT_SELECT;
            }
        }

    }

    public String getSelectTag() {
        if (mSelectIndex > -1 && mSelectIndex < mTags.size()) {
            return mTags.get(mSelectIndex);
        }
        return "";
    }

}
