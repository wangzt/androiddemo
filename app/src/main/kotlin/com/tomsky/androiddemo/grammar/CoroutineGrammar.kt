package com.tomsky.androiddemo.grammar

import android.util.Log
import kotlinx.coroutines.*
import java.lang.ArithmeticException
import kotlin.system.*

/**
 *
 * Created by wangzhitao on 2022/04/21
 *
 **/
class CoroutineGrammar {

    fun test() {
        subCoroutine()
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

    private suspend fun doSomethingUsefullOne():Int {
        delay(1000L)
        return 13
    }

    private suspend fun doSomethingUsefullTwo():Int {
        delay(1000L)
        return 29
    }

    private suspend fun concurrentSum(): Int = coroutineScope {
        val one = async { doSomethingUsefullOne() }
        val two = async { doSomethingUsefullTwo() }
        one.await() +two.await()
    }

    private fun seqCall() = runBlocking {
        // 顺序执行
//        val time = measureTimeMillis {
//            val one =  doSomethingUsefullOne()
//            val two = doSomethingUsefullTwo()
//            Log.i(GRAMMAR_TAG, "The answer is ${one + two}")
//        }

        // 显式并发 async
//        val time = measureTimeMillis {
//            val one = async { doSomethingUsefullOne() }
//            val two = async { doSomethingUsefullTwo() }
//            Log.i(GRAMMAR_TAG, "The answer is ${one.await() + two.await()}")
//        }

        // 惰性启动async
        // 在这个模式下，只有结果通过 await 获取的时候协程才会启动，或者在 Job 的 start 函数调用的时候
        // 注意，如果我们只是在 println 中调用 await，而没有在单独的协程中调用 start，这将会导致顺序行为，直到 await 启动该协程 执行并等待至它结束
//        val time = measureTimeMillis {
//            val one = async(start = CoroutineStart.LAZY) { doSomethingUsefullOne() }
//            val two = async(start = CoroutineStart.LAZY) { doSomethingUsefullTwo() }
//            one.start() // 启动第一个
//            two.start() // 启动第二个
//            Log.i(GRAMMAR_TAG, "The answer is ${one.await() + two.await()}")
//        }

        // 使用async的结构化并发，这里也是并发执行的
        // 这种情况下，如果在 concurrentSum 函数内部发生了错误，并且它抛出了一个异常， 所有在作用域中启动的协程都会被取消
//        val time = measureTimeMillis {
//            Log.i(GRAMMAR_TAG, "The answer is ${concurrentSum()}")
//        }

//        Log.i(GRAMMAR_TAG, "Complete in $time ms")

        // 如果其中一个子协程（即 two）失败，第一个 async 以及等待中的父协程都会被取消
        try {
            failedConcurrentSum()
        } catch (e: ArithmeticException) {
            Log.i(GRAMMAR_TAG, "Computation failed with ArithmeticException")
        }
    }

    private suspend fun failedConcurrentSum():Int = coroutineScope {
        val one = async<Int> {
            try {
                delay(Long.MAX_VALUE)
                42
            } finally {
                Log.i(GRAMMAR_TAG, "First child was cancelled")
            }
        }

        val two = async<Int> {
            Log.i(GRAMMAR_TAG, "Second child throws an exception")
            throw ArithmeticException()
        }

        one.await() + two.await()
    }

    private fun coroutineDispatchers() = runBlocking {
        launch { // 运行在父协程的上下文中，即 runBlocking 主协程
            Log.i(GRAMMAR_TAG, "main runBlocking      : I'm working in thread ${Thread.currentThread().name}")
        }

        launch(Dispatchers.Unconfined) { // 不受限的——将工作在主线程中
            Log.i(GRAMMAR_TAG, "Unconfined            : I'm working in thread ${Thread.currentThread().name}")
        }

        launch(Dispatchers.Default) { // 将会获取默认调度器
            Log.i(GRAMMAR_TAG, "Default            : I'm working in thread ${Thread.currentThread().name}")
        }

        launch(newSingleThreadContext("MyOwnThread")) { // 将使它获得一个新的线程
            Log.i(GRAMMAR_TAG, "MyOwnThread            : I'm working in thread ${Thread.currentThread().name}")
        }
    }

    private fun subCoroutine() = runBlocking {
        // 启动一个协程来处理某种传入请求（request）
        val request = launch {  
            repeat(3) { i -> // 启动少量的子作业
                launch  {
                    delay((i + 1) * 200L) // 延迟 200 毫秒、400 毫秒、600 毫秒的时间
                    Log.i(GRAMMAR_TAG,"Coroutine $i is done")
                }
            }
            Log.i(GRAMMAR_TAG,"request: I'm done and I don't explicitly join my children that are still active")
        }
        request.join() // 等待请求的完成，包括其所有子协程
        Log.i(GRAMMAR_TAG,"Now processing of the request is complete")
    }
}