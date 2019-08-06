package com.tomsky.androiddemo.dynamic.virtualview;

import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
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
        text = pObj.optString(P_TEXT);
        textSize = ProomLayoutUtils.scaleFloatSize((float) pObj.optDouble(P_TEXT_SIZE));
        gravity = pObj.optInt(P_GRAVITY, 0);
        maxLines = pObj.optInt(P_MAX_LINES, 1);
        double pWidth = pObj.optDouble(P_MAX_WIDTH, -1f);
        if (pWidth > 0) {
            maxWidth = ProomLayoutUtils.scaleSize((float) pWidth);
        }
        ellipsize = pObj.optInt(P_ELLIPSIZE, 0);

        view.setText(text);
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

        if (gravity == 0) {
            view.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
        } else if (gravity == 1) {
            view.setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);
        } else if (gravity == 2) {
            view.setGravity(Gravity.CENTER);
        }

        if (maxLines == 1) {
            view.setSingleLine(true);
        } else {
            view.setMaxLines(maxLines);
        }

        if (ellipsize == 0) {
            view.setEllipsize(TextUtils.TruncateAt.END);
            hasMarqueue = false;
        } else if (ellipsize == 1) {
            hasMarqueue = false;
        } else if (ellipsize == 2) {
            hasMarqueue = true;
            rootView.addMarqueeLabelView(this);
        }

        if (maxWidth > 0) {
            view.setMaxWidth(maxWidth);
        }
        if (borderWidth > 0) {
            int padding = borderWidth;
            view.setPadding(padding, padding, padding, padding);
        }


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

    @Override
    protected void parseData(JSONObject jsonObject) {

    }

    @Override
    public View getView() {
        return view;
    }

    @Override
    public void onAttach() {
        if (view != null) {
            view.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            view.setSelected(true);
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.setMarqueeRepeatLimit(-1);
        }
    }
}
