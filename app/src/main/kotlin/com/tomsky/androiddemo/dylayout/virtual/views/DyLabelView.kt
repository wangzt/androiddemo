package com.tomsky.androiddemo.dylayout.virtual.views

import android.view.Gravity
import com.tomsky.androiddemo.dylayout.render.DyLabelRenderView
import com.tomsky.androiddemo.dylayout.render.IRenderView
import com.tomsky.androiddemo.dylayout.utils.DyUtils
import com.tomsky.androiddemo.dylayout.virtual.DyContext
import com.tomsky.androiddemo.dylayout.virtual.beans.DyColorBean
import org.json.JSONObject

/**
 *
 * Created by wangzhitao on 2019/12/23
 *
 **/
class DyLabelView constructor(dyContext: DyContext, jsonObject: JSONObject, parentView: DyView?):DyBaseView(dyContext, jsonObject, parentView) {

    companion object {
        const val NAME = "label"

        const val P_TEXT = "text"
        const val P_TEXT_SIZE = "textSize"
        const val P_TEXT_COLOR = "textColor"
        const val P_MAX_WIDTH = "maxWidth"
        const val P_ELLIPSIZE = "ellipsize"
        const val P_MAX_LINES = "maxLines"
        const val P_GRAVITY = "gravity"

        const val DEFAULT_ELLIPSIZE = 0
        const val DEFAULT_MAX_LINES = 1
        const val DEFAULT_GRAVITY = 0


    }

    var text: String = ""
    var textSize: Float = DyUtils.INVALID_FLOAT_VALUE
    var textColor: DyColorBean = DyColorBean.BLACK_COLOR
    var maxWidth: Int = 0
    var ellipsize: Int = DEFAULT_ELLIPSIZE // 0:省略号, 1: 截断, 2:跑马灯
    var maxLines = DEFAULT_MAX_LINES
    var gravity: Int = 0 // 0: left, 1: right, 2: center

    var hasMarquee: Boolean = false// 是否需要跑马灯

    init {
        onCreate(jsonObject, parentView)
    }

    override fun parseCustomProps(propJson: JSONObject) {

        text = propJson.optString(P_TEXT, "")
        textSize = DyUtils.scaleFloatSize(propJson.optDouble(P_TEXT_SIZE, Double.NaN))

        val colorJson = propJson.optJSONObject(P_TEXT_COLOR)
        textColor = if (colorJson == null) DyColorBean.BLACK_COLOR else DyColorBean(colorJson)

        maxWidth = DyUtils.scaleSize(propJson.optDouble(P_MAX_WIDTH, Double.NaN))
        ellipsize = propJson.optInt(P_ELLIPSIZE, DEFAULT_ELLIPSIZE)
        maxLines = propJson.optInt(P_MAX_LINES, DEFAULT_MAX_LINES)

        gravity = propJson.optInt(P_GRAVITY, DEFAULT_GRAVITY)

        //处理一些自己私有的属性
        hasMarquee = ellipsize == 2
    }

    fun getRealGravity(): Int {
        return when (gravity) {
            0 -> Gravity.CENTER_VERTICAL or Gravity.LEFT
            1 -> Gravity.CENTER_VERTICAL or Gravity.RIGHT
            2 -> Gravity.CENTER
            else -> Gravity.CENTER_VERTICAL or Gravity.LEFT
        }
    }

    override fun onCreateRenderView(): IRenderView? {
        return DyLabelRenderView(dyContext, this, parentView)
    }

}