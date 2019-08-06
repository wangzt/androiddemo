package com.tomsky.androiddemo.dynamic.virtualview;

import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.LinearLayout;


import com.tomsky.androiddemo.dynamic.ProomLayoutManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProomView extends ProomBaseView {
    public static final String NAME = "view";

    public static final String P_CHILD = "child";

    private View view;

    private List<ProomBaseView> child = new ArrayList<>();


    @Override
    protected View generateView(JSONObject jsonObject, ProomRootView rootView, ProomBaseView parentView) {
        child.clear();

        List<ProomBaseView> subViews = new ArrayList<>();
        boolean absoluteConstraint = false; // 是否需要做成独立的ConstraintLayout
        if (jsonObject.has(P_CHILD)) {
            JSONArray child = jsonObject.optJSONArray(P_CHILD);
            if (child != null) {
                int size = child.length();
                if (size > 0) {
                    if (centerLand || centerPort) {
                        isLinearLayout = true;
                    }
                }
                for (int i = 0; i < size; i++) {
                    ProomBaseView subView = ProomLayoutManager.parseSubView((JSONObject) child.opt(i), rootView, this);
                    if (subView != null) {
                        if (subView.centerLand || subView.centerPort) {
                            absoluteConstraint = true;
                        }
                        subViews.add(subView);
                    }
                }
            }
        }

        if (subViews.size() > 0) {
            if (centerLand || centerPort) {
                LinearLayout layout = new LinearLayout(rootView.getContext());
                if (widthAudo) {
                    layout.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, h);
                    layout.setLayoutParams(lp);
                } else if (heightAudo) {
                    layout.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(w, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layout.setLayoutParams(lp);
                } else {
                    layout.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(w, h);
                    layout.setLayoutParams(lp);
                }
                for (ProomBaseView subView: subViews) {
                    if (subView.getView() != null) {
                        layout.addView(subView.getView());
                    }
                }
                view = layout;

            } else {
                if (absoluteConstraint) {
                    ConstraintLayout layout = new ConstraintLayout(rootView.getContext());
                    ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(w, h);
                    layout.setLayoutParams(lp);
                    for (ProomBaseView subView: subViews) {
                        if (subView.getView() != null) {
                            if (subView.centerLand) {
                                int width = subView.w;
                                int height = subView.h;
                                if (subView.widthAudo) {
                                    width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
                                }
                                ConstraintLayout.LayoutParams subLp = new ConstraintLayout.LayoutParams(width, height);
                                subLp.leftToLeft = viewId;
                                subLp.rightToRight = viewId;
                                layout.addView(subView.getView(), subLp);
                            } else if (subView.centerPort) {
                                int width = subView.w;
                                int height = subView.h;
                                if (subView.heightAudo) {
                                    height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
                                }
                                ConstraintLayout.LayoutParams subLp = new ConstraintLayout.LayoutParams(width, height);
                                subLp.topToTop = viewId;
                                subLp.bottomToBottom = viewId;
                                layout.addView(subView.getView(), subLp);
                            } else {
                                layout.addView(subView.getView());
                            }
                        }
                    }
                    view = layout;
                } else {
                    view = new View(rootView.getContext());
                    ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(w, h);
                    view.setLayoutParams(lp);

                    for (ProomBaseView subView: subViews) {
                        if (subView.getView() != null) {
                            rootView.addView(subView.getView());
                        }
                    }
                }
            }
            child.addAll(subViews);
        } else {
            view = new View(rootView.getContext());
            int rootId = rootView.getId();
            if (parentView != null) {
                if (parentView.isLinearLayout) {
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(w, h);
                    lp.leftMargin = l;
                    lp.topMargin = t;
                    view.setLayoutParams(lp);
                } else {
                    if (centerLand) {
                        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(w, h);
                        lp.topMargin = t;
                        lp.leftToLeft = parentView.viewId;
                        lp.rightToRight = parentView.viewId;
                        view.setLayoutParams(lp);
                    } else if (centerPort) {
                        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(w, h);
                        lp.leftMargin = l;
                        lp.topToTop = parentView.viewId;
                        lp.bottomToBottom = parentView.viewId;
                        view.setLayoutParams(lp);
                    } else {
                        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(w, h);
                        if (rootLeft > Integer.MIN_VALUE && rootTop > Integer.MIN_VALUE) {
                            lp.leftToLeft = rootId;
                            lp.topToTop = rootId;
                            lp.leftMargin = rootLeft;
                            lp.topMargin = rootTop;
                        } else {
                            lp.leftToLeft = parentView.viewId;
                            lp.topToTop = parentView.viewId;
                            lp.leftMargin = l;
                            lp.topMargin = t;
                        }
                        view.setLayoutParams(lp);
                    }
                }
            } else {
                ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(w, h);
                lp.leftToLeft = rootId;
                lp.topToTop = rootId;
                lp.leftMargin = l;
                lp.topMargin = t;
                view.setLayoutParams(lp);
            }
        }

        if (view != null) {
            view.setId(viewId);
        }

        return view;
    }

    @Override
    protected void parseSubProp(JSONObject pObj, ProomRootView rootView, ProomBaseView parentView) {

    }

    @Override
    protected void parseData(JSONObject jsonObject) {

    }

    @Override
    public View getView() {
        return view;
    }
}
