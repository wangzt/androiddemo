package com.tomsky.androiddemo.grammar

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CoroutineChannel {

    fun test() {
        produceChannel()
    }

    private fun baseChannel() = runBlocking {
        val channel = Channel<Int>()
        launch {
            for (x in 1..5) channel.send(x * x)
        }
        repeat(5){ Log.i(GRAMMAR_TAG, "${channel.receive()}") }
        Log.i(GRAMMAR_TAG, "Done!")
    }

    private fun closeChannel() = runBlocking {
        val channel = Channel<Int>()
        launch {
            for (x in 1..5) channel.send(x * x)
            channel.close()
        }
        for (y in channel) Log.i(GRAMMAR_TAG, "$y")
        Log.i(GRAMMAR_TAG, "Done!")
    }

    private fun CoroutineScope.produceSquares(): ReceiveChannel<Int> = produce {
        for (x in 1..5) send(x * x)
    }
    private fun produceChannel() = runBlocking {
        val squares = produceSquares()
        squares.consumeEach { Log.i(GRAMMAR_TAG, "$it") }
        Log.i(GRAMMAR_TAG, "Done!")
    }
}