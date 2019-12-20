package com.tomsky.androiddemo.dylayout.utils

import org.json.JSONArray
import org.json.JSONObject

/**
 *
 * Created by wangzhitao on 2019/12/19
 *
 **/
fun JSONObject.isEmpty(): Boolean {
    return length() <= 0
}

fun JSONArray.isEmpty(): Boolean {
    return length() <= 0
}