package com.iyao.eastat

import android.text.Selection
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.util.Log
import com.iyao.eastat.span.DataBindingSpan

object KeyCodeDeleteHelper {
    fun onDelDown(text: Spannable): Boolean {

        // 单独append空格，使用下面的微博方式
//        val selectionStart = Selection.getSelectionStart(text)
//        val selectionEnd = Selection.getSelectionEnd(text)
//        text.getSpans(selectionStart, selectionEnd, DataBindingSpan::class.java).firstOrNull { text.getSpanEnd(it) == selectionStart }?.run {
//            return (selectionStart == selectionEnd).also {
//                val spanStart = text.getSpanStart(this)
//                val spanEnd = text.getSpanEnd(this)
//                Selection.setSelection(text, spanStart, spanEnd)
//            }
//        }

        val selectionStart = Selection.getSelectionStart(text)
        val selectionEnd = Selection.getSelectionEnd(text)
        text.getSpans(selectionStart, selectionEnd, DataBindingSpan::class.java).firstOrNull { text.getSpanEnd(it) == selectionStart }?.run {

            return (selectionStart == selectionEnd).also {
                val spanStart = text.getSpanStart(this)
                val spanEnd = text.getSpanEnd(this)
                (text as SpannableStringBuilder).delete(spanStart, spanEnd)
            }
        }
        return false
    }
}