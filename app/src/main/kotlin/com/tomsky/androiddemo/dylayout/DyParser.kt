package com.tomsky.androiddemo.dylayout

import android.util.Log
import com.tomsky.androiddemo.dylayout.virtual.DyContext
import com.tomsky.androiddemo.dylayout.virtual.views.DyBaseView
import com.tomsky.androiddemo.dylayout.virtual.views.DyRootView
import org.json.JSONObject

/**
 *
 * Created by wangzhitao on 2019/12/19
 *
 **/
class DyParser {


    private var dyRootView: DyRootView? = null

    fun parseFullLayout(dyContext: DyContext, jsonObj: JSONObject):DyRootView {
        var rootView = DyRootView(dyContext, jsonObj, null)
        dyRootView = rootView
        return rootView
    }

    fun parseH5Method(dyContext: DyContext, jsonObj: JSONObject):ParseH5Result {
        val keys = jsonObj.keys()
        var resultObj = JSONObject()
        var updateChildren = mutableListOf<DyBaseView>()
        for (key in keys) {
            val nodeObj = jsonObj.optJSONObject(key)
            nodeObj?.let {
                val childView = dyContext.getChildView(key)
                if (childView != null) {
                    var resultNodeObj = JSONObject()
                    resultObj.put(key, resultNodeObj)
                    it.optJSONObject("setter")?.let { setter ->
                        childView.updateProp(setter)
                    }
//                    it.optJSONArray("getter")?.let { getter ->
//                        childView?.getPropJSON(getter)?.let { result ->
//                            resultNodeObj.put("getter", result)
//                        }
//                    }
//                    it.optJSONObject("addChild")?.let {
//                        childObj -> addViewByJSON(context, key,-1, childObj)
//                    }
//
//                    val insertJson = it.optJSONObject("insertChildAtIdx")
//                    if (insertJson != null) {
//                        val index = insertJson.optInt("idx", -1)
//                        val insertChild = insertJson.optJSONObject("child")
//                        if (insertChild != null) {
//                            addViewByJSON(context, key, index, insertChild)
//                        }
//                    }
//
//                    it.optJSONObject("removeFromSuper")?.let {
//                        removeViewById(key)
//                    }
//                    it.optJSONArray("setChildren")?.let {
//                        children -> updateViewChildrenByJSON(context, key, children)
//                    }
//                    it.optJSONArray("addChildren")?.let {
//                        children -> addViewChildrenByJSON(context, key, children)
//                    }
                    updateChildren.add(childView)
                } else {
                    Log.i(DyManager.TAG, "invokeMethod id:$key not found")
//                    LogManager.getInstance().collectLog(DyManager.TAG, "invokeMethod id:$key not found")
                }
            }
        }

        return ParseH5Result(resultObj, updateChildren)
    }
}

data class ParseH5Result(val resultObj: JSONObject, val updateChildren: MutableList<DyBaseView>)