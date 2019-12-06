package com.tomsky.androiddemo.test

import android.util.Log
import kotlinx.coroutines.*

/**
 *
 * Created by wangzhitao on 2019/12/05
 *
 **/
class TestKotlin {

    companion object {
        const val TAG = "wzt-kt"
    }
    object Singleton {
        fun test() {
            val list = "abc"
            var re = list.indexOf("d").takeIf {
                it > 0
            }?.run {
                list[this]
            }?: run {
                "not found"
            }
            Log.i("wzt-kt", "list:$list, re:$re")

        }
    }

    fun coroutineTest() {
        test2()
    }

    private fun test1() {
        Log.d(TAG, "协程初始化开始，时间: " + System.currentTimeMillis())

        val job:Job = GlobalScope.launch(start = CoroutineStart.LAZY) {
            Log.d(TAG, "协程初始化完成，时间: " + System.currentTimeMillis())
            for (i in 1..3) {
                Log.d(TAG, "协程任务1打印第$i 次，时间: " + System.currentTimeMillis())
            }
            delay(500)
            for (i in 1..3) {
                Log.d(TAG, "协程任务2打印第$i 次，时间: " + System.currentTimeMillis())
            }
        }

        Log.d(TAG, "主线程 sleep ，时间: " + System.currentTimeMillis())
        Thread.sleep(1000)
        job.start()
        Log.d(TAG, "主线程运行，时间: " + System.currentTimeMillis())

        for (i in 1..3) {
            Log.d(TAG, "主线程打印第$i 次，时间: " + System.currentTimeMillis())
        }
    }

    private fun test2() {
        GlobalScope.launch(Dispatchers.Unconfined) {
            val deferred = GlobalScope.async{
                delay(1000L)
                Log.d(TAG,"This is async ")
                return@async "taonce"
            }

            Log.d(TAG,"协程 other start")
            val result = deferred.await()
            Log.d(TAG,"async result is $result")
            Log.d(TAG,"协程 other end ")
        }

        Log.d(TAG, "主线程位于协程之后的代码执行，时间:  ${System.currentTimeMillis()}")
    }
}