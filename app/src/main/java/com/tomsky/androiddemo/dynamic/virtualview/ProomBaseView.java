package com.tomsky.androiddemo.dynamic.virtualview;

import android.graphics.drawable.GradientDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.tomsky.androiddemo.dynamic.ProomExpression;
import com.tomsky.androiddemo.dynamic.ProomDataCenter;
import com.tomsky.androiddemo.dynamic.ProomDataObsever;
import com.tomsky.androiddemo.dynamic.ProomLayoutManager;
import com.tomsky.androiddemo.dynamic.ProomLayoutUtils;
import com.tomsky.androiddemo.util.ThreadUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class ProomBaseView implements ProomDataObsever {
    public static final String P_NAME = "name";
    public static final String P_PROP = "prop";
    public static final String P_LAYOUT = "layout";
    public static final String P_ID = "id";
    public static final String P_L = "l";
    public static final String P_T = "t";
    public static final String P_R = "r";
    public static final String P_B = "b";
    public static final String P_W = "w";
    public static final String P_H = "h";
    public static final String P_VISIBLE = "visible";
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

    protected String id; // 对应节点的id

    protected int borderWidth = 0;

    protected boolean visible = true; // 是否可见

    protected boolean centerLand = false;
    protected boolean centerPort = false;

    protected boolean widthAuto = false;
    protected boolean heightAuto = false;

    protected Map<String, ProomExpression> datas = new HashMap<>();

    /**
     * 以上是解析出来的属性
     * ======================================
     */

    protected ProomBaseView parentView;

    protected ConstraintLayout.LayoutParams layoutParams;

    protected int viewId; // 对应view的id

    protected boolean needAddView; // 是否需要添加view

    protected boolean valid = true;

    protected boolean hasAttach = false;

    protected AtomicBoolean dataChanged = new AtomicBoolean(false);

    public ProomBaseView(boolean needAddView) {
        this.needAddView = needAddView;
    }

    public final void parseView(JSONObject jsonObject, ProomRootView rootView, ProomBaseView parentView) {
        viewId = ViewCompat.generateViewId();

        JSONObject pObj = jsonObject.optJSONObject(ProomBaseView.P_PROP);
        if (pObj == null) {
            valid = false;
            return;
        }
        String pId = jsonObject.optString(ProomBaseView.P_ID);
        if (TextUtils.isEmpty(pId)) {
            id = String.valueOf(viewId);
        } else {
            id = pId;
        }
        ProomLayoutManager.getInstance().addChildView(id, this);

        if (!parseLayout(pObj)) {
            valid = false;
        }

        View view = generateView(jsonObject, rootView, parentView);
        if (view != null) {
            parseBackground(view, pObj.optJSONObject(ProomBaseView.P_BG_COLOR), pObj.optJSONObject(ProomBaseView.P_ROUND));
        }

        parseViewVisible(pObj);
        parseSubProp(pObj, rootView, parentView);

        parseData(jsonObject.optJSONObject(ProomBaseView.P_DATA));

        this.parentView = parentView;
    }

    /**
     * 生成对应的view
     *
     * @param jsonObject
     * @param rootView
     * @param parentView
     * @return
     */
    protected abstract View generateView(JSONObject jsonObject, ProomRootView rootView, ProomBaseView parentView);

    /**
     * 子类型解析各自特殊的属性
     * @param pObj
     * @param rootView
     * @param parentView
     */
    protected abstract void parseSubProp(JSONObject pObj, ProomRootView rootView, ProomBaseView parentView);

    protected void parseViewVisible(JSONObject pObj) {
        if (pObj.has(P_VISIBLE)) {
            visible = ProomLayoutUtils.parseBoolean(pObj.optString(P_VISIBLE, "1"), true);
            View view = getView();
            if (view != null) {
                view.setVisibility(visible? View.VISIBLE: View.GONE);
            }
        }
    }

    /**
     * 解析表达式，子线程调用
     * @param dataObject
     */
    protected void parseData(JSONObject dataObject) {
        if (dataObject != null) {
            Iterator<String> props = dataObject.keys();
            while (props.hasNext()) {
                String prop = props.next();
                String src = dataObject.optString(prop);
                if (!TextUtils.isEmpty(src)) {
                    datas.put(prop, new ProomExpression(prop, src).parseKey());
                }
            }
        }
        if (datas.size() > 0) {
            ProomDataCenter.getInstance().addObserver(this);
        }
    }

    /**
     * 根据表达式更新属性值，子线程调用
     * @param key
     */
    @Override
    public void onDataChanged(String key) {
        if (datas.size() == 0) return;

        JSONObject data = ProomDataCenter.getInstance().getData();
        Collection<ProomExpression> dataExp = datas.values();
        for (ProomExpression exp: dataExp) {
            if (exp.hasKeyChanged(key)) {
                exp.parseValue(data);
                dataChanged.set(true);
            }
        }
    }

    /**
     * 根据表达式更新控件属性值，主线程调用
     */
    public void updateViewByData() {
        dataChanged.set(false);
        List<ProomExpression> dataList = new ArrayList<>(datas.values());
        for (ProomExpression exp: dataList) {
            updateViewValue(exp.getProp(), exp.getValue());
        }
    }

    protected abstract void updateViewValue(String prop, String value);

    /**
     * H5更新本地view属性，这个是增量更新,支持单个更新
     *
     * @param rootView
     * @param pObj
     */
    public abstract void updateViewPropByH5(ProomRootView rootView, JSONObject pObj);

    /**
     * H5更新本地view数据表达式，这个是整体替换
     *
     * @param dataObject
     */
    public boolean updateViewDataByH5(JSONObject dataObject) {
        boolean hasChanged = false;
        if (dataObject != null) {
            Iterator<String> props = dataObject.keys();
            if (props.hasNext()) {
                datas.clear();
            }
            while (props.hasNext()) {
                String prop = props.next();
                String src = dataObject.optString(prop);
                if (!TextUtils.isEmpty(src)) {
                    datas.put(prop, new ProomExpression(prop, src).parseKey());
                    hasChanged = true;
                }
            }
        }
        ProomDataCenter.getInstance().addObserver(this);

        if (hasChanged) { // 有变化，计算新值并更新到view上
            JSONObject data = ProomDataCenter.getInstance().getData();
            Collection<ProomExpression> dataExp = datas.values();
            for (ProomExpression exp: dataExp) {
                exp.parseValue(data);
                dataChanged.set(true);
            }
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateViewByData();
                }
            });
        }

        return hasChanged;
    }

    /**
     * 第一次加入主布局中，主线程调用
     */
    protected abstract void onAttach();

    /**
     * 从父容器中删除
     */
    public void removeFromParent() {
        View view = getView();
        if (view != null && view.getParent() != null) {
            ((ViewGroup)view.getParent()).removeView(view);
        }
        if (parentView != null && parentView instanceof ProomView) {
            ((ProomView)parentView).removeView(this);
        }
        ProomDataCenter.getInstance().removeObserver(this);
    }

    protected boolean parseLayout(JSONObject pObj) {
        JSONObject layoutObj = pObj.optJSONObject(P_LAYOUT);
        if (layoutObj == null) {
            return false;
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

        return true;
    }


    /**
     * 解析背景色和边框
     *
     * @param view
     * @param bgColorObj
     * @param roundObj
     */
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

    /**
     * 计算控件对应的 LayoutParams
     *
     * @param rootView
     * @param parentView
     * @return
     */
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
