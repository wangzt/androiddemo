package com.tomsky.androiddemo.eastat

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.KeyEvent
import android.widget.EditText
import com.iyao.eastat.KeyCodeDeleteHelper
import com.iyao.eastat.NoCopySpanEditableFactory
import com.iyao.eastat.SpanFactory
import com.iyao.eastat.span.DataBindingSpan
import com.iyao.eastat.watcher.SelectionSpanWatcher
import com.iyao.sample.Pound
import com.iyao.sample.User

class EastAtHelper : DeleteKeyEventListener{
    override fun onDeleteClick(v: KeyCodeDeleteEditText): Boolean {
        return KeyCodeDeleteHelper.onDelDown(
                v.text)
    }

    companion object {
        const val TAG = "east-helper"
    }

    var editText: KeyCodeDeleteEditText? = null
    fun init(editText: KeyCodeDeleteEditText) {
        this.editText = editText
        editText.deleteListener = this
        editText.text = null
        editText.setEditableFactory(NoCopySpanEditableFactory(
                SelectionSpanWatcher(
                        DataBindingSpan::class)))
//        editText.setOnKeyListener { v, keyCode, event ->
//            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
//                return@setOnKeyListener KeyCodeDeleteHelper.onDelDown(
//                        (v as EditText).text)
//            }
//            return@setOnKeyListener false
//        }
    }

    private fun newUserSpannable(user: User): Spannable {
        return SpanFactory.newSpannable(user.getSpannedName(), user)
    }

    private fun newPoundSpannable(user: Pound): Spannable {
        return SpanFactory.newSpannable(user.getSpannedName(), user)
    }

    fun appendAtText(text: String) {
        // TODO:删除已经加入的'＠'符号
        var user = User("123456", text)
        (editText?.text as SpannableStringBuilder).append(newUserSpannable(user)).append(" ")
    }

    fun appendPoundText(text: String) {
        // TODO:删除已经加入的'#'符号
        var user = Pound("$text#")
        (editText?.text as SpannableStringBuilder).append(newPoundSpannable(user)).append(" ")
    }

    fun checkCurrentSpan() {
        var length = editText?.text?.length ?: 0

        if (length > 1) {

            (editText?.text as SpannableStringBuilder).getSpans(0, length, User::class.java).forEach {
                Log.d(TAG, "at str: ${it.name}, start:${editText?.text?.getSpanStart(it)}, end:${editText?.text?.getSpanEnd(it)}")
            }

            (editText?.text as SpannableStringBuilder).getSpans(0, length, Pound::class.java).forEach {
                Log.d(TAG, "pound str: ${it.name}, start:${editText?.text?.getSpanStart(it)}, end:${editText?.text?.getSpanEnd(it)}")
            }
        }
    }
}