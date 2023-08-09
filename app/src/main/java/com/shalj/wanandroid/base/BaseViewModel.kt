package com.shalj.wanandroid.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    private val _uiIntentFlow: Channel<IUiIntent> = Channel()
    private val uiIntentFlow = _uiIntentFlow.receiveAsFlow()

    init {
        viewModelScope.launch {
            uiIntentFlow.collect {
                handleIntent(it)
            }
        }
    }

    open fun onStart() {}
    open fun onStop() {}

    fun sendUiIntent(uiIntent: IUiIntent) {
        viewModelScope.launch {
            _uiIntentFlow.send(uiIntent)
        }
    }

    protected open fun handleIntent(uiIntent: IUiIntent) {

    }
}