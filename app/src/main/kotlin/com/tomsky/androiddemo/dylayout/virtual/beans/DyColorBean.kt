package com.tomsky.androiddemo.dylayout.virtual.beans

import android.graphics.Color
import com.tomsky.androiddemo.dylayout.utils.DyUtils
import com.tomsky.androiddemo.dylayout.utils.isEmpty
import org.json.JSONObject

/**
 *
 * Created by wangzhitao on 2019/12/19
 *
 **/

class DyColorBean constructor() {
    companion object {
        const val P_COLOR = "color"
        const val P_ALPHA = "alpha"

        const val DEFAULT_ALPHA = 1.toDouble()

        /*透明色*/
        val TRANSPARENT_COLOR = DyColorBean(JSONObject())

        val BLACK_COLOR = DyColorBean(Color.BLACK)

        fun isValid(json: JSONObject?):Boolean {
            if (json == null) return false

            if (!json.has(P_COLOR)) return false

            return true
        }
    }

    var color = Color.TRANSPARENT

    var cacheJson: JSONObject? = null
    constructor(colorJson: JSONObject):this() {
        color = if (colorJson.isEmpty()) Color.TRANSPARENT else DyUtils.parseColor(colorJson.optString(P_COLOR), colorJson.optDouble(P_ALPHA, DEFAULT_ALPHA))
        cacheJson = colorJson
    }

    constructor(co: Int):this() {
        color = co
    }

    fun isTransparent(): Boolean {
        return color == Color.TRANSPARENT
    }

}