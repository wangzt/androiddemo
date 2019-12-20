package com.tomsky.androiddemo.dylayout.virtual.views

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v4.view.ViewCompat
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.tomsky.androiddemo.dylayout.utils.DyUtils
import com.tomsky.androiddemo.dylayout.virtual.DyContext
import com.tomsky.androiddemo.dylayout.virtual.beans.DyColorBean
import com.tomsky.androiddemo.dylayout.virtual.beans.DyGradientBean
import com.tomsky.androiddemo.dylayout.virtual.beans.DyLayoutBean
import com.tomsky.androiddemo.dylayout.virtual.beans.DyRoundBean
import org.json.JSONObject
import java.util.concurrent.atomic.AtomicBoolean

/**
 *
 * Created by wangzhitao on 2019/12/19
 *
 **/
abstract class DyBaseView constructor(protected val dyContext: DyContext, jsonObj: JSONObject, parentView:DyView?) {

    companion object {
        const val P_ID = "id"
        const val P_NAME = "name"
        const val P_DESC = "desc"
        const val P_PROP = "prop"
        const val P_LAYOUT = "layout"
        const val P_ROUND = "round"
        const val P_BG_COLOR = "bgColor"
        const val P_BG_GRADIENT = "bgGradient"
        const val P_VISIBLE = "visible"
        const val P_IS_DISPATCH_ONCLICK = "isDispatchOnClickEvent" // 透传点击事件到H5，由H5设置

        const val P_DATA = "data"
    }

    var id: String = jsonObj.optString(P_ID, "")
    var name: String = jsonObj.optString(P_NAME, "")
    var desc: String = jsonObj.optString(P_DESC, "")
    var data = jsonObj.optJSONObject(P_DATA)

    var layoutBean: DyLayoutBean? = null
    var roundBean: DyRoundBean? = null
    var bgColorBean: DyColorBean? = null
    var bgGradientBean: DyGradientBean? = null
    var visibility: Int = View.VISIBLE

    var action:Boolean = false


    protected var viewId = 0//real view的id
    protected var parentView:DyView? = null // 父容器

    init {
        id = if (TextUtils.isEmpty(id)) ViewCompat.generateViewId().toString() else id
        viewId = ViewCompat.generateViewId()
        val propJson = jsonObj.optJSONObject(P_PROP)
        if (propJson != null) {
            parseCommonProps(propJson)
            parseCustomProps(propJson)
        }
        onCreate(jsonObj, parentView)
    }

    /*解析通用属性，控件都需要的*/
    private fun parseCommonProps(propJson: JSONObject) {

        val layoutJson = propJson.optJSONObject(P_LAYOUT)
        if (layoutJson != null) layoutBean = DyLayoutBean(layoutJson)

        val roundJson = propJson.optJSONObject(P_ROUND)
        if (DyRoundBean.isValid(roundJson)) roundBean = DyRoundBean(roundJson)

        val bgColorJson = propJson.optJSONObject(P_BG_COLOR)
        if (DyColorBean.isValid(bgColorJson)) bgColorBean = DyColorBean(bgColorJson)

        val bgGradientJson = propJson.optJSONObject(P_BG_GRADIENT)
        if (bgGradientJson != null) {
            val gradientBean = DyGradientBean(bgGradientJson)
            if (gradientBean.isValid()) {
                bgGradientBean = gradientBean
            }
        }

        val visibilityStr = propJson.optString(P_VISIBLE, "")
        if (visibilityStr != null) {
            val visible = DyUtils.parseBoolean(visibilityStr, true)
            visibility = if (visible) View.VISIBLE else View.GONE
        }

        action = propJson.optBoolean(P_IS_DISPATCH_ONCLICK, false)
    }

    /*解析自定义的属性*/
    open fun parseCustomProps(propJson: JSONObject) {}

    protected open fun onCreate(jsonObj: JSONObject, parentView: DyView?) {
        this.parentView = parentView
        dyContext.addChildView(id, this)
    }


