package com.tomsky.androiddemo.dylayout.virtual.views

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.text.TextUtils
import android.view.View
import com.tomsky.androiddemo.dylayout.virtual.DyContext
import org.json.JSONObject

/**
 *
 * Created by wangzhitao on 2019/12/19
 *
 **/
open class DyView constructor(dyContext: DyContext, jsonObject: JSONObject, parentView: DyView?):DyBaseView(dyContext, jsonObject, parentView) {

    companion object {
        const val TAG = "DyView"
        const val PRINT_PREFIX = "*"

        const val NAME = "view"

        const val P_CHILD = "child"

    }

    var childList: MutableList<DyBaseView> = mutableListOf()//初始化在构造方法之前 否则为空

    init {
        parseChildren(jsonObject)
    }

    /*解析子View*/
    protected open fun parseChildren(jsonObj: JSONObject) {
        val childJsonArray = jsonObj.optJSONArray(P_CHILD) ?: return
        val childCount = childJsonArray.length()

        for (i in 0 until childCount) {
            val childJson = childJsonArray.optJSONObject(i) ?: continue
            val childName = childJson.optString(P_NAME)
            if (TextUtils.isEmpty(childName)) continue

            when(childName) {
                DyView.NAME -> childList.add(DyView(dyContext, childJson, this))
                else -> handleCustomChild(childName, childJson, childList)
            }
        }
    }

    open fun handleCustomChild(childName:String, childJson:JSONObject, childList:MutableList<DyBaseView>) {}


    override fun onCreateRenderView(context: Context): View? {
        var view = ConstraintLayout(context)
        view.id = viewId

        for (dyBaseView in childList) {
            var childView = dyBaseView.createRenderView(context)
            childView?.let {
                view.addView(it)
            }
        }
        return view
    }
}