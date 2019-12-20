package com.tomsky.androiddemo.dylayout.utils

import android.text.TextUtils
import com.tomsky.androiddemo.BaseApplication

object DyUtils {
    /*无效的Int值*/
    var INVALID_INT_VALUE = Integer.MIN_VALUE
    /*无效的Float值*/
    var INVALID_FLOAT_VALUE = Float.MIN_VALUE


    private var BASE_WIDTH = 375f
    private var scale = -1f

    fun scaleSize(size: Double): Int {
        if (size.isNaN()) return INVALID_INT_VALUE
        return scaleFloatSize(size).toInt()
//        if (scale < 0) {
//            val metrics = BaseApplication.getContext().resources.displayMetrics
//            var width = metrics.widthPixels
//            val height = metrics.heightPixels
//            if (width > height) {
//                width = height
//            }
//            scale = width * 1.0f / BASE_WIDTH
//        }
//        return (scale * size).toInt()
    }

    fun scaleFloatSize(size: Double): Float {
        if (size.isNaN()) return INVALID_FLOAT_VALUE
        if (scale < 0) {
            val metrics = BaseApplication.getContext().resources.displayMetrics
            var width = metrics.widthPixels
            val height = metrics.heightPixels
            if (width > height) {
                width = height
            }
            scale = width * 1.0f / BASE_WIDTH
        }
        return (scale * size).toFloat()
    }

    fun scaleSize(size: Float): Int {
        if (size.isNaN()) return INVALID_INT_VALUE
        return scaleFloatSize(size).toInt()
    }

    fun scaleFloatSize(size: Float): Float {
        if (size.isNaN()) return INVALID_FLOAT_VALUE
        if (scale < 0) {
            val metrics = BaseApplication.getContext().resources.displayMetrics
            var width = metrics.widthPixels
            val height = metrics.heightPixels
            if (width > height) {
                width = height
            }
            scale = width * 1.0f / BASE_WIDTH
        }
        return scale * size
    }

    fun unScaleSize(size:Int): Float {
        if (scale < 0) {
            val metrics = BaseApplication.getContext().resources.displayMetrics
            var width = metrics.widthPixels
            val height = metrics.heightPixels
            if (width > height) {
                width = height
            }
            scale = width * 1.0f / BASE_WIDTH
        }
        if (scale == 0f) return size.toFloat()
        return size / scale
    }

    fun unScaleSize(size:Float): Float {
        if (scale < 0) {
            val metrics = BaseApplication.getContext().resources.displayMetrics
            var width = metrics.widthPixels
            val height = metrics.heightPixels
            if (width > height) {
                width = height
            }
            scale = width * 1.0f / BASE_WIDTH
        }
        if (scale == 0f) return size
        return size / scale
    }

    fun setBaseWidth(baseWidth: Float) {
        BASE_WIDTH = baseWidth
        val metrics = BaseApplication.getContext().resources.displayMetrics
        var width = metrics.widthPixels
        val height = metrics.heightPixels
        if (width > height) {
            width = height
        }
        scale = width * 1.0f / BASE_WIDTH
    }

    fun parseColor(colorString: String, alpha: Double): Int {
        if (TextUtils.isEmpty(colorString) || colorString.length != 7 || colorString[0] != '#') throw IllegalArgumentException("Unknown color")

        var color = java.lang.Long.parseLong(colorString.substring(1), 16)
        color = if (alpha < 0 || alpha > 1) color or -0x1000000 else color or ((alpha * 255).toLong() shl 24)

        return color.toInt()
    }

    fun parseBoolean(str: String, defaultBoolean: Boolean): Boolean {
        if (TextUtils.isEmpty(str)) {
            return defaultBoolean
        }

        return "0" != str && !"false".equals(str, ignoreCase = true)

    }

    fun parseBooleanNoCheck(str: String): Boolean {
        return "0" != str && !"false".equals(str, ignoreCase = true)
    }


}
