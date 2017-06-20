package com.tomsky.androiddemo.widget;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.tomsky.androiddemo.R;
import com.tomsky.androiddemo.util.UIUtils;


public abstract class BasePannel extends PopupWindow {
    public static final int DEFAULT_PORT_HEIGHT = 232; // 默认高度

    private View mContentView;
    protected Activity mContext;

    public BasePannel(Activity context) {
        super(context);
        this.mContext = context;

        this.mContentView= getContentView(context);
        initView();
        setContentView(mContentView);

        initAttributes();
    }

    protected void initAttributes(){
        setWidth(getWindowWidth());
        setHeight(getWindowHeight());

        setClippingEnabled(false);
        setFocusable(true);
        setOutsideTouchable(true);
        setAnimationStyle(getAnimStyle());
        ColorDrawable dw = new ColorDrawable(mContext.getResources().getColor(getBgColor()));
        setBackgroundDrawable(dw);
    }

    protected View getContentView(Activity context){
        if(getLayoutId()!=-1){
            return LayoutInflater.from(context).inflate(getLayoutId(),null);
        }
        return null;
    }

    protected int getWindowWidth(){
        return ViewGroup.LayoutParams.MATCH_PARENT;
    }

    protected int getWindowHeight() {
        return UIUtils.dp2px(DEFAULT_PORT_HEIGHT);
    }

    protected int getAnimStyle(){
        return R.style.BottomDialogAnimation;
    }

    protected int getBgColor(){
        return R.color.default_pannel_bg;
    }

    protected Activity getActivity(){
        return mContext;
    }

    /**
     * 获取layoutId
     */
    protected abstract int getLayoutId();
    /**
     * 初始化view相关信息
     */
    protected abstract void initView();

    protected View findViewById(int id){
        if (mContentView==null) return null;
        return mContentView.findViewById(id);
    }

    public void show() {
        showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }
}
