package com.tomsky.androiddemo.dylayout.virtual.beans

import android.graphics.drawable.GradientDrawable
import org.json.JSONObject

/**
 *
 * Created by wangzhitao on 2019/12/19
 *
 **/
class DyGradientBean constructor(gradientJson: JSONObject) {

    companion object {
        const val P_ORIENTATION = "orientation"
        const val P_COLORS = "colors"

        const val DEFAULT_ORIENTATION = 0

    }

    var orientation = gradientJson.optInt(P_ORIENTATION, DEFAULT_ORIENTATION)
    var colors:ArrayList<Int> = ArrayList()

    var cacheJson: JSONObject? = null

    init {
        var colorsArray = gradientJson.optJSONArray(P_COLORS)
        colorsArray.let {
            var size = it.length()
            for (i in 0 until size) {
                var colorObj = it.optJSONObject(i) ?: continue
                if (DyColorBean.isValid(colorObj)) {
                    var colorBean = DyColorBean(colorObj)
                    colors.add(colorBean.color)
                }
            }
        }
        cacheJson = gradientJson
    }

    fun isValid():Boolean {
        return colors.size > 1
    }

    private fun getOrientation(): GradientDrawable.Orientation {
        return if (orientation == 1) {
            GradientDrawable.Orientation.TOP_BOTTOM
        } else {
            GradientDrawable.Orientation.LEFT_RIGHT
        }
    }

    fun toDrawable(): GradientDrawable {
        val drawable = GradientDrawable()
        drawable.colors = colors.toIntArray()
        drawable.orientation = getOrientation()
        return drawable
    }

}