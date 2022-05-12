package com.tomsky.androiddemo.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tomsky.androiddemo.R
import com.tomsky.androiddemo.util.UIUtils

/**
 *
 * Created by wangzhitao on 2020/11/23
 *
 **/
class CustomOutLineView (context: Context, attrs: AttributeSet?) :
        ConstraintLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_custom_outline, this)

        outlineProvider = TextureVideoViewOutlineProvider(UIUtils.dp2px(8).toFloat())
        clipToOutline = true
    }

}