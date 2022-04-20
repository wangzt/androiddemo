package com.tomsky.androiddemo.grammar

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull

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

fun asyncSequence(){
    simpleSequence().forEach {
        value -> Log.i(GRAMMAR_TAG, "sequence: $value")
    }
}

suspend fun simpleList():List<Int> {
    Log.i(GRAMMAR_TAG, "suspend start")
    delay(1000) // 假装我们在这里做了一些异步的事情
    return listOf(1, 2, 3)
}

fun asyncSuspend() {
    runBlocking<Unit> {
        simpleList().forEach {
                value -> Log.i(GRAMMAR_TAG, "suspend: $value")
        }
    }
}

fun simpleFlow(): Flow<Int> = flow {
    Log.i(GRAMMAR_TAG, "Flow started")
    for (i in 1..3) {
        delay(100) // 假装我们在这里做了一些有用的事情
        Log.i(GRAMMAR_TAG, "emit: $i")
        emit(i) // 发送下一个值
    }
}

fun asyncFlow() {
    runBlocking<Unit> {
//        launch {
//            for (k in 1..3) {
//                Log.i(GRAMMAR_TAG, "I'm not blocked $k")
//                delay(100)
//            }
//        }
        Log.i(GRAMMAR_TAG, "Calling simple flow")
        simpleFlow().collect { value -> Log.i(GRAMMAR_TAG, "async flow: $value") }

        Log.i(GRAMMAR_TAG, "Calling simple flow again")
        simpleFlow().collect { value -> Log.i(GRAMMAR_TAG, "again async flow: $value") }
    }
}

// 在超时的情况下取消并停止执行
fun timeoutFlow() {
    runBlocking<Unit> {
        withTimeoutOrNull(250) {
            simpleFlow().collect { value -> Log.i(GRAMMAR_TAG, "value:$value") }
        }
        Log.i(GRAMMAR_TAG, "Done")
    }

}

suspend fun performRequest(req: Int): String {
    delay(1000)
    return "response: $req"
}

// 过渡流
fun mapFlow() {
    runBlocking<Unit> {
        (1..3).asFlow()
            .map { req -> performRequest(req) }
            .collect { res -> Log.i(GRAMMAR_TAG, res) }
    }
}

// 转换操作符
fun transformFlow() {
    runBlocking<Unit> {
        (1..3).asFlow()
            .transform { req ->
                emit("Making req: $req")
                emit(performRequest(req))
            }
            .collect { res -> Log.i(GRAMMAR_TAG, res) }
    }
}


