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
     * 根据data更新布局的属性，主线程调用
     */
    public void updateViewByData() {
        List<ProomBaseView> views = new ArrayList<>(childViews.values());
        for (ProomBaseView view: views) {
            view.updateViewByData();
        }
    }

    /**
     * H5更新控件属性, 主线程调用
     *
     * @param id
     * @param pObj
     */
    public void updateViewPropById(String id, JSONObject pObj) {
        if (rootView != null) {
            if (rootView.isRootId(id)) {
                rootView.updateViewPropByH5(pObj);
            } else {
                ProomBaseView childView = childViews.get(id);
                if (childView != null) {
                    childView.updateViewPropByH5(rootView, pObj);
                }
            }
        }
    }

    /**
     * H5更新控件data表达式，整体替换
     *
     * 建议子线程调用
     *
     * @param id
     * @param dataObj
     */
    public void updateViewDataById(String id, JSONObject dataObj) {
        ProomBaseView childView = childViews.get(id);
        if (childView != null) {
            childView.updateViewDataByH5(dataObj);
        }
    }

    /**
     * 删除控件，主线程调用
     *
     * @param id
     */
    public void removeViewById(String id) {
        ProomBaseView proomBaseView = childViews.remove(id);
        if (proomBaseView != null) {
            proomBaseView.removeFromParent();
        }
    }

    /**
     * 添加view，子线程调用
     *
     * @param parentId 要插入子view的父容器id
     * @param index 要插入子view的index
     * @param jsonObject
     */
    public void addViewByJSON(String parentId, int index, JSONObject jsonObject) {
        if (rootView != null) {
            if (rootView.isRootId(parentId)) {
                rootView.addViewByJSON(index, jsonObject);
            } else {
                ProomBaseView childView = childViews.get(parentId);
                if (childView != null && childView instanceof ProomView) {
                    ((ProomView)childView).addViewByJSON(index, jsonObject, rootView);
                }
            }
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

    public static ProomBaseView parseSubView(JSONObject jsonObject, ProomRootView rootView, ProomView parentView, boolean needAddView, boolean addChildToLayout) {
        if (jsonObject == null) return null;

        String name = jsonObject.optString(ProomView.P_NAME);
        ProomBaseView subView = null;
        if (ProomView.NAME.equals(name)) {
            subView = new ProomView(needAddView, addChildToLayout);
        } else if (ProomLabelView.NAME.equals(name)) {
            subView = new ProomLabelView(needAddView);
        } else if (ProomImageView.NAME.equals(name)) {
            subView = new ProomImageView(needAddView);
        }

        if (subView != null) {
            subView.parseView(jsonObject, rootView, parentView);
        }
        return subView;
    }


}
