package com.tomsky.androiddemo.widget.sticker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.tomsky.androiddemo.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by j-wangzhitao on 17-6-22.
 */

public class StickerView extends View {

    private List<Sticker> mItems = new ArrayList<>();

    private Sticker mCurrentItem;

    private RectF mDeleteRect = new RectF();

    private boolean mForDelete = false;

    private StickerDeleteListener mDeleteListener;

    public StickerView(Context context) {
        super(context);
    }

    public StickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setDeleteListener(StickerDeleteListener deleteListener) {
        this.mDeleteListener = deleteListener;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mDeleteRect.set(left, bottom - UIUtils.dp2px(120), right, bottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean handled = false;

        float x = event.getX();
        float y = event.getY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mForDelete = false;
                if (canHandleTouch(x, y) && mCurrentItem != null) {
                    mCurrentItem.onDown(x, y);
                    handled = true;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (mCurrentItem != null) {
                    mCurrentItem.onMove(x, y, getWidth(), getHeight());
                    if (mDeleteRect.contains(mCurrentItem.centerX(), mCurrentItem.centerY())) {
                        if (!mForDelete && mDeleteListener != null) {
                            // change ui
                            mDeleteListener.changeDeleteState(true);
                        }
                        mForDelete = true;
                    } else {
                        if (mForDelete && mDeleteListener != null) {
                            // change ui
                            mDeleteListener.changeDeleteState(false);
                        }
                        mForDelete = false;
                    }
                    invalidate();
                    handled = true;
                }
                break;

            case MotionEvent.ACTION_CANCEL:
                if (mCurrentItem != null) {
                    handled = true;
                }
                mCurrentItem = null;
                mForDelete = false;
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                if (mCurrentItem != null) {
                    if (mDeleteRect.contains(mCurrentItem.centerX(), mCurrentItem.centerY())) {
                        deleteItem(mCurrentItem);
                        // delete
                        if (mDeleteListener != null) {
                            mDeleteListener.onDelete(mCurrentItem);
                        }
                    }
                    handled = true;
                }
                mCurrentItem = null;
                mForDelete = false;
                invalidate();
                break;
        }

        return handled;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (Sticker item: mItems) {
            item.onDraw(canvas);
        }
    }

    private boolean canHandleTouch(float x, float y) {
        mCurrentItem = null;

        boolean canTouch = false;
        int size = mItems.size();
        if (size > 0) {
            Sticker item = null;
            for (int i = size - 1; i > -1; i--) {
                item = mItems.get(i);
                if (item != null && item.isPointInRect(x, y)) {
                    mCurrentItem = item;
                    canTouch = true;
                    break;
                }
            }
        }

        return canTouch;
    }

    public void addItem(Sticker item) {
        int width = UIUtils.dp2px(150);
        int height = UIUtils.dp2px(80);
        item.setRect(0,0,width, height);

        mItems.add(item);
//        if (item.getType() == StickerImage.TYPE_TEXT) {
            postInvalidate();
//        }
    }

    private void deleteItem(Sticker item) {
        mItems.remove(item);
    }

    public Bitmap captureItem(int i) {
        if (i > -1 && i < mItems.size()) {
            Sticker item = mItems.get(i);
            return item.capture();
        }
        return null;
    }

    public interface StickerDeleteListener {
        void changeDeleteState(boolean delete);
        void onDelete(Sticker item);
    }

}
