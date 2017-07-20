package com.tomsky.androiddemo.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.tomsky.androiddemo.R;
import com.tomsky.androiddemo.widget.sticker.Sticker;
import com.tomsky.androiddemo.widget.sticker.StickerImage;
import com.tomsky.androiddemo.widget.sticker.StickerText;
import com.tomsky.androiddemo.widget.sticker.StickerView;

/**
 * Created by j-wangzhitao on 17-6-22.
 */

public class StickerActivity extends Activity implements StickerView.StickerDeleteListener {

    private StickerView mStickerView;
    private View mDeleteContainer;

    private View mCenterView;
    private ViewGroup mContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker);

        mContainer = (ViewGroup) findViewById(R.id.sticker_container);
        mStickerView = (StickerView) findViewById(R.id.sticker_view);
        mDeleteContainer = findViewById(R.id.delete_container);

        mCenterView = findViewById(R.id.center_view);

        mStickerView.setDeleteListener(this);

        findViewById(R.id.add_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StickerText item = new StickerText(StickerActivity.this, mContainer);
                mStickerView.addItem(item);
            }
        });

        findViewById(R.id.capture_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bmp = mStickerView.captureItem(0);
                if (bmp != null) {
                    mCenterView.setBackgroundDrawable(new BitmapDrawable(bmp));
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void changeDeleteState(boolean delete) {
        if (mDeleteContainer != null) {
            if (delete) {
                mDeleteContainer.setSelected(true);
            } else {
                mDeleteContainer.setSelected(false);
            }
        }
    }

    @Override
    public void onDelete(Sticker item) {
        if (mDeleteContainer != null) {
            mDeleteContainer.setSelected(false);
        }
    }
}
