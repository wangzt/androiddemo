package com.tomsky.androiddemo.eastat

import android.content.ClipboardManager
import android.content.Context
import android.text.Editable
import android.text.Selection
import android.text.Spannable
import android.text.Spanned
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

    override fun onTextContextMenuItem(id: Int): Boolean {
        if (id == android.R.id.paste) {
            var min = 0
            var max = text.length
            var withFormatting = false
            if (isFocused) {
                val selStart = selectionStart
                val selEnd = selectionEnd

                min = Math.max(0, Math.min(selStart, selEnd))
                max = Math.max(0, Math.max(selStart, selEnd))
            }

            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = clipboard.primaryClip
            if (clip != null) {
                var didFirst = false
                for (i in 0 until clip.itemCount) {
                    val paste: CharSequence?
                    if (withFormatting) {
                        paste = clip.getItemAt(i).coerceToStyledText(context)
                    } else {
                        // Get an item as text and remove all spans by toString().
                        val txt = clip.getItemAt(i).coerceToText(context)
                        paste = (txt as? Spanned)?.toString() ?: txt
                    }
                    if (paste != null) {
                        if (!didFirst) {
                            Selection.setSelection(text as Spannable, max)
                            (text as Editable).replace(min, max, paste)
                            didFirst = true
                        } else {
                            (text as Editable).insert(selectionEnd, "\n")
                            (text as Editable).insert(selectionEnd, paste)
                        }
                    }
                }
//                sLastCutCopyOrTextChangedTime = 0
            }
            return true
        }
        return super.onTextContextMenuItem(id)
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
