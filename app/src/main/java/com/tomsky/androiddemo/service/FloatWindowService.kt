package com.tomsky.androiddemo.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.os.Binder
import android.os.IBinder
import android.util.Log
import android.view.*
import android.view.View.OnTouchListener
import android.widget.RelativeLayout
import com.tomsky.androiddemo.R
import com.tomsky.androiddemo.activity.FloatActivity
import com.tomsky.androiddemo.service.FloatWindowService
import com.tomsky.androiddemo.util.LogUtils
import com.tomsky.androiddemo.util.UIUtils

/**
 * Created by wangzhitao on 2020/06/05
 */
class FloatWindowService : Service() {
    private var mWindowManager: WindowManager? = null
    private var wmParams: WindowManager.LayoutParams? = null
    private var inflater: LayoutInflater? = null

    //constant
    private val clickflag = false

    //view
    private var mFloatingLayout //浮动布局
            : View? = null
    private var mContainerLayout // 容器
            :View? = null
    private var smallSizePreviewLayout //容器父布局
            : RelativeLayout? = null

    override fun onBind(intent: Intent): IBinder? {
        return MyBinder()
    }

    inner class MyBinder : Binder() {
        val service: FloatWindowService
            get() = this@FloatWindowService
    }

    override fun onCreate() {
        super.onCreate()
        initWindow() //设置悬浮窗基本参数（位置、宽高等）
        initFloating() //悬浮框点击事件的处理
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val result = super.onStartCommand(intent, flags, startId)
        if (intent != null) {
            if (intent.hasExtra(FloatActivity.EXTRA_NAME)) {
                val name = intent.getStringExtra(FloatActivity.EXTRA_NAME)
                LogUtils.i(TAG, "name: $name")
            }
        }
        return result
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mFloatingLayout != null) {
            // 移除悬浮窗口
            mWindowManager!!.removeView(mFloatingLayout)
        }
    }

    /**
     * 设置悬浮框基本参数（位置、宽高等）
     */
    private fun initWindow() {
        mWindowManager = applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wmParams = params //设置好悬浮窗的参数

        wmParams?.run {
            // 悬浮窗默认显示以左上角为起始坐标
            gravity = Gravity.LEFT or Gravity.TOP
            //悬浮窗的开始位置，因为设置的是从左上角开始，所以屏幕左上角是x=0;y=0
            val point = Point()
            mWindowManager?.defaultDisplay?.getSize(point)
            if (point.x > 0) {
                x = point.x - UIUtils.dp2px(120)
            } else {
                x = 180
            }

            if (point.y > 0) {
                y = point.y - UIUtils.dp2px(240)
            } else {
                y = 210
            }

        }

        //得到容器，通过这个inflater来获得悬浮窗控件
        inflater = LayoutInflater.from(applicationContext)
        // 获取浮动窗口视图所在布局
        mFloatingLayout = inflater?.inflate(R.layout.alert_float_window_layout, null)
        // 添加悬浮窗的视图
        mWindowManager?.addView(mFloatingLayout, wmParams)
        mFloatingLayout?.findViewById<View>(R.id.float_load_btn)?.setOnClickListener {
            mContainerLayout?.layoutParams?.run {
                width = UIUtils.dp2px(200)
                height = UIUtils.dp2px(100)
            }
            mFloatingLayout?.requestLayout()
            Log.i(TAG, "")
        }
    }

    //设置window type 下面变量2002是在屏幕区域显示，2003则可以显示在状态栏之上
    //设置可以显示在状态栏上

    //设置悬浮窗口长宽数据
    private val params: WindowManager.LayoutParams
        private get() {
            wmParams = WindowManager.LayoutParams()

            wmParams?.run {
                //设置window type 下面变量2002是在屏幕区域显示，2003则可以显示在状态栏之上
                type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                //设置可以显示在状态栏上
                flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR or
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH

                //设置悬浮窗口长宽数据
                width = WindowManager.LayoutParams.WRAP_CONTENT
                height = WindowManager.LayoutParams.WRAP_CONTENT
            }

            return wmParams!!
        }

    private fun initFloating() {
        mContainerLayout = mFloatingLayout?.findViewById(R.id.float_container)
        smallSizePreviewLayout = mFloatingLayout?.findViewById(R.id.small_size_preview)

        //悬浮框点击事件
        smallSizePreviewLayout?.setOnClickListener(View.OnClickListener { //在这里实现点击重新回到Activity
            val intent = Intent()
            //                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setClass(applicationContext, FloatActivity::class.java)
            startActivity(intent)
        })

        //悬浮框触摸事件，设置悬浮框可拖动
        smallSizePreviewLayout?.setOnTouchListener(FloatingListener())
    }

    //开始触控的坐标，移动时的坐标（相对于屏幕左上角的坐标）
    private var mTouchStartX = 0
    private var mTouchStartY = 0
    private var mTouchCurrentX = 0
    private var mTouchCurrentY = 0

    //开始时的坐标和结束时的坐标（相对于自身控件的坐标）
    private var mStartX = 0
    private var mStartY = 0
    private var mStopX = 0
    private var mStopY = 0

    //判断悬浮窗口是否移动，这里做个标记，防止移动后松手触发了点击事件
    private var isMove = false
    private var tempY = 0

    private inner class FloatingListener : OnTouchListener {
        override fun onTouch(v: View, event: MotionEvent): Boolean {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    isMove = false
                    mTouchStartX = event.rawX.toInt()
                    mTouchStartY = event.rawY.toInt()
                    mStartX = mTouchStartX
                    mStartY = mTouchStartY
                }
                MotionEvent.ACTION_MOVE -> {
                    mTouchCurrentX = event.rawX.toInt()
                    mTouchCurrentY = event.rawY.toInt()
                    wmParams!!.x += mTouchCurrentX - mTouchStartX
                    //                    wmParams.y += mTouchCurrentY - mTouchStartY;
                    tempY = wmParams!!.y + mTouchCurrentY - mTouchStartY
                    if (tempY < 80) {
                        tempY = 80
                    }
                    wmParams!!.y = tempY
                    mWindowManager!!.updateViewLayout(mFloatingLayout, wmParams)
                    mTouchStartX = mTouchCurrentX
                    mTouchStartY = mTouchCurrentY
                }
                MotionEvent.ACTION_UP -> {
                    mStopX = event.rawX.toInt()
                    mStopY = event.rawY.toInt()
                    if (Math.abs(mStartX - mStopX) >= 2 || Math.abs(mStartY - mStopY) >= 2) {
                        isMove = true
                    }
                    if (isMove) {
                        slideToBorder()
                    }
                }
            }

            //如果是移动事件不触发OnClick事件，防止移动的时候一放手形成点击事件
            return isMove
        }
    }

    private fun slideToBorder() {
        val point = Point()
        mWindowManager?.defaultDisplay?.getSize(point)
        var width = mFloatingLayout?.width ?: 0
        var x = wmParams?.x ?: 0
        Log.i(TAG, "width:"+width+", pWidth:"+point.x+", pHeight:"+point.y+", wmParams.x:"+wmParams?.x+", wmParams.y:"+wmParams?.y)

        if (width > 0 && point.x > 0) {
            if (x > 0 && x < (point.x/2 - width/2)) {
                wmParams?.x = 0
                mWindowManager?.updateViewLayout(mFloatingLayout, wmParams)
            } else if (x > (point.x/2 - width/2) && x < point.x - width) {
                wmParams?.x = point.x - width
                mWindowManager?.updateViewLayout(mFloatingLayout, wmParams)
            }
        }
    }

    companion object {
        private const val TAG = "float-window"
    }
}