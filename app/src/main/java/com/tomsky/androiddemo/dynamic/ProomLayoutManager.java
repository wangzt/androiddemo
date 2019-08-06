package com.tomsky.androiddemo.dynamic;

import android.content.Context;

import com.tomsky.androiddemo.dynamic.virtualview.ProomBaseView;
import com.tomsky.androiddemo.dynamic.virtualview.ProomLabelView;
import com.tomsky.androiddemo.dynamic.virtualview.ProomView;
import com.tomsky.androiddemo.dynamic.virtualview.ProomRootView;

import org.json.JSONObject;

public class ProomLayoutManager {

    private static ProomLayoutManager instance;

    private ProomRootView rootView;

    private ProomLayoutManager() {
    }

    public static ProomLayoutManager getInstance() {
        if (instance == null) {
            synchronized (ProomLayoutManager.class) {
                if (instance == null) {
                    instance = new ProomLayoutManager();
                }
            }
        }

        return instance;
    }

    public void setRootView(ProomRootView rootView) {
        this.rootView = rootView;
    }

    public ProomRootView getRootView() {
        return rootView;
    }

    public void onDestroy() {
        rootView = null;
    }

    public static ProomRootView parseLayout(JSONObject json, Context context) {
        ProomRootView rootView = new ProomRootView(json, context);
        rootView.parseSubViews(json);
        return rootView;
    }

    public static ProomBaseView parseSubView(JSONObject jsonObject, ProomRootView rootView, ProomView parentView) {
        if (jsonObject == null) return null;

        String name = jsonObject.optString(ProomView.P_NAME);
        ProomBaseView subView = null;
        if (ProomView.NAME.equals(name)) {
            subView = new ProomView();
        } else if (ProomLabelView.NAME.equals(name)) {
            subView = new ProomLabelView();
        }

        if (subView != null) {
            subView.parseView(jsonObject, rootView, parentView);
        }
        return subView;
    }


}
