package com.tomsky.androiddemo.dynamic.virtualview;


import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewCompat;
import android.widget.RelativeLayout;

import com.tomsky.androiddemo.dynamic.ProomLayoutManager;
import com.tomsky.androiddemo.dynamic.ProomLayoutUtils;

import org.json.JSONArray;
import org.json.JSONObject;

public class ProomRootView {

    private ConstraintLayout rootView;

    public ProomRootView(JSONObject json, Context context) {
        JSONObject pObj = json.optJSONObject(ProomView.P_PROP);
        JSONObject layoutObj = pObj.optJSONObject(ProomView.P_LAYOUT);

        double l = layoutObj.optDouble(ProomView.P_L);
        double t = layoutObj.optDouble(ProomView.P_T);
        double w = layoutObj.optDouble(ProomView.P_W);
        double h = layoutObj.optDouble(ProomView.P_H);

        JSONObject bgColorObj = pObj.optJSONObject(ProomView.P_BG_COLOR);


        rootView = new ConstraintLayout(context);
        rootView.setId(ViewCompat.generateViewId());

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams( ProomLayoutUtils.scaleSize((float) w), ProomLayoutUtils.scaleSize((float) h));
        lp.leftMargin = ProomLayoutUtils.scaleSize((float) l);
        lp.topMargin = ProomLayoutUtils.scaleSize((float) t);
        rootView.setLayoutParams(lp);

        if (bgColorObj != null) {
            try {
                int color = ProomLayoutUtils.parseColor(bgColorObj.optString(ProomView.P_COLOR), (float)bgColorObj.optDouble(ProomView.P_ALPHA, -1));
                rootView.setBackgroundColor(color);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    public void parseSubViews(JSONObject json) {
        JSONArray child = json.optJSONArray(ProomView.P_CHILD);
        if (child != null) {
            int size = child.length();
            for (int i = 0; i < size; i++) {
                ProomBaseView subView = ProomLayoutManager.parseSubView(child.optJSONObject(i), rootView, null);
                if (subView != null && subView.getView() != null) {
                    rootView.addView(subView.getView());
                }
            }
        }
    }

    public ConstraintLayout getView() {
        return rootView;
    }
}
