package com.tomsky.androiddemo.dynamic.virtualview;

import android.support.constraint.ConstraintLayout;
import android.view.View;


import com.tomsky.androiddemo.dynamic.ProomLayoutManager;
import com.tomsky.androiddemo.util.ThreadUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProomView extends ProomBaseView {
    public static final String NAME = "view";

    public static final String P_CHILD = "child";

    private ConstraintLayout view;

    private List<ProomBaseView> child = new ArrayList<>();

    private boolean addChildToLayout = false; // 是否加入到自己的容器中，默认加入到root容器中，减少层级

    public ProomView(boolean needAddView, boolean addChildToLayout) {
        super(needAddView);
        this.addChildToLayout = addChildToLayout;
    }

    @Override
    protected View generateView(JSONObject jsonObject, ProomRootView rootView, ProomBaseView parentView) {
        child.clear();

        List<ProomBaseView> subViews = new ArrayList<>();
        if (jsonObject.has(P_CHILD)) {
            JSONArray child = jsonObject.optJSONArray(P_CHILD);
            if (child != null) {
                int size = child.length();
                for (int i = 0; i < size; i++) {
                    ProomBaseView subView = ProomLayoutManager.parseSubView((JSONObject) child.opt(i), rootView, this, needAddView, addChildToLayout);
                    if (subView != null) {
                        subViews.add(subView);
                    }
                }
            }
        }

        int size = subViews.size();
        if (size > 0) {
            child.addAll(subViews);
            if (widthAuto || heightAuto || addChildToLayout) {
                addToLayout(rootView, parentView, subViews);
            } else {
                addToRoot(rootView, parentView, subViews);
            }
        } else {
            view = new ConstraintLayout(rootView.getContext());
            layoutParams = calcLayoutParams(rootView, parentView);
            view.setLayoutParams(layoutParams);
            if (parentView == null && needAddView) {
                rootView.addView(view);
            }
        }

        if (view != null) {
            view.setId(viewId);
        }
        return view;
    }

    private void addToLayout(ProomRootView rootView, ProomBaseView parentView, List<ProomBaseView> subViews) {
        ConstraintLayout layout = new ConstraintLayout(rootView.getContext());
        layoutParams = calcLayoutParams(rootView, parentView);
        layout.setLayoutParams(layoutParams);
        if (parentView == null && needAddView) {
            rootView.addView(layout);
        }
        for (ProomBaseView subView: subViews) {
            layout.addView(subView.getView());
        }
        view = layout;
    }

    private void addToRoot(ProomRootView rootView, ProomBaseView parentView, List<ProomBaseView> subViews) {
        view = new ConstraintLayout(rootView.getContext());
        layoutParams = calcLayoutParams(rootView, parentView);
        view.setLayoutParams(layoutParams);
        if (parentView == null && needAddView) {
            rootView.addView(view);
        }
        if (rootView != null) {
            for (ProomBaseView subView: subViews) {
                rootView.addView(subView.getView());
            }
        }
    }

    @Override
    protected void parseSubProp(JSONObject pObj, ProomRootView rootView, ProomBaseView parentView) {

    }

    @Override
    protected void onAttach() {

    }

    @Override
    public View getView() {
        return view;
    }

    public void addViewByJSON(int index, JSONObject viewObject, ProomRootView rootView) {
        int size = child.size();
        if (size > 0) {
            if (index > -1 && index < size) {
                addChildViewToIndex(index, viewObject, rootView);
            } else { // 放到最后
                addChildViewToIndex(size - 1, viewObject, rootView);
            }
        } else {
            ProomBaseView subView = ProomLayoutManager.parseSubView(viewObject, rootView, this, false, true);
            if (subView != null && subView.getView() != null) {
                child.add(subView);
                int realIndex = ((ConstraintLayout)view.getParent()).indexOfChild(view);
                final ViewAddData addData = new ViewAddData((ConstraintLayout)view.getParent(), subView.getView(), realIndex + 1);
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addData.addView();
                    }
                });
            }
        }
    }

    private void addChildViewToIndex(int index, JSONObject viewObject, ProomRootView rootView) {
        ProomBaseView childView = child.get(index);
        View view = childView.getView();
        if (view != null) {
            int realIndex = ((ConstraintLayout)view.getParent()).indexOfChild(view);
            ProomBaseView subView = ProomLayoutManager.parseSubView(viewObject, rootView, this, false, true);
            if (subView != null && subView.getView() != null) {
                child.add(index, subView);
                final ViewAddData addData = new ViewAddData((ConstraintLayout)view.getParent(), subView.getView(), realIndex);
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addData.addView();
                    }
                });
            }
        }
    }

    public void removeView(ProomBaseView childView) {
        child.remove(childView);
    }

    @Override
    protected void updateViewValue(String prop, String value) {
        // TODO:根据prop名称，更改控件view的value值
    }

    @Override
    public void updateViewPropByH5(ProomRootView rootView, JSONObject pObj) {
        if (parseLayout(pObj)) {
            layoutParams = calcLayoutParams(rootView, parentView);
            view.setLayoutParams(layoutParams);
        }
        if (pObj.has(P_VISIBLE)) {
            // TODO:更新可见性，包括子view
        }
    }

    public static class ViewAddData {
        ConstraintLayout parent;
        View subView;
        int index;

        public ViewAddData(ConstraintLayout parent, View subView, int index) {
            this.parent = parent;
            this.subView = subView;
            this.index = index;
        }

        public void addView() {
            if (index < 0) {
                parent.addView(subView);
            } else {
                parent.addView(subView, index);
            }
        }
    }
}
