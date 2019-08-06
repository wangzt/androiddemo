package com.tomsky.androiddemo.dynamic.virtualview;

import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tomsky.androiddemo.dynamic.ProomLayoutUtils;
import com.tomsky.androiddemo.util.UIUtils;

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
    private int ellipsize;
    private int maxLines = 1;
    private int gravity;

    private TextView view;

    private boolean hasMarqueue;// 是否需要跑马灯

    @Override
    protected View generateView(JSONObject jsonObject, ProomRootView rootView, ProomBaseView parentView) {
        view = new TextView(rootView.getContext());

        if (parentView == null) {
            int width = w;
            int height = h;
            if (widthAudo) {
                width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
            } else if (heightAudo) {
                height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
            }
            ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(width, height);
            int rootId = rootView.getId();
            if (centerLand) {
                lp.leftToLeft = rootId;
                lp.rightToRight = rootId;
                lp.topToTop = rootId;
                lp.topMargin = t;
            } else if (centerPort) {
                lp.topToTop = rootId;
                lp.bottomToBottom = rootId;
            } else {
                lp.leftToLeft = rootId;
                lp.topToTop = rootId;
                lp.leftMargin = l;
                lp.topMargin = t;
            }
            view.setLayoutParams(lp);
        } else {
            if (parentView.isLinearLayout) {
                int width = w;
                int height = h;
                if (widthAudo) {
                    width = LinearLayout.LayoutParams.WRAP_CONTENT;
                } else if (heightAudo) {
                    height = LinearLayout.LayoutParams.WRAP_CONTENT;
                }
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
                lp.leftMargin = l;
                lp.topMargin = t;
                view.setLayoutParams(lp);
            } else {
                int width = w;
                int height = h;
                if (widthAudo) {
                    width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
                } else if (heightAudo) {
                    height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
                }
                ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(width, height);
                int parentId = parentView.viewId;
                if (centerLand) {
                    lp.leftToLeft = parentId;
                    lp.rightToRight = parentId;
                } else if (centerPort) {
                    lp.topToTop = parentId;
                    lp.bottomToBottom = parentId;
                } else {
                    if (rootLeft > Integer.MIN_VALUE && rootTop > Integer.MIN_VALUE) {
                        lp.leftToLeft = rootView.getId();
                        lp.topToTop = rootView.getId();
                        lp.leftMargin = rootLeft;
                        lp.topMargin = rootTop;
                    } else {
                        lp.leftToLeft = parentId;
                        lp.topToTop = parentId;
                        lp.leftMargin = l;
                        lp.topMargin = t;
                    }

                }
                view.setLayoutParams(lp);
            }
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
        } else if (ellipsize == 2) {
            hasMarqueue = true;
            rootView.addMarqueeLabelView(this);
        } else {
            hasMarqueue = false;
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

    public boolean hasMarquee() {
        return hasMarqueue;
    }

    public void setMarquee() {
        if (view != null) {
            view.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            view.setSelected(true);
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.setMarqueeRepeatLimit(-1);
        }
    }
}
