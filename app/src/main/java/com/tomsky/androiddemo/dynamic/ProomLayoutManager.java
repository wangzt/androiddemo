package com.tomsky.androiddemo.dynamic;

import android.content.Context;
import android.support.constraint.ConstraintLayout;

import com.tomsky.androiddemo.dynamic.virtualview.ProomBaseView;
import com.tomsky.androiddemo.dynamic.virtualview.ProomView;
import com.tomsky.androiddemo.dynamic.virtualview.ProomRootView;

import org.json.JSONObject;

public class ProomLayoutManager {



    public static ProomRootView parseLayout(JSONObject json, Context context) {
        ProomRootView rootView = new ProomRootView(json, context);
        rootView.parseSubViews(json);
        return rootView;
    }

    public static ProomBaseView parseSubView(JSONObject jsonObject, ConstraintLayout rootView, ProomView parentView) {
        if (jsonObject == null) return null;

        String name = jsonObject.optString(ProomView.P_NAME);
        ProomBaseView subView = null;
        if (ProomView.NAME.equals(name)) {
            subView = new ProomView();
        }

        if (subView != null) {
            subView.parseView(jsonObject, rootView, parentView);
        }
        return subView;
    }


}
