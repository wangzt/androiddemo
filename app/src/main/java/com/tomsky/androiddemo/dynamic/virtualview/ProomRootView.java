package com.tomsky.androiddemo.dynamic.virtualview;


import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.tomsky.androiddemo.dynamic.ProomLayoutManager;
import com.tomsky.androiddemo.dynamic.ProomLayoutUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProomRootView {

    private ConstraintLayout rootView;
    private int rootId;
    private List<ProomLabelView> marqueeLabelViewList = new ArrayList<>();

    private String id;

    public ProomRootView(JSONObject json, Context context) {
        rootId = ViewCompat.generateViewId();
        rootView = new ConstraintLayout(context);
        rootView.setId(rootId);

        String pId = json.optString(ProomBaseView.P_ID);
        if (!TextUtils.isEmpty(pId)) {
            id = pId;
        }

        JSONObject pObj = json.optJSONObject(ProomView.P_PROP);
        if (pObj != null) {
            parseProp(pObj);
        }

    }

    private void parseProp(JSONObject pObj) {
        if (pObj.has(ProomView.P_LAYOUT)) {
            JSONObject layoutObj = pObj.optJSONObject(ProomView.P_LAYOUT);

            double l = layoutObj.optDouble(ProomView.P_L);
            double t = layoutObj.optDouble(ProomView.P_T);
            double w = layoutObj.optDouble(ProomView.P_W);
            double h = layoutObj.optDouble(ProomView.P_H);

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams( ProomLayoutUtils.scaleSize((float) w), ProomLayoutUtils.scaleSize((float) h));
            lp.leftMargin = ProomLayoutUtils.scaleSize((float) l);
            lp.topMargin = ProomLayoutUtils.scaleSize((float) t);
            rootView.setLayoutParams(lp);
        }

        if (pObj.has(ProomView.P_BG_COLOR)) {
            JSONObject bgColorObj = pObj.optJSONObject(ProomView.P_BG_COLOR);
            if (bgColorObj != null) {
                try {
                    int color = ProomLayoutUtils.parseColor(bgColorObj.optString(ProomView.P_COLOR), (float)bgColorObj.optDouble(ProomView.P_ALPHA, -1));
                    rootView.setBackgroundColor(color);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void parseSubViews(JSONObject json) {
        JSONArray child = json.optJSONArray(ProomView.P_CHILD);
        if (child != null) {
            int size = child.length();
            for (int i = 0; i < size; i++) {
                ProomLayoutManager.parseSubView(child.optJSONObject(i), this, null);
            }
        }
    }

    public boolean isRootId(String id) {
        return TextUtils.equals(this.id, id);
    }

    public void updateViewPropByH5(JSONObject pObj) {
        if (pObj != null) {
            parseProp(pObj);
        }
    }

    public ConstraintLayout getView() {
        return rootView;
    }

    public Context getContext() {
        return rootView.getContext();
    }

    public int getId() {
        return rootId;
    }

    public void addView(View view) {
        if (rootView != null) {
            rootView.addView(view);
        }
    }

    public void onAttach() {
        if (marqueeLabelViewList.size() > 0) {
            for (ProomLabelView labelView: marqueeLabelViewList) {
                labelView.onAttach();
            }
        }

    }

    public void addMarqueeLabelView(ProomLabelView labelView) {
        marqueeLabelViewList.add(labelView);
    }

}
