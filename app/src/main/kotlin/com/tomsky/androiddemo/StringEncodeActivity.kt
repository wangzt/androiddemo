package com.tomsky.androiddemo

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.iyao.sample.Pound
import com.iyao.sample.User
import com.tomsky.androiddemo.eastat.EastAtHelper
import com.tomsky.androiddemo.eastat.KeyCodeDeleteEditText
import com.tomsky.androiddemo.util.UIUtils
import org.json.JSONArray
import java.util.regex.Matcher
import java.util.regex.Pattern


class StringEncodeActivity : AppCompatActivity(), InputListener{

    companion object {
        const val TAG = "TestStringEncode"
        const val TYPE_POUND = 1
        const val TYPE_AT = 2
    }

    var editInput: KeyCodeDeleteEditText? = null
    var textOutput:TextView? = null
    var inputDialog:InputDialog? = null
//    var editHelper:CustomEditHelper = CustomEditHelper()

    var eastAtHelper: EastAtHelper = EastAtHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_string_encode)

        editInput = findViewById(R.id.et_input)
        textOutput = findViewById(R.id.tv_output)

        textOutput?.movementMethod = MyLinkMovementMethod.getInstance()

//        editHelper.init(editInput)

        eastAtHelper.init(editInput!!)

        editInput?.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                Log.d(TAG, "s:$s, start:$start, after:$after, count:$count")

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                Log.d(TAG, "s:$s, start:$start, before:$before, count:$count")
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
            convertEditValueToTextView()
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
                eastAtHelper.appendPoundText(text)
//                editHelper.appendPoundText(text)

            }
            TYPE_AT -> {
                if (text.isEmpty()) {
                    return
                }
                eastAtHelper.appendAtText(text)
//                editHelper.appendAtText(text)
            }
        }
    }

    private fun convertEditValueToTextView() {
        var length = editInput?.text?.length ?: 0

        if (length > 1) {

            var users = JSONArray()
            (editInput?.text as SpannableStringBuilder).getSpans(0, length, User::class.java).forEach {
                //                Log.d(TAG, "at str: ${it.name}, start:${editText?.text?.getSpanStart(it)}, end:${editText?.text?.getSpanEnd(it)}")
                users.put("${it.id}-${it.name}")
            }

            var pounds = JSONArray()
            (editInput?.text as SpannableStringBuilder).getSpans(0, length, Pound::class.java).forEach {
                //                Log.d(TAG, "pound str: ${it.name}, start:${editText?.text?.getSpanStart(it)}, end:${editText?.text?.getSpanEnd(it)}")
                pounds.put(it.name)
            }

            var outText =  editInput?.text?.toString() ?: ""

            var textSpanBuilder = SpannableStringBuilder(outText)

            var userSize = users.length()
            for (i in 0 until userSize) {
                var user:String = users[i].toString()
                var name = user.substring(user.indexOf("-")+1)
                Log.d(TAG, "name:$name")

                var nameSize = name.length+1
                var pattern: Pattern = Pattern.compile("@$name")
                var matcher: Matcher = pattern.matcher(outText)
                while (matcher.find()) {
                    Log.d(TAG, "user: ${matcher.start()}")
                    var start = matcher.start()
//                    textSpanBuilder.setSpan(ForegroundColorSpan(Color.BLUE), start, start + nameSize, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    textSpanBuilder.setSpan(MyClickableSpan(name), start, start + nameSize, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }

            var poundSize = pounds.length()
            for (i in 0 until poundSize) {
                var name:String = pounds[i].toString()
                Log.d(TAG, "pound:$name")

                var nameSize = name.length+1
                var pattern: Pattern = Pattern.compile("#$name")
                var matcher: Matcher = pattern.matcher(outText)
                while (matcher.find()) {
                    Log.d(TAG, "pound: ${matcher.start()}")
                    var start = matcher.start()
//                    textSpanBuilder.setSpan(ForegroundColorSpan(Color.BLUE), start, start + nameSize, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    textSpanBuilder.setSpan(MyClickableSpan(name), start, start + nameSize, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }


            textOutput?.text = textSpanBuilder
        }
    }
}

class MyClickableSpan(val name:String): ClickableSpan() {
    override fun onClick(widget: View?) {
        Log.d("TestStringEncode", "onClick:$name")
    }

    override fun updateDrawState(ds: TextPaint?) {
        ds?.color = Color.BLUE
        ds?.isUnderlineText = false
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


