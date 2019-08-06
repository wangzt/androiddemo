package com.tomsky.androiddemo.dynamic.virtualview;

import android.graphics.drawable.GradientDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewCompat;
import android.view.View;

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

    public static final String P_DATA = "data";

    protected int l = Integer.MIN_VALUE;
    protected int t = Integer.MIN_VALUE;
    protected int w = Integer.MIN_VALUE;
    protected int h = Integer.MIN_VALUE;

    protected int r = Integer.MIN_VALUE;
    protected int b = Integer.MIN_VALUE;

    protected int borderWidth = 0;

    protected boolean centerLand = false;
    protected boolean centerPort = false;

    protected boolean widthAuto = false;
    protected boolean heightAuto = false;

    /**
     * 以上是解析出来的属性
     * ======================================
     */

    protected ConstraintLayout.LayoutParams layoutParams;

    protected int viewId;

    protected boolean valid = true;
    
    public final void parseView(JSONObject jsonObject, ProomRootView rootView, ProomBaseView parentView) {
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
        parseSubProp(pObj, rootView, parentView);

        parseData(jsonObject.optJSONObject(ProomBaseView.P_DATA));
    }

    protected abstract View generateView(JSONObject jsonObject, ProomRootView rootView, ProomBaseView parentView);

    protected abstract void parseSubProp(JSONObject pObj, ProomRootView rootView, ProomBaseView parentView);

    protected abstract void parseData(JSONObject dataObject);

    protected abstract void onAttach();

    protected void parseProp(JSONObject pObj, ProomRootView rootView, ProomBaseView parentView) {

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
        }
        if (layoutObj.has(ProomBaseView.P_T)) {
            t = ProomLayoutUtils.scaleSize((float)layoutObj.optDouble(ProomBaseView.P_T));
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
            widthAuto = ProomLayoutUtils.parseBoolean(layoutObj.optString(ProomBaseView.P_WIDTH_AUTO), false);
        }

        if (layoutObj.has(ProomBaseView.P_HEIGHT_AUTO)) {
            heightAuto = ProomLayoutUtils.parseBoolean(layoutObj.optString(ProomBaseView.P_HEIGHT_AUTO), false);
        }

    }


    protected void parseBackground(View view, JSONObject bgColorObj, JSONObject roundObj) {
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
        double borderW = roundObj.optDouble(ProomBaseView.P_BORDER_WIDTH, -1);
        double cornerRadius = roundObj.optDouble(ProomBaseView.P_CORNER_RADIUS, -1);

        JSONObject borderColorObj = roundObj.optJSONObject(ProomBaseView.P_BORDER_COLOR);
        if (borderColorObj != null) {
            int borderColor = ProomLayoutUtils.parseColor(borderColorObj.optString(ProomBaseView.P_COLOR), (float)borderColorObj.optDouble(ProomBaseView.P_ALPHA, -1));
            if (borderW > 0) {
                borderWidth = ProomLayoutUtils.scaleSize((float) borderW);
                drawable.setStroke(borderWidth, borderColor);
            }
        }

        if (cornerRadius > 0) {
            drawable.setCornerRadius(ProomLayoutUtils.scaleFloatSize((float) cornerRadius));
        }
        return drawable;
    }

    protected ConstraintLayout.LayoutParams calcLayoutParams(ProomRootView rootView, ProomBaseView parentView) {
        int width = w;
        int height = h;
        if (widthAuto) {
            width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        }
        if (heightAuto) {
            height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        }
        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(width, height);
        int parentId = rootView.getId();
        if (parentView != null) {
            parentId = parentView.viewId;
        }
        if (centerLand) {
            lp.leftToLeft = parentId;
            lp.rightToRight = parentId;
            if (b > Integer.MIN_VALUE) {
                lp.bottomToBottom = parentId;
                lp.bottomMargin = b;
            } else {
                lp.topToTop = parentId;
                lp.topMargin = t;
            }
        } else if (centerPort) {
            lp.topToTop = parentId;
            lp.bottomToBottom = parentId;
            if (r > Integer.MIN_VALUE) {
                lp.rightToRight = parentId;
                lp.rightMargin = r;
            } else {
                lp.leftToLeft = parentId;
                lp.leftMargin = l;
            }
        } else {
            if (r > Integer.MIN_VALUE) {
                lp.rightToRight = parentId;
                lp.rightMargin = r;
            } else {
                lp.leftToLeft = parentId;
                lp.leftMargin = l;
            }
            if (b > Integer.MIN_VALUE) {
                lp.bottomToBottom = parentId;
                lp.bottomMargin = b;
            } else {
                lp.topToTop = parentId;
                lp.topMargin = t;
            }
        }

        return lp;
    }

    public abstract View getView();

    public ConstraintLayout.LayoutParams getLayoutParams() {
        return layoutParams;
    }

}
