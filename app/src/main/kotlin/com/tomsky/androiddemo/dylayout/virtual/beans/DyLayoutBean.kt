package com.tomsky.androiddemo.dylayout.virtual.beans

import com.tomsky.androiddemo.dylayout.utils.DyUtils
import org.json.JSONObject

/**
 *
 * Created by wangzhitao on 2019/12/19
 *
 **/
class DyLayoutBean constructor(layoutJson: JSONObject) {

    companion object {
        const val P_L = "l"
        const val P_T = "t"

        const val P_R = "r"
        const val P_B = "b"

        const val P_W = "w"
        const val P_H = "h"

        const val P_CENTER_LAND = "centerLand"
        const val P_CENTER_PORT = "centerPort"

        const val P_WIDTH_AUTO = "widthAuto"
        const val P_HEIGHT_AUTO = "heightAuto"

    }

    var cacheJson = layoutJson

    //距离父View左边的距离
    var left = DyUtils.scaleSize(layoutJson.optDouble(P_L, Double.NaN))
    //距离父View顶部的距离
    var top = DyUtils.scaleSize(layoutJson.optDouble(P_T, Double.NaN))

    //如果left和top没有 那通过right和bottom进行右下对齐
    var right = DyUtils.scaleSize(layoutJson.optDouble(P_R, Double.NaN))
    //如果left和top没有 那通过right和bottom进行右下对齐
    var bottom = DyUtils.scaleSize(layoutJson.optDouble(P_B, Double.NaN))

    //宽度
    var width = DyUtils.scaleSize(layoutJson.optDouble(P_W, Double.NaN))
    //高度
    var height = DyUtils.scaleSize(layoutJson.optDouble(P_H, Double.NaN))

    //是否横向居中
    var centerLand = DyUtils.parseBoolean(layoutJson.optString(P_CENTER_LAND, ""), false)
    //是否总想居中
    var centerPort = DyUtils.parseBoolean(layoutJson.optString(P_CENTER_PORT, ""), false)

    //宽度自适应
    var widthAuto = DyUtils.parseBoolean(layoutJson.optString(P_WIDTH_AUTO, ""), false)
    //高度自适应
    var heightAuto = DyUtils.parseBoolean(layoutJson.optString(P_HEIGHT_AUTO, ""), false)

    override fun toString(): String {
        return "($left-$top-$right-$bottom, w=$width, h=$height, centerLand=$centerLand, centerPort=$centerPort, widthAuto=$widthAuto, heightAuto=$heightAuto)"
    }

}