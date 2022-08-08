package com.tomsky.androiddemo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 *
 * Created by wangzhitao on 2022/08/08
 *
 **/
class ShareViewModel: ViewModel() {
    val selectValue = MutableLiveData<Int>()
}