    protected var layoutChanged = AtomicBoolean(false)
    protected var bgChanged = AtomicBoolean(false)
    protected var visibleChanged = AtomicBoolean(false)
    protected var actionChanged = AtomicBoolean(false)

    open fun updateProp(propJson: JSONObject) {
        val layoutJson = propJson.optJSONObject(P_LAYOUT)
        if (layoutJson != null) {
            layoutBean = DyLayoutBean(layoutJson)
            layoutChanged.set(true)
        }

        val roundJson = propJson.optJSONObject(P_ROUND)
        if (DyRoundBean.isValid(roundJson)) {
            roundBean = DyRoundBean(roundJson)
            bgChanged.set(true)
        }

        val bgColorJson = propJson.optJSONObject(P_BG_COLOR)
        if (DyColorBean.isValid(bgColorJson)) {
            bgColorBean = DyColorBean(bgColorJson)
            bgChanged.set(true)
        }

        val bgGradientJson = propJson.optJSONObject(P_BG_GRADIENT)
        if (bgGradientJson != null) {
            val gradientBean = DyGradientBean(bgGradientJson)
            if (gradientBean.isValid()) {
                bgGradientBean = gradientBean
                bgChanged.set(true)
            }
        }

        val visibilityStr = propJson.optString(P_VISIBLE, "")
        if (visibilityStr != null) {
            val visible = DyUtils.parseBoolean(visibilityStr, true)
            visibility = if (visible) View.VISIBLE else View.GONE
            visibleChanged.set(true)
        }

        if (propJson.has(P_IS_DISPATCH_ONCLICK)) {
            action = propJson.optBoolean(P_IS_DISPATCH_ONCLICK, false)
            actionChanged.set(true)
        }
    }

    /**
     * ===================以下都是NativeView相关操作，放在主线程=====================
     */
    protected var renderView:View? = null // 要渲染的view

    fun createRenderView(context: Context):View? {
        renderView = onCreateRenderView(context)
        renderView?.let {
            if (layoutBean != null) {
                it.layoutParams = calcLayoutParams(layoutBean!!, parentView)
            }

            handleBackground(it)
            handleAction()
            it.visibility = visibility
        }
        return renderView
    }

    abstract fun onCreateRenderView(context: Context):View?


    fun updateRenderView(context: Context) {
        renderView?.let {
            if (layoutChanged.get()) {
                it.layoutParams = calcLayoutParams(layoutBean!!, parentView)
                layoutChanged.set(false)
            }

            if (bgChanged.get()) {
                handleBackground(it)
                bgChanged.set(false)
            }

            if (visibleChanged.get()) {
                it.visibility = visibility
                visibleChanged.set(false)
            }

            if (actionChanged.get()) {
                handleAction()
                actionChanged.set(false)
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
            val parentId = parentView.viewId
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
        if (bgGradientBean != null) {
            val bgDrawable = bgGradientBean?.toDrawable() ?: return
            roundBean?.setRound(bgDrawable)
            @Suppress("DEPRECATION")
            view.setBackgroundDrawable(bgDrawable)
        } else if (bgColorBean != null) {
            if (roundBean != null) {
                val bgDrawable = roundBean?.toDrawable() ?: return
                bgDrawable.setColor(bgColorBean!!.color)
                @Suppress("DEPRECATION")
                view.setBackgroundDrawable(bgDrawable)
            } else {
                view.setBackgroundColor(bgColorBean!!.color)
            }

        } else if (roundBean != null) {
            val bgDrawable = roundBean?.toDrawable() ?: return
            @Suppress("DEPRECATION")
            view.setBackgroundDrawable(bgDrawable)
        }
    }

    private fun handleAction() {
        if (action) {
            renderView?.setOnClickListener {
                dyContext.onViewAction(id)
            }
        } else {
            renderView?.setOnClickListener(null)
            renderView?.isClickable = false
        }
    }
}