package com.tomsky.androiddemo.views

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView

/**
 *
 * Created by wangzhitao on 2019/12/23
 *
 **/
open class MarqueeTextView(context: Context) : TextView(context) {

    override fun isFocused(): Boolean {
        return true
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
    }
}