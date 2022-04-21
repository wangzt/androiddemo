package com.tomsky.androiddemo.grammar

import android.util.Log
import kotlinx.coroutines.*

/**
 *
 * Created by wangzhitao on 2022/04/21
 *
 **/
class CoroutineGrammar {

    fun test() {
        runBlocking {
            globalScope()
        }
    }

    private fun scope() = runBlocking {
        launch {
            delay(200L)
            Log.i(GRAMMAR_TAG, "Task from runBlocking")
        }
        coroutineScope { // 创建一个协程作用域
            launch {
                delay(500L)
                Log.i(GRAMMAR_TAG, "Task from nested launch")
            }
            delay(100L)
            Log.i(GRAMMAR_TAG, "Task from coroutine scope")
        }
        Log.i(GRAMMAR_TAG, "Coroutine scope is over")
    }

    private suspend fun globalScope() {
        Log.i(GRAMMAR_TAG, "Start global")
        val job = GlobalScope.launch {
            repeat(1000) { i ->
                Log.i(GRAMMAR_TAG, "I'm sleeping $i ...")
                delay(500L)
            }
        }

        delay(1300L)
        job.cancelAndJoin()
        Log.i(GRAMMAR_TAG, "Over global")
    }
}