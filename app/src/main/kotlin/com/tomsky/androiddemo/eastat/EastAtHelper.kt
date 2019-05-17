package com.tomsky.androiddemo.eastat

import android.text.Selection
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

        var length = (editText?.text as SpannableStringBuilder).length
        var start = Selection.getSelectionStart(editText?.text)
        var end = Selection.getSelectionEnd(editText?.text)

        var preIndex = start - 1

        if (preIndex > -1 && preIndex < length) {
            var startChar = editText?.text?.get(preIndex) ?: ' '
            if (startChar == '@') {
                (editText?.text as SpannableStringBuilder).delete(preIndex, preIndex+1)
            } else{
                preIndex = start
            }
        } else{
            preIndex = start
        }

        if (start == length) { // append
            (editText?.text as SpannableStringBuilder).append(newUserSpannable(user)).append(" ")
        } else { // insert
            var span:Spannable = newUserSpannable(user)
            (editText?.text as SpannableStringBuilder).insert(preIndex, span).insert(preIndex + span.length, " ")
        }

        Log.d(TAG, "appendAtText, start:$start, end:$end, length:$length, preIndex:$preIndex")

    }

    fun appendPoundText(text: String) {
        // TODO:删除已经加入的'#'符号
        var user = Pound("$text")
        var length = (editText?.text as SpannableStringBuilder).length
        var start = Selection.getSelectionStart(editText?.text)
        var end = Selection.getSelectionEnd(editText?.text)

        var preIndex = start - 1

        if (preIndex > -1 && preIndex < length) {
            var startChar = editText?.text?.get(preIndex) ?: ' '
            if (startChar == '#') {
                (editText?.text as SpannableStringBuilder).delete(preIndex, preIndex+1)
            } else{
                preIndex = start
            }
        } else{
            preIndex = start
        }

        if (start == length) { // append
            (editText?.text as SpannableStringBuilder).append(newPoundSpannable(user)).append(" ")
        } else { // insert
            var span:Spannable = newPoundSpannable(user)
            (editText?.text as SpannableStringBuilder).insert(preIndex, span).insert(preIndex + span.length, " ")
        }
        Log.d(TAG, "appendPoundText, start:$start, end:$end, length:$length, preIndex:$preIndex")


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