package com.tomsky.androiddemo.widget;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.view.MotionEventCompat;

import com.tomsky.androiddemo.util.LogUtils;

/**
 * Created by wangzhitao on 8/3/16.
 */
public class MyDragListener2 implements View.OnTouchListener {

    static final int NONE = 0;
    static final int DRAG = 1; // 拖动
    static final int ZOOM = 2; // 缩放

    private int mode = NONE;

    private int startX, startY;
    private int lastX, lastY;
    private int screenWidth, screenHeight;
    private int right, bottom;

    private float piovtX = 0.5f;
    private float piovtY = 0.5f;
    private float beforeLength, afterLength;
    private float scale;
    private float maxScale = 2.0f;
    private float minScale = 1.0f;
    private int originalWidth, originalHeight;
    private int startWidth, startHeight;
    private float zoomStartX, zoomStartY;

    private boolean isClick = false;

// true 既可以拖拽，又可以缩放
    private boolean supportZoom = false;
    private boolean supportDrag = true;

    private View dragView;

    private float fx = -1;
    private float fy = -1;

    private OnDragTouchCallback onDragTouchCallback;

    public void setOnDragTouchCallback(OnDragTouchCallback listener){
        onDragTouchCallback = listener;
    }

    public MyDragListener2(View dragView) {
        this.dragView = dragView;
    }

    public void setScreenSize(int width, int height) {
        this.screenWidth = width;
        this.screenHeight = height;
    }

    public void setSupportZoom(boolean supportZoom) {
        this.supportZoom = supportZoom;
    }

    public boolean isSupportDrag() {
        return supportDrag;
    }

    public void setSupportDrag(boolean supportDrag) {
        this.supportDrag = supportDrag;
    }

