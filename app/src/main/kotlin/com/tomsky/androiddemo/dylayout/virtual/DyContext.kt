package com.tomsky.androiddemo.dylayout.virtual

import com.tomsky.androiddemo.dylayout.DyDataCenter
import com.tomsky.androiddemo.dylayout.virtual.views.DyBaseView
import java.util.concurrent.ConcurrentHashMap

/**
 *
 * Created by wangzhitao on 2019/12/20
 *
 **/
class DyContext {

    var dyDataCenter = DyDataCenter()

    private val childViews = ConcurrentHashMap<String, DyBaseView>()


    fun addChildView(id:String, childView:DyBaseView) {
        childViews[id] = childView
    }

    fun getChildView(id:String):DyBaseView? {
        return childViews[id]
    }

    fun onViewAction(id:String) {

    }
}