package com.tomsky.androiddemo.dylayout.render

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v4.view.ViewCompat
import android.view.View
import com.tomsky.androiddemo.dylayout.virtual.DyContext
import com.tomsky.androiddemo.dylayout.virtual.beans.DyLayoutBean
import com.tomsky.androiddemo.dylayout.virtual.views.DyBaseView
import com.tomsky.androiddemo.dylayout.virtual.views.DyView

/**
 *
 * Created by wangzhitao on 2019/12/23
 *
 **/

abstract class DyBaseRenderView<out T:DyBaseView> (protected val dyContext: DyContext, protected val dyView: T, protected var parentView:DyView?): IRenderView {

    protected var nativeView: View? = null // 要渲染的view
    protected var viewId = 0//contentView的id

    init {
        viewId = ViewCompat.generateViewId()
    }

    override fun getRenderViewId(): Int {
        return viewId
    }

    override fun getRenderView(): View? {
        return nativeView
    }

    override fun createNativeView(context: Context) {
        nativeView = onCreateNativeView(context)
        nativeView?.let {
            if (dyView.layoutBean != null) {
                it.layoutParams = calcLayoutParams(dyView.layoutBean!!, parentView)
            }

            handleBackground(it)
            handleCustomProps(context)
            handleAction()
            it.visibility = dyView.visibility
        }
    }


    abstract fun onCreateNativeView(context: Context): View?

    abstract fun handleCustomProps(context: Context)

    override fun updateRenderView(context: Context) {
        nativeView?.let {
            if (dyView.layoutChanged.get()) {
                it.layoutParams = calcLayoutParams(dyView.layoutBean!!, parentView)
                dyView.layoutChanged.set(false)
            }

            if (dyView.bgChanged.get()) {
                handleBackground(it)
                dyView.bgChanged.set(false)
            }

            if (dyView.visibleChanged.get()) {
                it.visibility = dyView.visibility
                dyView.visibleChanged.set(false)
            }

            if (dyView.actionChanged.get()) {
                handleAction()
                dyView.actionChanged.set(false)
            }
        }

    }
    /*计算控件对应的 LayoutParams*/
    private fun calcLayoutParams(layoutBean: DyLayoutBean, parentView: DyView?): ConstraintLayout.LayoutParams {
        var width = layoutBean.width
        var height = layoutBean.height
        if (layoutBean.widthAuto) {
            width = ConstraintLayout.LayoutParams.WRAP_CONTENT
        }
        if (layoutBean.heightAuto) {
            height = ConstraintLayout.LayoutParams.WRAP_CONTENT
        }

        // 兼容iOS逻辑
        if (width == Integer.MIN_VALUE) {
            if (layoutBean.left > Integer.MIN_VALUE && layoutBean.right > Integer.MIN_VALUE) {
                width = 0
            } else {
                width = ConstraintLayout.LayoutParams.WRAP_CONTENT
            }
        }
        if (height == Integer.MIN_VALUE) {
            if (layoutBean.top > Integer.MIN_VALUE && layoutBean.bottom > Integer.MIN_VALUE) {
                height = 0
            } else {
                height = ConstraintLayout.LayoutParams.WRAP_CONTENT
            }
        }
        if (layoutBean.width == 0) {
            width = -10 // fix ConstraintLayout的bug,设置为0的情况会铺满父容器，测试发现小于-4就不可见了,shit
        }
        if (layoutBean.height == 0) {
            height = -10 // fix ConstraintLayout的bug,设置为0的情况会铺满父容器，测试发现小于-4就不可见了,shit
        }

        val lp = ConstraintLayout.LayoutParams(width, height)

        if (parentView != null) {
            val parentId = parentView.getRenderViewId()
            if (layoutBean.centerLand) {
                lp.leftToLeft = parentId
                lp.rightToRight = parentId
                if (layoutBean.centerPort) {
                    lp.topToTop = parentId
                    lp.bottomToBottom = parentId
                } else {
                    if (layoutBean.bottom > Integer.MIN_VALUE) {
                        lp.bottomToBottom = parentId
                        lp.bottomMargin = layoutBean.bottom
                    }
                    if (layoutBean.top > Integer.MIN_VALUE){
                        lp.topToTop = parentId
                        lp.topMargin = layoutBean.top
                    }
                }
            } else if (layoutBean.centerPort) {
                lp.topToTop = parentId
                lp.bottomToBottom = parentId
                if (layoutBean.centerLand) {
                    lp.leftToLeft = parentId
                    lp.rightToRight = parentId
                } else {
                    if (layoutBean.right > Integer.MIN_VALUE) {
                        lp.rightToRight = parentId
                        lp.rightMargin = layoutBean.right
                    }
                    if (layoutBean.left > Integer.MIN_VALUE){
                        lp.leftToLeft = parentId
                        lp.leftMargin = layoutBean.left
                    }
                }
            } else {
                if (layoutBean.right > Integer.MIN_VALUE) {
                    lp.rightToRight = parentId
                    lp.rightMargin = layoutBean.right
                }
                if (layoutBean.left > Integer.MIN_VALUE){
                    lp.leftToLeft = parentId
                    lp.leftMargin = layoutBean.left
                }
                if (layoutBean.bottom > Integer.MIN_VALUE) {
                    lp.bottomToBottom = parentId
                    lp.bottomMargin = layoutBean.bottom
                }
                if (layoutBean.top > Integer.MIN_VALUE){
                    lp.topToTop = parentId
                    lp.topMargin = layoutBean.top
                }
            }
        }

        return lp
    }

    protected open fun handleBackground(view: View) {
        if (dyView.bgGradientBean != null) {
            val bgDrawable = dyView.bgGradientBean?.toDrawable() ?: return
            dyView.roundBean?.setRound(bgDrawable)
            @Suppress("DEPRECATION")
            view.setBackgroundDrawable(bgDrawable)
        } else if (dyView.bgColorBean != null) {
            if (dyView.roundBean != null) {
                val bgDrawable = dyView.roundBean?.toDrawable() ?: return
                bgDrawable.setColor(dyView.bgColorBean!!.color)
                @Suppress("DEPRECATION")
                view.setBackgroundDrawable(bgDrawable)
            } else {
                view.setBackgroundColor(dyView.bgColorBean!!.color)
            }

        } else if (dyView.roundBean != null) {
            val bgDrawable = dyView.roundBean?.toDrawable() ?: return
            @Suppress("DEPRECATION")
            view.setBackgroundDrawable(bgDrawable)
        }
    }

    private fun handleAction() {
        if (dyView.action) {
            nativeView?.setOnClickListener {
                dyContext.onViewAction(dyView.id)
            }
        } else {
            nativeView?.setOnClickListener(null)
            nativeView?.isClickable = false
        }
    }
}