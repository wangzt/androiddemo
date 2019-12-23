package com.tomsky.androiddemo.dylayout.render

import android.content.Context
import android.view.View

/**
 *
 * Created by wangzhitao on 2019/12/23
 *
 **/
interface IRenderView {

    fun getRenderViewId():Int

    fun getRenderView(): View?

    fun createNativeView(context: Context)

    fun updateRenderView(context: Context)
}