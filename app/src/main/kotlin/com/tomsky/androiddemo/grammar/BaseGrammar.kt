package com.tomsky.androiddemo.grammar

import android.util.Log
import kotlin.random.Random

class BaseGrammar {

    fun test() {
//        val array = arrayOfMinusOnes(10)
//        Log.i(GRAMMAR_TAG, "size:${array.size}")
//        array.forEachIndexed { index, i -> Log.i(GRAMMAR_TAG, "index:$index, value:$i") }

        val i = getRandomInt()
        Log.i(GRAMMAR_TAG, "i:$i")
    }

    private fun arrayOfMinusOnes(size: Int): IntArray {
        return IntArray(size).apply { fill(-1) }
    }

    private fun getRandomInt(): Int {
        return Random.nextInt(100).also {
            Log.i(GRAMMAR_TAG, "getRandomInt() generated value $it")
        }
    }
}