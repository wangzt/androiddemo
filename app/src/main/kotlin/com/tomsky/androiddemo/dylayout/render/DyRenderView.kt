package com.tomsky.androiddemo.dylayout.render

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.view.View
import com.tomsky.androiddemo.dylayout.virtual.DyContext
import com.tomsky.androiddemo.dylayout.virtual.views.DyView

/**
 *
 * Created by wangzhitao on 2019/12/23
 *
 **/
open class DyRenderView constructor(dyContext: DyContext, dyView: DyView, parentView: DyView?): DyBaseRenderView<DyView>(dyContext, dyView, parentView) {


    override fun onCreateNativeView(context: Context): View? {
        var view = ConstraintLayout(context)
        view.id = viewId

        for (dyBaseView in dyView.childList) {
            var childView = dyBaseView.createRenderView(context)
            childView?.let {
                view.addView(it.getRenderView())
            }
        }
        return view
    }

    override fun handleCustomProps(context: Context) {

    }

}