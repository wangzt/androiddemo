package com.tomsky.androiddemo.grammar

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

/**
 *
 * Created by wangzhitao on 2022/04/20
 *
 **/
const val GRAMMAR_TAG = "kotlin_grammar"

fun simpleSequence():Sequence<Int> = sequence {
    for (i in 1..3) {
        Thread.sleep(100)
        yield(i)
    }
}

fun flowSequence(){
    simpleSequence().forEach {
        value -> Log.i(GRAMMAR_TAG, "flow: $value")
    }
}

suspend fun simpleList():List<Int> {
    Log.i(GRAMMAR_TAG, "suspend flow start")
    delay(1000) // 假装我们在这里做了一些异步的事情
    return listOf(1, 2, 3)
}

fun flowSuspend() {
    runBlocking<Unit> {
        simpleList().forEach {
                value -> Log.i(GRAMMAR_TAG, "suspend flow: $value")
        }
    }
}
