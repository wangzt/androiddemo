package com.tomsky.androiddemo.eastat

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputConnectionWrapper
import android.widget.EditText

class KeyCodeDeleteEditText(context: Context?, attrs: AttributeSet?) : EditText(context, attrs) {
    var deleteListener: DeleteKeyEventListener? = null

    override fun onCreateInputConnection(outAttrs: EditorInfo?): InputConnection {
        return DeleteInputConnection(super.onCreateInputConnection(outAttrs), true)
    }

    inner class DeleteInputConnection(target: InputConnection, mutable:Boolean): InputConnectionWrapper(target, mutable) {

        override fun sendKeyEvent(event: KeyEvent?): Boolean {
            if (event?.action == KeyEvent.ACTION_DOWN
                    && event?.keyCode == KeyEvent.KEYCODE_DEL) {
                var handled = this@KeyCodeDeleteEditText.deleteListener?.onDeleteClick(this@KeyCodeDeleteEditText) ?: false
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

interface DeleteKeyEventListener {
    fun onDeleteClick(editText: KeyCodeDeleteEditText):Boolean
}
