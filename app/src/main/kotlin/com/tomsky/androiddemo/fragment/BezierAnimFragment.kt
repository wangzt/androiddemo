package com.tomsky.androiddemo.fragment

import android.animation.FloatArrayEvaluator
import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import com.tomsky.androiddemo.databinding.FragmentBezierAnimBinding
import com.tomsky.androiddemo.util.UIUtils

/**
 *
 * Created by wangzhitao on 2022/08/11
 *
 **/
class BezierAnimFragment:Fragment() {

    private var binding: FragmentBezierAnimBinding? = null

    private val xStart = UIUtils.dp2px(100).toFloat()
    private val yStart = UIUtils.dp2px(100).toFloat()

    private val xEnd = UIUtils.dp2px(120).toFloat()
    private val yEnd = UIUtils.dp2px(90).toFloat()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBezierAnimBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.btnAnim?.setOnClickListener {
            doAnim()
        }
    }

    private fun doAnim() {
        binding?.bezierIcon?.run {
            x = xStart
            y = yStart
            alpha = 1.0f
        }
        val startParams = floatArrayOf(xStart, yStart, 1f)
        val endParams = floatArrayOf(xEnd, yEnd, 0f)
        val control = floatArrayOf(UIUtils.dp2px(90).toFloat(), UIUtils.dp2px(90).toFloat())
        val control2 = floatArrayOf(UIUtils.dp2px(100).toFloat(), UIUtils.dp2px(80).toFloat())
        val animAvatar = ValueAnimator.ofObject(Bezier3Evaluator(control, control2), startParams, endParams)
        animAvatar.duration = 3000
//        animAvatar.interpolator = DecelerateInterpolator()
        animAvatar.interpolator = AccelerateInterpolator()
        animAvatar.addUpdateListener { animation->
            val value = animation.animatedValue as FloatArray
            binding?.bezierIcon?.let {
                it.x = value[0]
                it.y = value[1]
                it.alpha = value[2]
            }
        }
        animAvatar.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}

class Bezier2Evaluator(val control: FloatArray): TypeEvaluator<FloatArray> {

    override fun evaluate(
        fraction: Float,
        startValue: FloatArray,
        endValue: FloatArray
    ): FloatArray {
        var f = 1 - fraction
        var x = f*f*startValue[0] + 2*fraction*f*control[0] + fraction*fraction * endValue[0]
        var y = f*f*startValue[1] + 2*fraction*f*control[1] + fraction*fraction * endValue[1]
        var alpha =
        return floatArrayOf(x, y)
    }

}

class Bezier3Evaluator(val control: FloatArray, val control2: FloatArray): TypeEvaluator<FloatArray> {

    override fun evaluate(
        fraction: Float,
        startValue: FloatArray,
        endValue: FloatArray
    ): FloatArray {
        var f = 1 - fraction
        var x = f*f*f*startValue[0] + 3*fraction*f*f*control[0] + 3*fraction*fraction*f*control2[0] + fraction * fraction*fraction * endValue[0]
        var y = f*f*f*startValue[1] + 3*fraction*f*f*control[1] + 3*fraction*fraction*f*control2[1] + fraction * fraction*fraction * endValue[1]
        var z = startValue[2] + fraction * (endValue[2] - startValue[2])
        return floatArrayOf(x, y, z)
    }

}