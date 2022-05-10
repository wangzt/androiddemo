package com.tomsky.androiddemo.grammar

import android.util.Log
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FlowGrammar {
    fun test(scope: CoroutineScope, shareScope: CoroutineScope) {
        coldFlow(scope, shareScope)
    }

    // 冷流
    private fun coldFlow(scope: CoroutineScope, shareScope: CoroutineScope) {
        scope.launch {
            flow {
                for (i in 1..100) {
                    Log.i(GRAMMAR_TAG, "---生产：$i")
                    emit(i)
                }
            }.buffer(
                onBufferOverflow = BufferOverflow.DROP_OLDEST
            )
                .shareIn(
                    shareScope,
                SharingStarted.Eagerly
            )
                .collect {
                delay(1000)
                Log.i(GRAMMAR_TAG, "收集:$it")

            }
        }
    }
}