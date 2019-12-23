package com.tomsky.androiddemo.dylayout.render

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.tomsky.androiddemo.BuildConfig
import com.tomsky.androiddemo.dylayout.utils.DyUtils
import com.tomsky.androiddemo.dylayout.virtual.DyContext
import com.tomsky.androiddemo.dylayout.virtual.views.DyBaseView
import com.tomsky.androiddemo.dylayout.virtual.views.DyBaseView.Companion.TAG
import com.tomsky.androiddemo.dylayout.virtual.views.DyLabelView
import com.tomsky.androiddemo.dylayout.virtual.views.DyView
import com.tomsky.androiddemo.dylayout.visibleviews.ViewVisibleListener
import com.tomsky.androiddemo.dylayout.visibleviews.VisibleTextView
import com.tomsky.androiddemo.util.ThreadUtils

/**
 *
 * Created by wangzhitao on 2019/12/23
 *
 **/
class DyLabelRenderView constructor(dyContext: DyContext, dyView: DyLabelView, parentView: DyView?): DyBaseRenderView<DyLabelView>(dyContext, dyView, parentView) {

    private var textView:VisibleTextView? = null
    private var isViewVisible = false

    override fun onCreateNativeView(context: Context): View? {
        textView = VisibleTextView(context)
        textView?.setPadding(0,0,0,0)
        textView?.setVisibleListener(object : ViewVisibleListener {
            override fun onVisible(visible: Boolean) {
                if (BuildConfig.DEBUG) {
                    Log.i(DyBaseView.TAG, "label, visible:$visible")
                }
                if (visible && dyView.visibility == View.VISIBLE) {
                    doMarquee()
                }
                isViewVisible = visible
            }

        })
        return textView
    }

    override fun handleCustomProps(context: Context) {
        textView?.let {
            it.text = formatText(dyView.text)
            if (dyView.textSize != DyUtils.INVALID_FLOAT_VALUE) {
                it.setTextSize(TypedValue.COMPLEX_UNIT_PX, dyView.textSize)
            } else {
                it.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15f)
            }
            it.gravity = dyView.getRealGravity()
//            it.typeface = dyView.getTypeFace()
            handleMaxLines(it)
            if (dyView.maxWidth > 0) it.maxWidth = dyView.maxWidth

            if (dyView.ellipsize == 0) {
                it.ellipsize = TextUtils.TruncateAt.END
            }
            it.setTextColor(dyView.textColor.color)
//            val padding = props.roundBean?.borderWidth?.toInt() ?: 0
//            if (padding > 0) it.setPadding(padding, padding, padding, padding)

            if (BuildConfig.DEBUG) {
                Log.i(TAG, "textview, handleCustomProps, isViewVisible=$isViewVisible, text=${dyView.text}")
            }
            if (isViewVisible && !TextUtils.isEmpty(dyView.text)) {
                doMarquee()
            }
        }
    }

    private fun handleMaxLines(view: TextView) {
        when(dyView.maxLines) {
            0 -> view.setSingleLine(false)
            1 -> view.setSingleLine(true)
            else -> view.maxLines = dyView.maxLines
        }
    }

    private fun formatText(text: String?):String? {
//        when (dyView.format) {
//            ProomDyLabelProps.FORMAT_CURRENCY -> {
//                return NumberUtils.numberFormatHundredMillion(NumberUtils.parseLong(text, 0), 2)
//            }
//            else -> {
//                return text
//            }
//        }
        return text
    }

    /*开始跑马灯*/
    private fun doMarquee() {
        if (!dyView.hasMarquee || textView?.isShown == false) return

        ThreadUtils.runOnUiThreadDelay({
            textView?.let {
                it.ellipsize = TextUtils.TruncateAt.MARQUEE
                it.isSelected = true
                it.isFocusable = true
                it.isFocusableInTouchMode = true
                it.marqueeRepeatLimit = -1
            }
        }, 500)

    }
}