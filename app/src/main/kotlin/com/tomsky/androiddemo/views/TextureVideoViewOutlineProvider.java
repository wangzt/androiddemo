package com.tomsky.androiddemo.views;

import android.graphics.Outline;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewOutlineProvider;

public class TextureVideoViewOutlineProvider extends ViewOutlineProvider {
        private float mRadius;
     
        public TextureVideoViewOutlineProvider(float radius) {
            this.mRadius = radius;
        }
     
        @Override
        public void getOutline(View view, Outline outline) {
            Rect rect = new Rect();
            view.getGlobalVisibleRect(rect);
//            int leftMargin = 0;
//            int topMargin = 0;
//            Rect selfRect = new Rect(leftMargin, topMargin,
//                    rect.right - rect.left - leftMargin, rect.bottom - rect.top - topMargin);
//            outline.setRoundRect(selfRect, mRadius);

            setTriangleConvexPath(rect, outline);
        }

        private void setTriangleConvexPath(Rect rect, Outline outline) {
            int width = rect.right - rect.left;
            int height = rect.bottom - rect.top;
            Path path = new Path();

            path.rewind();
            //设置起点
            path.moveTo(width / 2, 0);
            path.lineTo(0, height);
            path.lineTo(width, height);
            //闭合路径
            path.close();
            boolean isConvex = path.isConvex();
            Log.i("wzt-outline", "isConvex:"+isConvex);
            outline.setConvexPath(path);
        }
}