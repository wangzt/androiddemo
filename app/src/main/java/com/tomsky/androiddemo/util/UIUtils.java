package com.tomsky.androiddemo.util;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.tomsky.androiddemo.BaseApplication;

/**
 * @author Jungly
 * jungly.ik@gmail.com
 * 15/3/8 10:07
 */
public class UIUtils {

    public static int px2sp(float pxValue) {
        final float fontScale = BaseApplication.getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int dp2px(int dp) {
        DisplayMetrics displayMetrics = BaseApplication.getContext().getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + 0.5);
    }

    public static int getStatusBarHeight() {
        int height = 0;
        int resourceId = BaseApplication.getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = BaseApplication.getContext().getResources().getDimensionPixelSize(resourceId);
        }

        return height;
    }

    public static void displayImage(SimpleDraweeView view, String uri) {
        displayImage(view, uri, true, false, null);
    }

    public static void displayImage(SimpleDraweeView view, String uri, boolean autoPlayAnimations){
        displayImage(view, uri, autoPlayAnimations, false, null);
    }

    public static void displayImage(SimpleDraweeView view, String uri, boolean autoPlayAnimations, boolean tapToRetry, ControllerListener controllerListener) {
        if(TextUtils.isEmpty(uri)) return;
        ImageRequest imageRequest =
                ImageRequestBuilder.newBuilderWithSource(Uri.parse(uri))
//                        .setResizeOptions(new ResizeOptions(view.getLayoutParams().width, view.getLayoutParams().height))
                        .setProgressiveRenderingEnabled(true)
                        .setImageDecodeOptions(getImageDecodeOptions(view))
                        .setAutoRotateEnabled(true)//旋转角度
                        .build();
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest)
                .setOldController(view.getController())
                .setTapToRetryEnabled(tapToRetry)
                .setControllerListener(controllerListener)
//                .setControllerListener(view.getListener())
                .setAutoPlayAnimations(autoPlayAnimations)
                .build();
        view.setController(draweeController);
    }

    private static ImageDecodeOptions getImageDecodeOptions(SimpleDraweeView view) {
        if (view != null && view.getHierarchy() != null && view.getHierarchy().getRoundingParams() != null) {
            if (view.getHierarchy().getRoundingParams().getRoundAsCircle() || view.getHierarchy().getRoundingParams().getCornersRadii() != null) {
                return forceStaticDecodeOption;
            }
        }
        return ImageDecodeOptions.defaults();
    }

    // 强制使用静态图
    private static ImageDecodeOptions forceStaticDecodeOption = ImageDecodeOptions.newBuilder().setForceStaticImage(true).build();

}
