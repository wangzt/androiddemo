package com.tomsky.androiddemo.dynamic;

import android.content.Context;

import com.tomsky.androiddemo.dynamic.virtualview.ProomBaseView;
import com.tomsky.androiddemo.dynamic.virtualview.ProomImageView;
import com.tomsky.androiddemo.dynamic.virtualview.ProomLabelView;
import com.tomsky.androiddemo.dynamic.virtualview.ProomRootView;
import com.tomsky.androiddemo.dynamic.virtualview.ProomView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProomLayoutManager {

    private static ProomLayoutManager instance;

    private ProomRootView rootView;

    private Map<String, ProomBaseView> childViews = new HashMap<>();

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

    public void addChildView(String id, ProomBaseView childView) {
        childViews.put(id, childView);
    }

    /**
     * 根据data更新布局的属性
     */
    public void updateViewByData() {
        List<ProomBaseView> views = new ArrayList<>(childViews.values());
        for (ProomBaseView view: views) {
            view.updateViewByData();
        }
    }

    public void clearChildView() {
        childViews.clear();
    }

    public void onDestroy() {
        rootView = null;
        childViews.clear();
    }

    public static ProomRootView parseLayout(JSONObject json, Context context) {
        ProomDataCenter.getInstance().clearObserver();
        ProomLayoutManager.getInstance().clearChildView();

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
        } else if (ProomImageView.NAME.equals(name)) {
            subView = new ProomImageView();
        }

        if (subView != null) {
            subView.parseView(jsonObject, rootView, parentView);
        }
        return subView;
    }


}
