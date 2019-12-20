package com.tomsky.androiddemo.dylayout.virtual.beans

import android.graphics.drawable.GradientDrawable
import com.tomsky.androiddemo.dylayout.utils.DyUtils
import org.json.JSONObject

/**
 *
 * Created by wangzhitao on 2019/12/19
 *
 **/
class DyRoundBean constructor(roundJson: JSONObject) {

    companion object {
        const val P_CORNER_RADIUS = "radius"
        const val P_BORDER_WIDTH = "borderWidth"
        const val P_BORDER_COLOR = "borderColor"

        fun isValid(json: JSONObject?):Boolean {
            if (json == null) return false

            if (json.has(P_CORNER_RADIUS) || json.has(P_BORDER_WIDTH)) {
                return true
            }
            return false
        }
    }

    var cacheJson = roundJson

    //圆角角度
    var cornerRadius = DyUtils.scaleFloatSize(roundJson.optDouble(P_CORNER_RADIUS, Double.NaN))
    //边框宽度
    var borderWidth = DyUtils.scaleFloatSize(roundJson.optDouble(P_BORDER_WIDTH, Double.NaN))

    var borderColor: DyColorBean?

    init {
        val borderColorJson = roundJson.optJSONObject(P_BORDER_COLOR)
        borderColor = if (DyColorBean.isValid(borderColorJson)) DyColorBean(borderColorJson) else null
    }

    fun toDrawable(): GradientDrawable {
        val drawable = GradientDrawable()
        if (borderColor != null && borderWidth > 0) drawable.setStroke(borderWidth.toInt(), borderColor!!.color)
        if (cornerRadius == DyUtils.INVALID_FLOAT_VALUE || cornerRadius <= 0) drawable.cornerRadius = 0F
        else drawable.cornerRadius = cornerRadius
        return drawable
    }

    fun setRound(drawable: GradientDrawable) {
        if (borderColor != null && borderWidth > 0) drawable.setStroke(borderWidth.toInt(), borderColor!!.color)
        if (cornerRadius == DyUtils.INVALID_FLOAT_VALUE || cornerRadius <= 0) drawable.cornerRadius = 0F
        else drawable.cornerRadius = cornerRadius
    }
}