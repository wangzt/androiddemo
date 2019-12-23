package com.tomsky.androiddemo.dylayout.visibleviews

import android.content.Context
import android.view.View
import com.tomsky.androiddemo.views.MarqueeTextView

/**
 *
 * Created by wangzhitao on 2019/12/23
 *
 **/
class VisibleTextView(context: Context) : MarqueeTextView(context), IVisibleView {
    private var visibleListener:ViewVisibleListener? = null

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
    }

    override fun setVisibleListener(visibleListener: ViewVisibleListener?) {
        this.visibleListener = visibleListener
    }
}