package com.tomsky.androiddemo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.tomsky.androiddemo.databinding.FragmentShareModelBinding
import com.tomsky.androiddemo.viewmodel.ShareViewModel

/**
 *
 * Created by wangzhitao on 2022/08/08
 *
 **/
class ShareModelFragment:Fragment() {

    private val model: ShareViewModel by activityViewModels<ShareViewModel>()

    private var binding: FragmentShareModelBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShareModelBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.btnUpdate?.setOnClickListener {
            var value = model.selectValue.value ?: 0
            model.selectValue.value = value + 1
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}