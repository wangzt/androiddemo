package com.tomsky.androiddemo.dylayout

import android.content.Context
import android.util.Log
import android.widget.FrameLayout
import com.tomsky.androiddemo.dylayout.virtual.DyContext
import com.tomsky.androiddemo.dylayout.virtual.views.DyBaseView
import com.tomsky.androiddemo.dylayout.virtual.views.DyRootView
import com.tomsky.androiddemo.dylayout.virtual.views.DyView
import com.tomsky.androiddemo.util.JobWorker
import org.json.JSONObject

/**
 *
 * Created by wangzhitao on 2019/12/19
 *
 **/

class DyManager {

    companion object {
        const val TAG = "dy_layout"
    }

    var dyParser = DyParser()

    var dyRender = DyRender()

    var dyContext = DyContext()

    private var containerView: FrameLayout? = null
    private var context: Context? = null

    fun setContainerView(containerView: FrameLayout?, context: Context?) {
        this.containerView = containerView
        this.context = context
    }

    fun parseFullLayout(data: JSONObject) {
        JobWorker.submit_SingleThread<DyRootView>(object : JobWorker.Task<DyRootView?>() {
            override fun doInBackground(): DyRootView? {
                Log.i(TAG, "parseFullLayout int thread:${Thread.currentThread()}")
                var result:DyRootView? = null
                try {
                    result = dyParser.parseFullLayout(dyContext, data)
                    printLayoutProps(DyView.PRINT_PREFIX, result)
                } catch (e:Exception) {
                    Log.e(TAG, "error", e)
                }

                return result
            }

            override fun onComplete(result: DyRootView?) {
                result?.let {
                    Log.i(TAG, "parseFullLayout complete, int thread:${Thread.currentThread()}")
                    if (context != null && containerView != null) {
                        dyRender.createFullRenderView(context!!, containerView!!, it)
                    }
                }
            }
        })
    }

    fun invokeLayoutFromH5(data: JSONObject) {
        JobWorker.submit_SingleThread<ParseH5Result>(object : JobWorker.Task<ParseH5Result?>() {
            override fun doInBackground(): ParseH5Result? {
                Log.i(TAG, "invokeLayoutFromH5 int thread:${Thread.currentThread()}")
                var result:ParseH5Result? = null
                try {
                    result = dyParser.parseH5Method(dyContext, data)
                } catch (e:Exception) {
                    Log.e(TAG, "error", e)
                }

                return result
            }

            override fun onComplete(result: ParseH5Result?) {
                result?.let {
                    Log.i(TAG, "invokeLayoutFromH5 complete, int thread:${Thread.currentThread()}")
                    if (context != null && containerView != null) {
                        dyRender.updateRenderView(context!!, it.updateChildren)
                    }
                }
            }
        })

    }

    private fun printLayoutProps(prefix: String, view: DyBaseView) {
        Log.i(TAG, prefix + view.name)
        if (view is DyView) {
            for (child: DyBaseView in view.childList) {
                printLayoutProps(prefix + DyView.PRINT_PREFIX, child)
            }
        }
    }
}