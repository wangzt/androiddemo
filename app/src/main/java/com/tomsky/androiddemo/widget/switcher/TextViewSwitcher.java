package com.tomsky.androiddemo.widget.switcher;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.tomsky.androiddemo.R;
import com.tomsky.androiddemo.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

public class TextViewSwitcher extends TextSwitcher implements ISwitcher {

    private static final int DEFAULT_SIZE = UIUtils.dp2px(14);
    private static final int DEFAULT_COLOR = 0xff000000;

    private int textSize = DEFAULT_SIZE;
    private int textColor = DEFAULT_COLOR;
    private int textGravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;

    private int animIn = R.anim.fade_in_slide_in;
    private int animOut = R.anim.fade_out_slide_out;

    private SwitcherClickListener mClickListener;

    private List<String> mTexts = new ArrayList<>();
    private int mPosition;

    public TextViewSwitcher(Context context) {
        super(context);

        init();
    }

    public TextViewSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextViewSwitcher);
        textSize = a.getDimensionPixelSize(R.styleable.TextViewSwitcher_tx_size, DEFAULT_SIZE);
        textColor = a.getColor(R.styleable.TextViewSwitcher_tx_color, DEFAULT_COLOR);
        int grivaty = a.getInt(R.styleable.TextViewSwitcher_tx_gravity, 0);
        animIn = a.getResourceId(R.styleable.TextViewSwitcher_tx_animIn, R.anim.fade_in_slide_in);
        animOut = a.getResourceId(R.styleable.TextViewSwitcher_tx_animIn, R.anim.fade_out_slide_out);

        a.recycle();

        if (grivaty == 1) {
            textGravity = Gravity.CENTER;
        } else if (grivaty == 2) {
            textGravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        }

        init();
    }

    private void init() {
        setFactory(new ViewFactory() {
            @Override
            public View makeView() {
                TextView innerText = new TextView(getContext());
                innerText.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                innerText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                innerText.setTextColor(textColor);
                innerText.setGravity(textGravity);
                innerText.setMaxLines(1);
                innerText.setEllipsize(TextUtils.TruncateAt.END);
                innerText.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mClickListener != null) {
                            mClickListener.onItemClick(mPosition);
                        }
                    }
                });
                return innerText;
            }
        });

        Context context = getContext();
        setInAnimation(AnimationUtils.loadAnimation(context.getApplicationContext(), animIn));
        setOutAnimation(AnimationUtils.loadAnimation(context.getApplicationContext(), animOut));
    }

    public void setTextList(List<String> texts) {
        if (texts != null && texts.size() > 0) {
            mTexts.clear();
            mTexts.addAll(texts);
            mPosition = 0;
            updateDesc();
        }
    }

    @Override
    public void next() {
        int size = mTexts.size();
        if (size > 0) {
            if (mPosition < size - 1) {
                mPosition++;
            } else {
                mPosition = 0;
            }
            updateDesc();
        }
    }

    @Override
    public void previous() {
        int size = mTexts.size();
        if (size > 0) {
            if (mPosition > 0) {
                mPosition--;
            } else {
                mPosition = size - 1;
            }
            updateDesc();
        }
    }

    @Override
    public int size() {
        return mTexts.size();
    }

    @Override
    public void setSwitcherClickListener(SwitcherClickListener switcherClickListener) {
        this.mClickListener = switcherClickListener;
    }

    private void updateDesc() {
        int size = size();
        if (mPosition < size) {
            if (size == 1) {
                this.setCurrentText(mTexts.get(mPosition));
            } else {
                this.setText(mTexts.get(mPosition));
            }
        }
    }
}
