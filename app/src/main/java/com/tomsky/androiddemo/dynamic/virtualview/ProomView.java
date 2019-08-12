package com.tomsky.androiddemo.dynamic.virtualview;

import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.LinearLayout;


import com.tomsky.androiddemo.dynamic.ProomLayoutManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProomView extends ProomBaseView {
    public static final String NAME = "view";

    public static final String P_CHILD = "child";

    private View view;

    private List<ProomBaseView> child = new ArrayList<>();

    @Override
    protected View generateView(JSONObject jsonObject, ProomRootView rootView, ProomBaseView parentView) {
        child.clear();

        List<ProomBaseView> subViews = new ArrayList<>();
        if (jsonObject.has(P_CHILD)) {
            JSONArray child = jsonObject.optJSONArray(P_CHILD);
            if (child != null) {
                int size = child.length();
                for (int i = 0; i < size; i++) {
                    ProomBaseView subView = ProomLayoutManager.parseSubView((JSONObject) child.opt(i), rootView, this);
                    if (subView != null) {
                        subViews.add(subView);
                    }
                }
            }
        }

        int size = subViews.size();
        if (size > 0) {
            if (widthAuto || heightAuto) {
                addToLayout(rootView, parentView, subViews);
            } else {
                addToRoot(rootView, parentView, subViews);
            }
        } else {
            view = new View(rootView.getContext());
            layoutParams = calcLayoutParams(rootView, parentView);
            view.setLayoutParams(layoutParams);
            if (parentView == null) {
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
        if (parentView == null) {
            rootView.addView(layout);
        }
        for (ProomBaseView subView: subViews) {
            layout.addView(subView.getView());
        }
        view = layout;
    }

    private void addToRoot(ProomRootView rootView, ProomBaseView parentView, List<ProomBaseView> subViews) {
        view = new View(rootView.getContext());
        layoutParams = calcLayoutParams(rootView, parentView);
        view.setLayoutParams(layoutParams);
        if (parentView == null) {
            rootView.addView(view);
        }
        for (ProomBaseView subView: subViews) {
            rootView.addView(subView.getView());
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

    @Override
    protected void updateViewValue(String prop, String value) {

    }
}
