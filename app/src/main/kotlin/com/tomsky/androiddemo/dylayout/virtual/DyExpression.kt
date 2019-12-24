package com.tomsky.androiddemo.dylayout.virtual

/**
 *
 * Created by wangzhitao on 2019/12/24
 *
 **/

import android.util.Log
import com.tomsky.androiddemo.BuildConfig
import com.tomsky.androiddemo.util.StringUtil


import org.json.JSONArray
import org.json.JSONObject

import java.util.ArrayList
import java.util.Stack

class DyExpression(val prop: String, private val src: String) { // sync(proomid):p_game.scores[uid=$sync(liveid):p_user[pos=0].uid].nickname  原始数据 sync.p_game.scores.2.nickname

    var value: String? = null
        private set // 最终算出来的值

    val syncObservable = HashMap<String, String>() // key:id
    val apiObservable = HashSet<String>()

    fun parseKey(): DyExpression {
        if (!src.contains(":")) {
            return this
        }
        val keyArray = src.toCharArray()
        var sb = StringBuilder()
        var id = StringBuilder()

        var isSync = true
        var isKey = false
        var isId = false
        for (c in keyArray) {
            if (':' == c) {
                isSync = "sync" == sb.toString()
                sb.clear()
                isKey = true
            } else if ('.' == c) {
                if (sb.isNotEmpty()) {
                    if (isKey) {
                        if (isSync) {
                            if (id.isNotEmpty()) {
                                syncObservable[sb.toString()] = id.toString()
                                id.clear()
                            } else {
                                syncObservable[sb.toString()] = ""
                            }
                        } else {
                            apiObservable.add(sb.toString())
                        }
                    }
                    sb.clear()
                }
                isKey = false
            } else if ('[' == c) {
                if (sb.isNotEmpty()) {
                    if (isKey) {
                        if (isSync) {
                            if (id.isNotEmpty()) {
                                syncObservable[sb.toString()] = id.toString()
                                id = StringBuilder()
                            } else {
                                syncObservable[sb.toString()] = ""
                            }
                        } else {
                            apiObservable.add(sb.toString())
                        }
                    }
                    sb.clear()
                }
                isKey = false
            } else if (']' == c) {
            } else if ('$' == c) {
            } else if ('=' == c) {
                sb.clear()
            } else if ('(' == c) {
                id.clear()
                isId = true
            } else if (')' == c) {
                isId = false
            } else {
                if (isId) {
                    id.append(c)
                } else {
                    sb.append(c)
                }
            }
        }

        return this
    }

    fun parseValue(data: JSONObject?) {
        if (!src.contains(":")) {
            value = src
            return
        }

        if (data == null) return

        var currentFrame: StackFrame? = StackFrame()
        var sb = StringBuilder()

        val stack = Stack<StackFrame>()

        var canCalc = true

        var skip = false

        val keyArray = src.toCharArray()
        for (c in keyArray) {
            if (':' == c) {
                currentFrame!!.add(sb.toString())
                sb = StringBuilder()
            } else if ('.' == c) {
                if (sb.length > 0) {
                    currentFrame!!.add(sb.toString())
                    sb = StringBuilder()
                }
            } else if ('[' == c) {
                currentFrame!!.add(sb.toString())
                stack.push(currentFrame)
                currentFrame = StackFrame()
                sb = StringBuilder()
            } else if ('=' == c) {
                currentFrame!!.key = sb.toString()
                sb = StringBuilder()
            } else if ('$' == c) {
                currentFrame!!.shouldCalc = true
            } else if (']' == c) {
                val value = sb.toString()
                if (!currentFrame!!.shouldCalc) {
                    currentFrame.value = value
                    val stackFrame = stack.pop()
//                    if (stackFrame.shouldCalc || currentFrame.key == null) {
                    stackFrame.parseJSON(data, currentFrame)
//                    }
                    currentFrame = stackFrame
                } else {
                    currentFrame.add(value)
                    if (!currentFrame.canCalc) {
                        canCalc = false
                        break
                    }
                    currentFrame.calcValue(data)
                    if (!stack.isEmpty()) {
                        val stackFrame = stack.pop()
                        stackFrame.parseJSON(data, currentFrame)
                        currentFrame = stackFrame
                    }
                }
                sb = StringBuilder()

            } else if ('(' == c) {
                skip = true
            } else if (')' == c) {
                skip = false
            } else if (!skip) {
                sb.append(c)
            }

        }

        if (sb.length > 0) {
            currentFrame!!.add(sb.toString())
        }



        if (canCalc) {
            if (currentFrame != null) {
                val result = currentFrame.calcValue(data)
                if (BuildConfig.DEBUG) {
                    Log.i(TAG, "src:" +src +", " + currentFrame.propString() + " : " + result)
                }
                value = result
            }
        }

    }

    fun hasKeyChanged(key: String): Boolean {
        return syncObservable.contains(key)
    }

    class StackFrame {
        internal var canCalc = true
        internal var shouldCalc = false
        internal var key: String? = null
        var value: String? = null
            internal set
        internal var propList: MutableList<String> = ArrayList()

        fun size(): Int {
            return propList.size
        }

        fun add(key: String) {
            propList.add(key)
        }

        fun parseJSON(dataObject: JSONObject?, subFrame: StackFrame?) {
            if (dataObject == null) {
                canCalc = false
                return
            }
            canCalc = true
            var jsonObject: JSONObject = dataObject
            var propVal = ""
            for (prop in propList) {
                val obj = jsonObject.opt(prop)
                if (obj == null) {
                    canCalc = false
                    break
                }
                if (obj is JSONObject) {
                    jsonObject = obj
                } else if (obj is JSONArray) {
                    if (subFrame == null) {
                        canCalc = false
                        break
                    }
                    val key = subFrame.key
                    val value = subFrame.value
                    if (value == null) {
                        canCalc = false
                        break
                    }

                    if (key == null) { // value[2]
                        propVal = value
                        break
                    } else { // value[key=2]
                        val size = obj.length()
                        if (size <= 0) {
                            canCalc = false
                            break
                        }
                        for (i in 0 until size) {
                            val subObj = obj.optJSONObject(i)
                            if (subObj != null && subObj.has(key)) {
                                if (value == subObj.optString(key)) {
                                    propVal = "" + i
                                    break
                                }
                            }
                        }
                        canCalc = ("" != propVal)
                        if (!canCalc) {
                            break
                        }
                    }

                }
            }

            if ("" != propVal && canCalc) {
                propList.add(propVal)
            }
        }

        fun calcValue(dataObject: JSONObject): String? {
            if (canCalc) {
                var obj: Any? = dataObject
                val len = propList.size
                try {
                    for (i in 0 until len) {
                        val prop = propList[i]
                        if (obj is JSONObject) {
                            obj = obj.opt(prop)
                        } else if (obj is JSONArray) {
                            val index = Integer.valueOf(prop)
                            obj = obj.opt(index)
                        } else if (i == len - 1) {
                            value = obj.toString()
                        }
                    }
                    if (value == null && obj != null) {
                        value = obj.toString()
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "error", e)
                }

            }


            return value
        }

        fun propString(): String? {
            return StringUtil.listToString(propList, '.')
        }

    }

    companion object {

        private const val TAG = "dy_layout"
    }

}
