package com.tomsky.androiddemo

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.util.Log
import android.util.SparseArray
import android.util.SparseIntArray
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputConnectionWrapper
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.tomsky.androiddemo.R
import com.tomsky.androiddemo.customedit.CustomEditHelper
import com.tomsky.androiddemo.customedit.CustomEditText
import com.tomsky.androiddemo.util.UIUtils
import kotlinx.android.synthetic.main.activity_appbar.view.*


class StringEncodeActivity : AppCompatActivity(), InputListener{

    companion object {
        const val TAG = "TestStringEncode"
        const val TYPE_POUND = 1
        const val TYPE_AT = 2
    }

    var editInput: CustomEditText? = null
    var textOutput:TextView? = null
    var inputDialog:InputDialog? = null
//    var editHelper:CustomEditHelper = CustomEditHelper()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_string_encode)

        editInput = findViewById(R.id.et_input)
        textOutput = findViewById(R.id.tv_output)

//        editHelper.init(editInput)


        editInput?.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d(TAG, "s:$s, start:$start, after:$after, count:$count")

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d(TAG, "s:$s, start:$start, before:$before, count:$count")
                if (s != null) {
                    if (count == 1) { // add
                        if ('#' == s[start]) {
                            showPoundDialog()
                        } else if ('@' == s[start]) {
                            showAtDialog()
                        }
                    } else if (count == 0 && start > 0) { // delete
//                        editHelper.handleDelete(start)
                    }
                }
            }

        })



        findViewById<View>(R.id.btn_convert).setOnClickListener {

        }
    }

    /**
     * '#'号对话框
     */
    fun showPoundDialog() {
        if (inputDialog == null) {
            inputDialog = InputDialog(this)
        }
        inputDialog?.inputListener = this
        inputDialog?.show(TYPE_POUND)
    }

    /**
     * '@'号对话框
     */
    fun showAtDialog() {
        if (inputDialog == null) {
            inputDialog = InputDialog(this)
        }
        inputDialog?.inputListener = this
        inputDialog?.show(TYPE_AT)
    }

    override fun onInput(type: Int, text: String) {
        Log.d(TAG, "type:$type, text:$text")

        when(type) {
            TYPE_POUND -> {
                if (text.isEmpty()) {
                    return
                }
//                editHelper.appendPoundText(text)

            }
            TYPE_AT -> {
                if (text.isEmpty()) {
                    return
                }
//                editHelper.appendAtText(text)
            }
        }
    }


}


class InputDialog(context: Context?) : Dialog(context, R.style.Base_Theme_AppCompat_Dialog) {

    var type = 0

    var inputListener:InputListener? = null

    init {
        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL
        layout.gravity = Gravity.CENTER_HORIZONTAL
        layout.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val editText = EditText(context)
        editText.layoutParams = LinearLayout.LayoutParams(UIUtils.dp2px(220), UIUtils.dp2px(60))
        layout.addView(editText)

        val btn = Button(context)
        btn.layoutParams = LinearLayout.LayoutParams(UIUtils.dp2px(150), UIUtils.dp2px(40))
        btn.text = "确定"
        layout.addView(btn)

        btn.setOnClickListener {
            val text = editText.text
            var str = ""
            if (text != null && !TextUtils.isEmpty(text.toString())) {
                str = text.toString()
            }
            inputListener?.onInput(type, str)
            dismiss()
        }
        setContentView(layout)
        setCanceledOnTouchOutside(false)
        setCancelable(false)
    }

    fun show(type: Int) {
        this.type = type
        super.show()
    }
}


interface InputListener {
    fun onInput(type: Int, text: String)
}


