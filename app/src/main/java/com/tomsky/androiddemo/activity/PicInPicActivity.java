package com.tomsky.androiddemo.activity;

import android.app.PictureInPictureParams;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.util.Rational;
import android.view.View;
import android.widget.RelativeLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.fresco.animation.backend.AnimationBackend;
import com.facebook.fresco.animation.backend.AnimationBackendDelegate;
import com.facebook.fresco.animation.drawable.AnimatedDrawable2;
import com.facebook.fresco.animation.drawable.AnimationListener;
import com.facebook.imagepipeline.image.ImageInfo;
import com.tomsky.androiddemo.R;
import com.tomsky.androiddemo.util.LogUtils;
import com.tomsky.androiddemo.util.UIUtils;

/**
 * Created by wangzhitao on 2020/05/29
 **/
public class PicInPicActivity extends FragmentActivity {

    private static final String TAG = "PicInPicActivity";

    private PictureInPictureParams.Builder mPictureInPictureParamsBuilder;

    private View containerView;
    private View centerView;

    private SimpleDraweeView simpleDraweeView;
    private boolean mIsInPictureInPictureMode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picinpic);
        centerView = findViewById(R.id.pic_center_view);
        simpleDraweeView = findViewById(R.id.test_webp_img);
        containerView = findViewById(R.id.pic_container);
        findViewById(R.id.pic_root_view).addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (mIsInPictureInPictureMode) {
                    LogUtils.e(TAG, "----onLayoutChange:"+left+","+top+","+right+","+bottom);
                    int[] loc = new int[2];
                    getWindow().getDecorView().getLocationOnScreen(loc);
                    LogUtils.e(TAG, "----getLocationOnScreen:"+loc[0]+","+loc[1]);
                    Rect rect = new Rect();
                    getWindow().getDecorView().getGlobalVisibleRect(rect);
                    LogUtils.e(TAG, "----getGlobalVisibleRect:"+rect);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onStop() {
        super.onStop();
        if (mIsInPictureInPictureMode) {
            LogUtils.e(TAG, "----onStop in pip");
            finish();
        }
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        LogUtils.e(TAG, "----onUserLeaveHint");
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        mIsInPictureInPictureMode = isInPictureInPictureMode;
        if (isInPictureInPictureMode) {
            if (containerView != null) {
                containerView.setVisibility(View.GONE);
            }

        } else {
            if (containerView != null) {
                containerView.setVisibility(View.VISIBLE);
            }
        }
    }

    public void picinpic(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && getPackageManager().hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)) {

            if (mPictureInPictureParamsBuilder == null) {
                mPictureInPictureParamsBuilder = new PictureInPictureParams.Builder();
            }

            View decorView = getWindow().getDecorView();
            int width = decorView.getWidth();
            int height = decorView.getHeight();
            int top = UIUtils.dp2px(100);

            if (width > 0 && height > 0) {
                Rect bounds = new Rect();
                centerView.getDrawingRect(bounds);
                bounds.top += top;
                bounds.bottom += top;
                LogUtils.e(TAG, "-----width="+width+", height="+height+", bounds="+bounds);
                Rational aspectRatio = new Rational(bounds.width(), bounds.height());
//                mPictureInPictureParamsBuilder.setSourceRectHint(bounds);
                mPictureInPictureParamsBuilder.setAspectRatio(aspectRatio);
            }

            if (mIsInPictureInPictureMode) {
                setPictureInPictureParams(mPictureInPictureParamsBuilder.build());
            } else {
                enterPictureInPictureMode(mPictureInPictureParamsBuilder.build());
            }
        }
    }

    public void testWebp(View v) {
        final DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setAutoPlayAnimations(true)
                .setUri(Uri.parse(getResUri(R.drawable.link_pk_win)))
                .setControllerListener(new BaseControllerListener<ImageInfo>() {
                    @Override
                    public void onFinalImageSet(
                            String id,
                            @Nullable ImageInfo imageInfo,
                            @Nullable Animatable animatable) {
                        if (animatable instanceof AnimatedDrawable2) {
                            AnimatedDrawable2 animatedDrawable = (AnimatedDrawable2) animatable;
                            animatedDrawable.setAnimationBackend(new LoopCountModifyingBackend(animatedDrawable.getAnimationBackend(), 1));//设置循环次数
                            animatedDrawable.setAnimationListener(new AnimationListener() {
                                @Override
                                public void onAnimationStart(AnimatedDrawable2 drawable) {
                                    LogUtils.i(TAG, "onAnimationStart");
                                }

                                @Override
                                public void onAnimationStop(AnimatedDrawable2 drawable) {
                                    LogUtils.i(TAG, "onAnimationStop");
                                }

                                @Override
                                public void onAnimationReset(AnimatedDrawable2 drawable) {

                                }

                                @Override
                                public void onAnimationRepeat(AnimatedDrawable2 drawable) {

                                }

                                @Override
                                public void onAnimationFrame(AnimatedDrawable2 drawable, int frameNumber) {

                                }
                            });
                            LogUtils.i(TAG, "anim duration="+animatedDrawable.getLoopDurationMs());
                            animatedDrawable.start();
                        }
                    }
                })
                .build();
        simpleDraweeView.setController(controller);
    }

    private static final String drawableResUri = "res://com.huajiao/";

    public static String getResUri(int drawableId) {
        StringBuilder sb = new StringBuilder(drawableResUri);
        sb.append(drawableId);
        return sb.toString();
    }

    public class LoopCountModifyingBackend extends AnimationBackendDelegate {

        private int mLoopCount;

        public LoopCountModifyingBackend(
                @Nullable AnimationBackend animationBackend,
                int loopCount) {
            super(animationBackend);
            mLoopCount = loopCount;
        }

        @Override
        public int getLoopCount() {
            return mLoopCount;
        }
    }
}
