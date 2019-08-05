package com.tomsky.androiddemo.dynamic.virtualview;

import android.graphics.drawable.GradientDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;

import com.tomsky.androiddemo.dynamic.ProomLayoutUtils;

import org.json.JSONObject;

public abstract class ProomBaseView {
    public static final String P_NAME = "name";
    public static final String P_PROP = "prop";
    public static final String P_LAYOUT = "layout";
    public static final String P_L = "l";
    public static final String P_T = "t";
    public static final String P_R = "r";
    public static final String P_B = "b";
    public static final String P_W = "w";
    public static final String P_H = "h";
    public static final String P_CENTER_LAND = "centerLand";
    public static final String P_CENTER_PORT = "centerPort";
    public static final String P_WIDTH_AUTO = "widthAuto";
    public static final String P_HEIGHT_AUTO = "heightAuto";

    public static final String P_BG_COLOR = "bgColor";
    public static final String P_COLOR = "color";
    public static final String P_ALPHA = "alpha";

    public static final String P_ROUND = "round";
    public static final String P_CORNER_RADIUS = "cornerRadius";
    public static final String P_BORDER_WIDTH = "borderWidth";
    public static final String P_BORDER_COLOR = "borderColor";

    protected int l = Integer.MIN_VALUE;
    protected int t = Integer.MIN_VALUE;
    protected int w = Integer.MIN_VALUE;
    protected int h = Integer.MIN_VALUE;

    protected int r = Integer.MIN_VALUE;
    protected int b = Integer.MIN_VALUE;

    protected boolean centerLand = false;
    protected boolean centerPort = false;

    protected boolean widthAudo = false;
    protected boolean heightAudo = false;

    /**
     * 以上是解析出来的属性
     * ======================================
     */

    protected int rootLeft = Integer.MIN_VALUE; // 相对于rootView的位置，用于减少层级
    protected int rootTop = Integer.MIN_VALUE; // 相对于rootView的位置，用于减少层级

    protected boolean isLinearLayout = false;

    protected int viewId;

    protected boolean valid = true;
    
    public final void parseView(JSONObject jsonObject, ConstraintLayout rootView, ProomBaseView parentView) {
        viewId = ViewCompat.generateViewId();

        JSONObject pObj = jsonObject.optJSONObject(ProomBaseView.P_PROP);
        if (pObj == null) {
            valid = false;
            return;
        }
        parseProp(pObj, rootView, parentView);
        View view = generateView(jsonObject, rootView, parentView);
        if (view != null) {
            parseBackground(view, pObj.optJSONObject(ProomBaseView.P_BG_COLOR), pObj.optJSONObject(ProomBaseView.P_ROUND));
        }

    }

    protected abstract View generateView(JSONObject jsonObject, ConstraintLayout rootView, ProomBaseView parentView);

    protected void parseProp(JSONObject pObj, ConstraintLayout rootView, ProomBaseView parentView) {

        JSONObject layoutObj = pObj.optJSONObject(ProomBaseView.P_LAYOUT);

        if (layoutObj == null) {
            valid = false;
            return;
        }

        if (layoutObj.has(ProomBaseView.P_CENTER_LAND)) {
            centerLand = ProomLayoutUtils.parseBoolean(layoutObj.optString(ProomBaseView.P_CENTER_LAND), false);
        }

        if (layoutObj.has(ProomBaseView.P_CENTER_PORT)) {
            centerPort = ProomLayoutUtils.parseBoolean(layoutObj.optString(ProomBaseView.P_CENTER_PORT), false);
        }

        if (layoutObj.has(ProomBaseView.P_L)) {
            l = ProomLayoutUtils.scaleSize((float)layoutObj.optDouble(ProomBaseView.P_L));
            if (!centerLand && !centerPort) {
                if (parentView == null) {
                    rootLeft = l;
                } else if (parentView.rootLeft > Integer.MIN_VALUE) {
                    rootLeft = parentView.rootLeft + l;
                }
            }
        }
        if (layoutObj.has(ProomBaseView.P_T)) {
            t = ProomLayoutUtils.scaleSize((float)layoutObj.optDouble(ProomBaseView.P_T));
            if (!centerLand && !centerPort) {
                if (parentView == null) {
                    rootTop = t;
                } else if (parentView.rootTop > Integer.MIN_VALUE) {
                    rootTop = parentView.rootTop + t;
                }
            }
        }
        if (layoutObj.has(ProomBaseView.P_W)) {
            w = ProomLayoutUtils.scaleSize((float)layoutObj.optDouble(ProomBaseView.P_W));
        }
        if (layoutObj.has(ProomBaseView.P_H)) {
            h = ProomLayoutUtils.scaleSize((float)layoutObj.optDouble(ProomBaseView.P_H));
        }

        if (layoutObj.has(ProomBaseView.P_R)) {
            r = ProomLayoutUtils.scaleSize((float)layoutObj.optDouble(ProomBaseView.P_R));
        }
        if (layoutObj.has(ProomBaseView.P_B)) {
            b = ProomLayoutUtils.scaleSize((float)layoutObj.optDouble(ProomBaseView.P_B));
        }



        if (layoutObj.has(ProomBaseView.P_WIDTH_AUTO)) {
            widthAudo = ProomLayoutUtils.parseBoolean(layoutObj.optString(ProomBaseView.P_WIDTH_AUTO), false);
        }

        if (layoutObj.has(ProomBaseView.P_HEIGHT_AUTO)) {
            heightAudo = ProomLayoutUtils.parseBoolean(layoutObj.optString(ProomBaseView.P_HEIGHT_AUTO), false);
        }

    }

    private void parseBackground(View view, JSONObject bgColorObj, JSONObject roundObj) {
        if (bgColorObj != null) {
            try {
                int color = ProomLayoutUtils.parseColor(bgColorObj.optString(ProomBaseView.P_COLOR), (float)bgColorObj.optDouble(ProomBaseView.P_ALPHA, -1));
                if (roundObj != null) {
                    GradientDrawable drawable = parseRoundDrawable(roundObj);
                    drawable.setColor(color);
                    view.setBackground(drawable);
                } else {
                    view.setBackgroundColor(color);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (roundObj != null) {
                GradientDrawable drawable = parseRoundDrawable(roundObj);
                view.setBackground(drawable);

            }
        }
    }

    private GradientDrawable parseRoundDrawable(JSONObject roundObj) {
        GradientDrawable drawable = new GradientDrawable();
        double borderWidth = roundObj.optDouble(ProomBaseView.P_BORDER_WIDTH, -1);
        double cornerRadius = roundObj.optDouble(ProomBaseView.P_CORNER_RADIUS, -1);

        JSONObject borderColorObj = roundObj.optJSONObject(ProomBaseView.P_BORDER_COLOR);
        if (borderColorObj != null) {
            int borderColor = ProomLayoutUtils.parseColor(borderColorObj.optString(ProomBaseView.P_COLOR), (float)borderColorObj.optDouble(ProomBaseView.P_ALPHA, -1));
            if (borderWidth > 0) {
                drawable.setStroke(ProomLayoutUtils.scaleSize((float) borderWidth), borderColor);
            }
        }

        if (cornerRadius > 0) {
            drawable.setCornerRadius((float) cornerRadius);
        }
        return drawable;
    }

    public abstract View getView();

}