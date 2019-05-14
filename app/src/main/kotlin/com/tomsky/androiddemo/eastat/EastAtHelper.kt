package com.tomsky.androiddemo.eastat

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.view.KeyEvent
import android.widget.EditText
import com.iyao.eastat.KeyCodeDeleteHelper
import com.iyao.eastat.NoCopySpanEditableFactory
import com.iyao.eastat.SpanFactory
import com.iyao.eastat.span.DataBindingSpan
import com.iyao.eastat.watcher.SelectionSpanWatcher
import com.iyao.sample.Pound
import com.iyao.sample.User

class EastAtHelper {

    var editText: EditText? = null
    fun init(editText: EditText) {
        this.editText = editText
        editText.text = null
        editText.setEditableFactory(NoCopySpanEditableFactory(
                SelectionSpanWatcher(
                        DataBindingSpan::class)))
        editText.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                return@setOnKeyListener KeyCodeDeleteHelper.onDelDown(
                        (v as EditText).text)
            }
            return@setOnKeyListener false
        }
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
}