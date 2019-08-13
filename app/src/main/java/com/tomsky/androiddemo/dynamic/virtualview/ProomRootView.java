package com.tomsky.androiddemo.dynamic.virtualview;


import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.tomsky.androiddemo.dynamic.ProomLayoutManager;
import com.tomsky.androiddemo.dynamic.ProomLayoutUtils;
import com.tomsky.androiddemo.util.ThreadUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProomRootView {

    private ConstraintLayout rootView;
    private int rootId;
    private List<ProomLabelView> marqueeLabelViewList = new ArrayList<>();

    private List<ProomBaseView> child = new ArrayList<>();

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
        JSONArray childArray = json.optJSONArray(ProomView.P_CHILD);
        if (childArray != null) {
            int size = childArray.length();
            for (int i = 0; i < size; i++) {
                ProomBaseView subView = ProomLayoutManager.parseSubView(childArray.optJSONObject(i), this, null, true, false);
                child.add(subView);
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

    public void addViewByJSON(int index, JSONObject viewObject) {
        ProomBaseView subView = ProomLayoutManager.parseSubView(viewObject, this, null, false, true);
        if (subView != null && subView.getView() != null) {
            int size = child.size();
            if (size > 0) {
                if (index > -1 && index < size) {
                    ProomBaseView childView = child.get(index);
                    View view = childView.getView();
                    if (view != null) {
                        int realIndex = ((ConstraintLayout)view.getParent()).indexOfChild(view);
                        child.add(index, subView);
                        addViewToRoot(subView.getView(), realIndex);
                    }
                } else {
                    child.add(subView);
                    addViewToRoot(subView.getView(), -1);

                }
            } else { // 没有child的情况
                child.add(subView);
                addViewToRoot(subView.getView(), -1);

            }
        }
    }

    private void addViewToRoot(View subView, int index) {
        final ProomView.ViewAddData addData = new ProomView.ViewAddData(rootView, subView, index);
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                addData.addView();
            }
        });
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
