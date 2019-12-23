package com.tomsky.androiddemo.dylayout.virtual.views

import com.tomsky.androiddemo.dylayout.virtual.DyContext
import org.json.JSONObject

/**
 *
 * Created by wangzhitao on 2019/12/20
 *
 **/
class DyRootView constructor(dyContext: DyContext, jsonObject: JSONObject, parentView: DyView?):DyView(dyContext, jsonObject, parentView) {

    companion object {
        const val NAME = "root"

        const val P_SETTING = "setting" // 动态布局额外配置
    }

}