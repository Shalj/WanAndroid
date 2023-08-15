package com.shalj.wanandroid.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {
    protected val _showLoading = MutableStateFlow(false)
    protected val _snackMessage = MutableStateFlow("")

    open fun onStart() {}
    open fun onStop() {}

}