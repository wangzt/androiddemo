package com.tomsky.androiddemo.test

import android.util.Log

/**
 *
 * Created by wangzhitao on 2019/12/05
 *
 **/
class TestKotlin {
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
}