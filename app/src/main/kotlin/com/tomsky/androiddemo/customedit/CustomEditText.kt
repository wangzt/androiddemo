package com.tomsky.androiddemo.customedit

import android.content.Context
import android.graphics.Color
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.util.Log
import android.util.SparseArray
import android.util.SparseIntArray
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputConnectionWrapper
import android.widget.EditText
import com.tomsky.androiddemo.StringEncodeActivity

class CustomEditText(context: Context?, attrs: AttributeSet?) : EditText(context, attrs) {
    var selectionListener: SelectionChangeListener? = null
    var deleteListener: DeleteKeyEventListener? = null

    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        var handled = selectionListener?.handleSelectionChange(selStart, selEnd) ?: false
        if (!handled) {
            super.onSelectionChanged(selStart, selEnd)
        }
    }

    override fun onCreateInputConnection(outAttrs: EditorInfo?): InputConnection {
        return DeleteInputConnection(super.onCreateInputConnection(outAttrs), true)
    }

    inner class DeleteInputConnection(target: InputConnection, mutable:Boolean): InputConnectionWrapper(target, mutable) {

        override fun sendKeyEvent(event: KeyEvent?): Boolean {
            if (event?.action == KeyEvent.ACTION_DOWN
                    && event?.keyCode == KeyEvent.KEYCODE_DEL) {
                var handled = this@CustomEditText.deleteListener?.onDeleteClick() ?: false
                if (handled) {
                    return true
                }
            }

            return super.sendKeyEvent(event)
        }

        override fun deleteSurroundingText(beforeLength: Int, afterLength: Int): Boolean {
            return if (beforeLength === 1 && afterLength === 0) {
                sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN,
                        KeyEvent.KEYCODE_DEL)) && sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP,
                        KeyEvent.KEYCODE_DEL))
            } else super.deleteSurroundingText(beforeLength, afterLength)
        }

    }
}

class SelectData(val start:Int, val end:Int) {
    var center = 0
    init {
        center = (start + end) / 2
    }

    fun calcSelectIndex(index:Int):Int {
        if (index in start..end) {
            return if (index > center) {
                end+1
            } else {
                start
            }
        }

        return -1
    }
}

class CustomEditHelper: SelectionChangeListener, DeleteKeyEventListener{
    val poundMap: SparseIntArray = SparseIntArray()
    val atMap: SparseArray<String> = SparseArray()
    val selectMap: SparseArray<SelectData> = SparseArray()

    var editInput:CustomEditText? = null

    fun init(editInput:CustomEditText?) {
        this.editInput = editInput
        editInput?.movementMethod = LinkMovementMethod.getInstance()
        editInput?.selectionListener = this
        editInput?.deleteListener = this
    }

    fun appendPoundText(text:String) {
        var start = editInput?.editableText?.length?.minus(1) ?: 0
        if (start < 0) {
            start = 0
        }

        var end = start + text.length + 2
        Log.d(StringEncodeActivity.TAG, "pound, end:$end, start:$start")

        poundMap.put(end, start)
        selectMap.put(end, SelectData(start, end))
        editInput?.editableText?.append("$text# ")
        editInput?.editableText?.setSpan(ForegroundColorSpan(Color.BLUE), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    fun appendAtText(text:String) {
        var start = editInput?.editableText?.length?.minus(1) ?: 0
        if (start < 0) {
            start = 0
        }

        var end = start + text.length + 1
        Log.d(StringEncodeActivity.TAG, "at, end:$end, start:$start")
        atMap.put(end, start.toString())
        selectMap.put(end, SelectData(start, end))
        editInput?.editableText?.append("$text ")
        editInput?.editableText?.setSpan(ForegroundColorSpan(Color.BLUE), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    fun handleDelete(start:Int) {
        if (poundMap.indexOfKey(start) > -1) {
            try {
                val delete = poundMap.get(start)
                Log.d(StringEncodeActivity.TAG, "pound, delete:$delete, start:$start")
                if (delete == 0) {
                    editInput?.setText("")
                } else {
                    editInput?.editableText?.delete(delete, start)
                }
                poundMap.delete(start)
                selectMap.delete(start)
            } catch (e:Throwable) {
                Log.e(StringEncodeActivity.TAG, "error", e)
            }

        } else if (atMap.indexOfKey(start) > -1) {

            try {
                val delete = atMap.get(start).toInt()
                Log.d(StringEncodeActivity.TAG, "at, delete:$delete, start:$start")
                if (delete == 0) {
                    editInput?.setText("")
                } else {
                    editInput?.editableText?.delete(delete, start)
                }
                atMap.delete(start)
                selectMap.delete(start)
            } catch (e:Throwable) {
                Log.e(StringEncodeActivity.TAG, "error", e)
            }
        }
    }

    override fun handleSelectionChange(selStart: Int, selEnd: Int): Boolean {
        val size = selectMap.size()
        for (i in 0 until size) {
            var selectData: SelectData = selectMap.valueAt(i)
            var index = selectData.calcSelectIndex(selStart)
            if (index > -1) {
//                Log.d(TAG, "handleSelectionChange, index:$index, selStart:$selStart, true")
                editInput?.setSelection(index)
                return true
            }
        }

//        if (selStart == -1) {
//            Log.d(TAG, "handleSelectionChange, selStart:-1")
//            editInput?.setSelection(0)
//            return true
//        }
//        Log.d(TAG, "handleSelectionChange, selStart:$selStart, false")
        return false
    }

    override fun onDeleteClick():Boolean {
        var select = editInput?.selectionStart ?: -1
        Log.d(StringEncodeActivity.TAG, "------onDeleteClick, select:$select")
//        if (select > 0) {
//            if (selectMap.indexOfKey(select - 1) > -1) {
//                var selectData = selectMap.get(select - 1)
//                if (selectData != null) {
//                    editInput?.setSelection(selectData.start, selectData.end)
//                    return true
//                }
//            }
//        }
        return false
    }
}

interface SelectionChangeListener {
    fun handleSelectionChange(selStart: Int, selEnd: Int):Boolean
}

interface DeleteKeyEventListener {
    fun onDeleteClick():Boolean
}