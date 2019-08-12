package com.tomsky.androiddemo.dynamic.virtualview;

import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.tomsky.androiddemo.dynamic.ProomLayoutUtils;

import org.json.JSONObject;

public class ProomLabelView extends ProomBaseView {
    public static final String NAME = "label";

    public static final String P_TEXT = "text";
    public static final String P_TEXT_SIZE = "textSize";
    public static final String P_TEXT_COLOR = "textColor";
    public static final String P_MAX_WIDTH = "maxWidth";
    public static final String P_ELLIPSIZE = "ellipsize";
    public static final String P_MAX_LINES = "maxLines";
    public static final String P_GRAVITY = "gravity";

    private String text;
    private float textSize;
    private int maxWidth;
    private int ellipsize; // 0:省略号, 1: 截断, 2:跑马灯
    private int maxLines = 1;
    private int gravity; // 0: left, 1: right, 2: center

    private TextView view;

    private boolean hasMarqueue;// 是否需要跑马灯

    @Override
    protected View generateView(JSONObject jsonObject, ProomRootView rootView, ProomBaseView parentView) {
        view = new TextView(rootView.getContext());

        layoutParams = calcLayoutParams(rootView, parentView);
        view.setLayoutParams(layoutParams);
        if (parentView == null) {
            rootView.addView(view);
        }

        return view;
    }

    @Override
    protected void parseSubProp(JSONObject pObj, ProomRootView rootView, ProomBaseView parentView) {
        updateViewProp(pObj, rootView, parentView);
        if (borderWidth > 0) {
            int padding = borderWidth;
            view.setPadding(padding, padding, padding, padding);
        }
    }

    private void updateViewProp(JSONObject pObj, ProomRootView rootView, ProomBaseView parentView) {
        if (pObj.has(P_TEXT)) {
            text = pObj.optString(P_TEXT);
            view.setText(text);
        }
        if (pObj.has(P_TEXT_SIZE)) {
            textSize = ProomLayoutUtils.scaleFloatSize((float) pObj.optDouble(P_TEXT_SIZE));
            view.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }
        if (pObj.has(P_GRAVITY)) {
            gravity = pObj.optInt(P_GRAVITY, 0);
            if (gravity == 0) {
                view.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
            } else if (gravity == 1) {
                view.setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);
            } else if (gravity == 2) {
                view.setGravity(Gravity.CENTER);
            }
        }

        if (pObj.has(P_MAX_LINES)) {
            maxLines = pObj.optInt(P_MAX_LINES, 1);
            if (maxLines == 1) {
                view.setSingleLine(true);
            } else {
                view.setMaxLines(maxLines);
            }
        } else if (!hasAttach) {
            maxLines = 1;
            view.setSingleLine(true);
        }


        if (pObj.has(P_MAX_WIDTH)) {
            double pWidth = pObj.optDouble(P_MAX_WIDTH, -1f);
            if (pWidth > 0) {
                maxWidth = ProomLayoutUtils.scaleSize((float) pWidth);
            }
            if (maxWidth > 0) {
                view.setMaxWidth(maxWidth);
            }
        }

        if (pObj.has(P_ELLIPSIZE)) {
            ellipsize = pObj.optInt(P_ELLIPSIZE, 0);
            if (ellipsize == 0) {
                view.setEllipsize(TextUtils.TruncateAt.END);
                hasMarqueue = false;
            } else if (ellipsize == 1) {
                hasMarqueue = false;
            } else if (ellipsize == 2) {
                if (!hasMarqueue) {
                    hasMarqueue = true;
                    rootView.addMarqueeLabelView(this);
                }
            }
        } else if (!hasAttach) { // 第一次没有配置
            hasMarqueue = false;
            view.setEllipsize(TextUtils.TruncateAt.END);
        }

        if (pObj.has(P_TEXT_COLOR)) {
            JSONObject colorObj = pObj.optJSONObject(P_TEXT_COLOR);
            if (colorObj != null) {
                try {
                    int color = ProomLayoutUtils.parseColor(colorObj.optString(ProomBaseView.P_COLOR), (float)colorObj.optDouble(ProomBaseView.P_ALPHA, -1));
                    view.setTextColor(color);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public View getView() {
        return view;
    }

    @Override
    public void onAttach() {
        if (view != null) {
            if (hasMarqueue) {
                view.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                view.setSelected(true);
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                view.setMarqueeRepeatLimit(-1);
            }
            hasAttach = true;
        }
    }

    @Override
    protected void updateViewValue(String prop, String value) {
        if (view == null) return;

        if (P_TEXT.equals(prop)) {
            if (value != null) {
                view.setText(value);
            }
        }
    }

    @Override
    public void updateViewPropByH5(ProomRootView rootView, JSONObject pObj) {
        if (parseLayout(pObj)) {
            layoutParams = calcLayoutParams(rootView, parentView);
            view.setLayoutParams(layoutParams);
        }
        updateViewProp(pObj, rootView, parentView);
        if (view != null) {
            if (pObj.has(ProomBaseView.P_BG_COLOR) || pObj.has(ProomBaseView.P_ROUND)) {
                parseBackground(view, pObj.optJSONObject(ProomBaseView.P_BG_COLOR), pObj.optJSONObject(ProomBaseView.P_ROUND));
            }
        }
    }

}
