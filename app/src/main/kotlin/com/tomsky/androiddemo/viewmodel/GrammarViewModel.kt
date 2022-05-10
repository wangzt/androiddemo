package com.tomsky.androiddemo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class GrammarViewModel:ViewModel() {

    val sharedFlow = MutableSharedFlow<String>(2)

    val stateFlow = MutableStateFlow("State")

    fun emitShareFlow() {
        viewModelScope.launch {
            sharedFlow.emit("Hello")
            sharedFlow.emit("SharedFlow")
        }
    }
}