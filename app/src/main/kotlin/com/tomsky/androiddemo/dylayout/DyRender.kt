package com.tomsky.androiddemo.dylayout

import android.content.Context
import android.widget.FrameLayout
import com.tomsky.androiddemo.dylayout.virtual.views.DyBaseView
import com.tomsky.androiddemo.dylayout.virtual.views.DyRootView

/**
 *
 * Created by wangzhitao on 2019/12/19
 *
 **/

class DyRender {


    fun createFullRenderView(context: Context, containerView: FrameLayout, rootView: DyRootView) {
        var rootRenderView = rootView.createRenderView(context)
        rootRenderView?.let {
            containerView.removeAllViews()
            containerView.addView(it.getRenderView())
        }
    }

    fun updateRenderView(context: Context, updateChildren: MutableList<DyBaseView>) {
        for (dyView in updateChildren) {
            dyView.updateRenderView(context)
        }
    }
}