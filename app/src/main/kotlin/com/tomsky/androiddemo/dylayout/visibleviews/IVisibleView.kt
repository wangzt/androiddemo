package com.tomsky.androiddemo.dylayout.visibleviews

/**
 *
 * Created by wangzhitao on 2019/12/23
 *
 **/
interface IVisibleView {
    fun setVisibleListener(visibleListener: ViewVisibleListener?)
}

interface ViewVisibleListener {
    fun onVisible(visible:Boolean)
}