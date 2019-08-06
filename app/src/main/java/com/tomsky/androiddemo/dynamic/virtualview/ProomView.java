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

    private boolean addRoot = true;

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
                addRoot = false;
            }
            ConstraintLayout layout = new ConstraintLayout(rootView.getContext());
            layoutParams = calcLayoutParams(rootView, parentView);
            layout.setLayoutParams(layoutParams);
            if (parentView == null) {
                rootView.addView(layout);
            }
            for (int i = 0; i < size; i++) {
                ProomBaseView subView = subViews.get(i);
                if (addRoot) {
                    rootView.addView(subView.getView());
                } else {
                    layout.addView(subView.getView());
                }
            }
            view = layout;
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

    @Override
    protected void parseSubProp(JSONObject pObj, ProomRootView rootView, ProomBaseView parentView) {

    }

    @Override
    protected void parseData(JSONObject jsonObject) {

    }

    @Override
    protected void onAttach() {

    }

    @Override
    public View getView() {
        return view;
    }
}
