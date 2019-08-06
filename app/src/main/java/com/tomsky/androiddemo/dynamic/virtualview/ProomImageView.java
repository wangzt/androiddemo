package com.tomsky.androiddemo.dynamic.virtualview;

import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.view.View;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tomsky.androiddemo.dynamic.ProomLayoutUtils;
import com.tomsky.androiddemo.util.UIUtils;

import org.json.JSONObject;

public class ProomImageView extends ProomBaseView {

    public static final String NAME = "image";

    public static final String P_SRC = "src";
    public static final String P_DEFAULT_SRC = "defaultSrc";
    public static final String P_CONTENT_MODE = "contentMode";

    private String src;
    private String defaultSrc;
    private int contentMode; // 0: centerCorp, 1: fit, 2: fill

    private SimpleDraweeView view;

    @Override
    protected View generateView(JSONObject jsonObject, ProomRootView rootView, ProomBaseView parentView) {
        view = new SimpleDraweeView(rootView.getContext());

        layoutParams = calcLayoutParams(rootView, parentView);
        view.setLayoutParams(layoutParams);
        if (parentView == null) {
            rootView.addView(view);
        }

        return view;
    }

    @Override
    protected void parseSubProp(JSONObject pObj, ProomRootView rootView, ProomBaseView parentView) {
        src = pObj.optString(P_SRC);
        defaultSrc = pObj.optString(P_DEFAULT_SRC);
        contentMode = pObj.optInt(P_CONTENT_MODE, 0);

        GenericDraweeHierarchy hierarchy = view.getHierarchy();
        if (contentMode == 0) {
            hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
        } else if (contentMode == 1) {
            hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);
        } else if (contentMode == 2) {
            hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY);
        }

        JSONObject roundObj = pObj.optJSONObject(P_ROUND);
        if (roundObj != null) {
            double borderW = roundObj.optDouble(ProomBaseView.P_BORDER_WIDTH, -1);
            double cornerRadius = roundObj.optDouble(ProomBaseView.P_CORNER_RADIUS, -1);

            JSONObject borderColorObj = roundObj.optJSONObject(ProomBaseView.P_BORDER_COLOR);

            if (cornerRadius > 0) {
                RoundingParams roundingParams = RoundingParams.fromCornersRadius(ProomLayoutUtils.scaleFloatSize((float)cornerRadius));
                if (borderColorObj != null) {
                    int borderColor = ProomLayoutUtils.parseColor(borderColorObj.optString(ProomBaseView.P_COLOR), (float)borderColorObj.optDouble(ProomBaseView.P_ALPHA, -1));
                    if (borderW > 0) {
                        borderWidth = ProomLayoutUtils.scaleSize((float) borderW);
                        roundingParams.setBorder(borderColor, borderWidth);
                    }
                }
                hierarchy.setRoundingParams(roundingParams);
            }
        }

        UIUtils.displayImage(view, src);
    }

    @Override
    protected void parseBackground(View view, JSONObject bgColorObj, JSONObject roundObj) {
        // doNothing
    }

    @Override
    protected void parseData(JSONObject dataObject) {
    }

    @Override
    protected void onAttach() {

    }

    @Override
    public View getView() {
        return view;
    }
}