package com.tomsky.androiddemo.dylayout.virtual.views

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.core.view.ViewCompat
import com.tomsky.androiddemo.dylayout.render.DyBaseRenderView
import com.tomsky.androiddemo.dylayout.render.DyRenderView
import com.tomsky.androiddemo.dylayout.render.IRenderView
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
        const val TAG = "dy_layout"
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


//    protected var viewId = 0//real view的id
    protected var parentView:DyView? = null // 父容器

    init {
        id = if (TextUtils.isEmpty(id)) ViewCompat.generateViewId().toString() else id
//        viewId = ViewCompat.generateViewId()

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

    protected open fun onCreate(jsonObj: JSONObject, parentView: DyView?) {
        this.parentView = parentView
        val propJson = jsonObj.optJSONObject(P_PROP)
        if (propJson != null) {
            parseCommonProps(propJson)
            parseCustomProps(propJson)
        }
        dyContext.addChildView(id, this)
    }

    open fun parseCustomProps(propJson: JSONObject){}

    var layoutChanged = AtomicBoolean(false)
    var bgChanged = AtomicBoolean(false)
    var visibleChanged = AtomicBoolean(false)
    var actionChanged = AtomicBoolean(false)

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

    protected var mRenderView: IRenderView? = null

    fun createRenderView(context: Context):IRenderView? {
        mRenderView = onCreateRenderView()
        mRenderView?.createNativeView(context)
        return mRenderView
    }

    abstract fun onCreateRenderView(): IRenderView?

    fun getRenderViewId():Int {
        return mRenderView?.getRenderViewId() ?: 0
    }

    fun updateRenderView(context: Context) {
        mRenderView?.updateRenderView(context)
    }
}