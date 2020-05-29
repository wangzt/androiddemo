package com.tomsky.androiddemo.activity;

import android.app.PictureInPictureParams;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Rational;
import android.view.View;
import android.widget.RelativeLayout;

import com.tomsky.androiddemo.R;
import com.tomsky.androiddemo.util.LogUtils;
import com.tomsky.androiddemo.util.UIUtils;

/**
 * Created by wangzhitao on 2020/05/29
 **/
public class PicInPicActivity extends FragmentActivity {

    private static final String TAG = "PicInPicActivity";

    private PictureInPictureParams.Builder mPictureInPictureParamsBuilder;

    private View centerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picinpic);
        centerView = findViewById(R.id.pic_center_view);
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        if (isInPictureInPictureMode) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) centerView.getLayoutParams();
            if (lp != null) {
                lp.setMargins(0, -UIUtils.dp2px(100), 0, 0);
            }
        } else {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) centerView.getLayoutParams();
            if (lp != null) {
                lp.setMargins(0, UIUtils.dp2px(100), 0, 0);
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
                LogUtils.d(TAG, "width="+width+", height="+height+", bounds="+bounds);
                Rational aspectRatio = new Rational(bounds.width(), bounds.height());
                mPictureInPictureParamsBuilder.setSourceRectHint(bounds);
                mPictureInPictureParamsBuilder.setAspectRatio(aspectRatio);
            }

            if (isInPictureInPictureMode()) {
                setPictureInPictureParams(mPictureInPictureParamsBuilder.build());
            } else {
                enterPictureInPictureMode(mPictureInPictureParamsBuilder.build());
            }
        }
    }
}
