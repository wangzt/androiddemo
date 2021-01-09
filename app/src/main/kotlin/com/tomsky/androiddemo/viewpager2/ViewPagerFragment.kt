package com.tomsky.androiddemo.viewpager2

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.tomsky.androiddemo.databinding.Viewpager2ItemBinding
import com.tomsky.androiddemo.util.ThreadUtils

/**
 *
 * Created by wangzhitao on 2021/01/09
 *
 **/
class ViewPagerFragment: Fragment() {

    private var index = 0

    private lateinit var binding: Viewpager2ItemBinding

    private lateinit var viewModel: ViewPagerFragViewModel

    companion object {

        private const val TAG = "viewpager-frag"

        private const val ARG_INDEX = "arg_index"

        fun newInstance(index: Int): ViewPagerFragment {
            val bundle = Bundle()
            bundle.putInt(ARG_INDEX, index)
            val fragment = ViewPagerFragment()
            fragment.arguments = bundle
            return fragment
        }

        val colors = arrayListOf<Int>(
                Color.parseColor("#FDF6E5"),
                Color.parseColor("#E8F9FE"),
                Color.parseColor("#F0ECFC"),
                Color.parseColor("#FFEDED"),
                Color.parseColor("#FCEFFD"),
                Color.parseColor("#EBF2FD"),
                Color.parseColor("#FFF4F4"),
                Color.parseColor("#FEF6E8")
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        index = arguments?.getInt(ARG_INDEX, -1) ?: -2
        viewModel = ViewModelProviders.of(this).get(ViewPagerFragViewModel::class.java)
        Log.i(TAG, "onCreate, index:$index")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.i(TAG, "onCreateView, index:$index")
        binding = Viewpager2ItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.i(TAG, "onViewCreated, index:$index")
        super.onViewCreated(view, savedInstanceState)
        viewModel.indexBean.observe(this, Observer {
            binding.viewpager2ItemText.text = it.toString()
        })
        binding.root.setBackgroundColor(colors[index%8])
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart, index:$index")
    }
    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume, index:$index")
        ThreadUtils.postDelayed({
            viewModel.indexBean.value = index
        }, 500)

    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause, index:$index")
        viewModel.indexBean.value = -1
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop, index:$index")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i(TAG, "onAttach, index:$index")
    }

    override fun onDetach() {
        super.onDetach()
        Log.i(TAG, "onDetach, index:$index")
    }
}

class ViewPagerFragViewModel : ViewModel() {
    val indexBean = MutableLiveData<Int>()
}