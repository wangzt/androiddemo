package com.tomsky.androiddemo.base;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;


public class ConstraintGroup {

    private int mGroupVisibility;
    private View mGroupView;

    private SparseArray<View> mSubViews = new SparseArray<>();
    private SparseIntArray mSubVisibilitys = new SparseIntArray();

    public ConstraintGroup(View groupView, int visibility) {
        this.mGroupView = groupView;
        this.mGroupVisibility = visibility;
    }

    public ConstraintGroup(int visibility) {
        this.mGroupVisibility = visibility;
    }

    /**
     *
     * @param subView
     * @param viewId
     * @param visibility 逻辑上的初始值，不是真实的值, 如果要跟随Group变化，则设置为VISIBILITY
     */
    public void addSubView(View subView, int viewId, int visibility) {
        mSubViews.put(viewId, subView);
        mSubVisibilitys.put(viewId, visibility);
    }

    /**
     *
     * @param parent 用来获取subView
     * @param subViewId
     * @param visibility 逻辑上的初始值，不是真实的值, 如果要跟随Group变化，则设置为VISIBILITY
     * @return subView，方便外部复用，不用再find一次，但是要判空
     */
    public View addSubViewFromParent(View parent, int subViewId, int visibility) {
        View subView = parent.findViewById(subViewId);
        addSubView(subView, subViewId, visibility);
        return subView;
    }

    public void setSubVisibility(int viewId, int visibility) {
        mSubVisibilitys.put(viewId, visibility);

        if (mGroupVisibility == View.VISIBLE) {
            mSubViews.get(viewId).setVisibility(visibility);
        }
    }

    public void setVisibility(int visibility) {
        this.mGroupVisibility = visibility;
        if (mGroupView != null) {
            mGroupView.setVisibility(visibility);
        }
        if (visibility == View.VISIBLE) { // 需要判断单个是否展示
            int size = mSubViews.size();
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    View view = mSubViews.valueAt(i);
                    if (view != null && mSubVisibilitys.valueAt(i) == View.VISIBLE) {
                        view.setVisibility(visibility);
                    }
                }
            }

        } else { // 依次设置
            int size = mSubViews.size();
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    View view = mSubViews.valueAt(i);
                    if (view != null) {
                        view.setVisibility(visibility);
                    }
                }
            }
        }
    }

    public int getVisibility() {
        return mGroupVisibility;
    }

    private Object tag;
    public void setTag(Object o){
        this.tag = o;
    }
    public Object getTag(){
        return tag;
    }
}
