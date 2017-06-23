package com.tomsky.androiddemo.widget.sticker;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by j-wangzhitao on 17-6-22.
 */

public class StickerImage extends Sticker {

    public StickerImage() {
        super(TYPE_IMAGE);
    }

    public void onDraw(Canvas canvas) {
        // do nothing
        canvas.drawRect(mRect, mLinePaint);
    }

    public Bitmap capture() {
        return null;
    }
}