    public void resetPosition(){
        if(dragView != null){
            ViewGroup.LayoutParams lp = dragView.getLayoutParams();
            if (lp != null && originalWidth !=0 && originalHeight != 0) {
                lp.width = (int)originalWidth;
                lp.height = (int)originalHeight;
                dragView.setLayoutParams(lp);
            }

            if(fx != -1 && fy != -1 && fx != 0 && fy != 0){
                dragView.setX(fx);
                dragView.setY(fy);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action=event.getAction();
        switch(action & MotionEventCompat.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                LogUtils.d("wzt-drag", "ACTION_DOWN, Touch:id = "+v.getId());

                if (onDragTouchCallback != null) {
                    onDragTouchCallback.onDragTouchDown();
                }

                mode = DRAG;
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                startX = lastX;
                startY = lastY;

                right = screenWidth - v.getWidth();
                bottom = screenHeight - v.getHeight();
                if (originalWidth == 0) {
                    originalWidth = v.getWidth();
                    originalHeight = v.getHeight();

                    float _fx = v.getX();
                    float _fy = v.getY();

                    if (dragView != null) {
                        _fx = dragView.getX();
                        _fy = dragView.getY();
                    }

                    fx = _fx;
                    fy = _fy;
                }
                isClick = true;
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                center = centerPostion(event);

                if(dragView != null){
                    piovtX = center[0] / dragView.getWidth();
                    piovtY = center[1] / dragView.getHeight();

                    startWidth = dragView.getWidth();
                    startHeight = dragView.getHeight();
                }else{
                    piovtX = center[0] / v.getWidth();
                    piovtY = center[1] / v.getHeight();

                    startWidth = v.getWidth();
                    startHeight = v.getHeight();
                }

                if(dragView != null){
                    zoomStartX = dragView.getX();
                    zoomStartY = dragView.getY();
                }else{
                    zoomStartX = v.getX();
                    zoomStartY = v.getY();
                }

                center[0] = center[0] + zoomStartX;
                center[1] = center[1] + zoomStartY;

                float space = spacing(event);
                LogUtils.d("wzt-drag", "ACTION_POINTER_DOWN, space:"+space+", piovtX:"+piovtX+", piovtY:"+piovtY+", centerX:"+center[0]+", centerY:"+center[1]+",zoomStartX:"+zoomStartX+", zoomStartY:"+zoomStartY);
                if (space > 10f && supportZoom) {
                    mode = ZOOM;
                    beforeLength = space;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) { // 处理拖动
                    if (supportDrag) {

                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;

                        float x = v.getX();
                        float y = v.getY();

                        if (dragView != null) {
                            x = dragView.getX();
                            y = dragView.getY();
                        }

                        x = x + dx;
                        y = y + dy;
                        if (x < 0) {
                            x = 0;
                        } else if (x > right) {
                            x = right;
                        }
                        if (y < 0) {
                            y = 0;
                        } else if (y > bottom) {
                            y = bottom;
                        }

                        if (dragView != null) {
                            dragView.setX(x);
                            dragView.setY(y);
                        } else {
                            v.setX(x);
                            v.setY(y);
                        }

                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        if (Math.abs(startX - lastX) > 6) {
                            isClick = false;
                        }
                    }
                } else if (mode == ZOOM) { // 处理缩放
                    float space2 = spacing(event);
                    if (space2 > 10f && supportZoom) {
                        afterLength = space2;
                        float gapLength = afterLength - beforeLength;
                        if (Math.abs(gapLength) > 5f) {

                            float rScale = afterLength/beforeLength;
                            scale = startWidth * rScale / originalWidth;

                            LogUtils.d("wzt-drag", "=======originalWidth:"+originalWidth+", startWidth:"+ startWidth +", scale:"+scale);
                            if (scale >= minScale && scale <= maxScale) {

                                if(scale == maxScale){
//                                    ToastUtils.showToast(v.getContext(), "窗口已最大",true);
                                }

                                float w = startWidth * rScale;
                                float h = startHeight *rScale;

                                float l = center[0] - w * piovtX;
                                float t = center[1] - h * piovtY;

                                if(dragView != null){
                                    dragView.setX(l);
                                    dragView.setY(t);
                                    ViewGroup.LayoutParams lp = dragView.getLayoutParams();
                                    if (lp != null) {
                                        lp.width = (int)w;
                                        lp.height = (int)h;
                                        dragView.setLayoutParams(lp);
                                    }
                                }else{
                                    v.setX(l);
                                    v.setY(t);
                                    ViewGroup.LayoutParams lp = v.getLayoutParams();
                                    if (lp != null) {
                                        lp.width = (int)w;
                                        lp.height = (int)h;
                                        v.setLayoutParams(lp);
                                    }
                                }


                                zoomStartX = l;
                                zoomStartY = t;
                            }else if(scale > maxScale){
                                Toast.makeText(v.getContext(), "窗口已最大",Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isClick && mode == DRAG) {
                    LogUtils.d("wzt-drag", "ACTION_Click");
                    v.performClick();
                }
                LogUtils.d("wzt-drag", "ACTION_UP, actionIndex:"+event.getActionIndex());
                v.setVisibility(View.VISIBLE);
                if (onDragTouchCallback != null) {
                    onDragTouchCallback.onDragTouchUp();
                }
                isClick = false;
                mode = NONE;
                return true;

            case MotionEvent.ACTION_POINTER_UP:
                LogUtils.d("wzt-drag", "ACTION_POINTER_UP, actionIndex:"+event.getActionIndex());
                isClick = false;
                mode = NONE;
                break;
        }
        return false;
    }

    /**
     * 计算两点间的距离
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private float[] center;

    /**
     * 计算两点间的中心点
     */
    private float[] centerPostion(MotionEvent event) {
        float[] center = new float[2];
        float x = event.getX(0);
        float y = event.getY(0);
        float x1 = event.getX(1);
        float y1 = event.getY(1);
        /* x,y分别的距离 */
        center[0] = Math.abs((x1 - x) / 2);
        center[1] = Math.abs((y1 - y) / 2);
        center[0] = Math.max(x, x1) - center[0];
        center[1] = Math.max(y, y1) - center[1];
        return center;
    }

    public interface OnDragTouchCallback{
        void onDragTouchDown();
        void onDragTouchUp();
    }
}
