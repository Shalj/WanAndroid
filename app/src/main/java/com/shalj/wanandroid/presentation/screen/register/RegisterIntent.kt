package com.shalj.wanandroid.presentation.screen.register

import androidx.lifecycle.viewModelScope
import com.shalj.wanandroid.base.BaseViewModel
import com.shalj.wanandroid.net.Api
import com.shalj.wanandroid.net.RequestResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RegisterIntent @Inject constructor(
    private val api: Api
) : BaseViewModel() {
    private val _username = MutableStateFlow("")
    private val _password = MutableStateFlow("")
    private val _rePassword = MutableStateFlow("")
    private val _verifyCode = MutableStateFlow("")

    val state = combine<Any, RegisterState>(
        _username,
        _password,
        _rePassword,
        _verifyCode,
        _showLoading,
        _snackMessage,
    ) { flows ->
        RegisterState().fromArray(flows)
    }.shareIn(viewModelScope, SharingStarted.WhileSubscribed())

    fun onEvent(event: RegisterEvent, closePage: () -> Unit = {}) {
        when (event) {
            is RegisterEvent.InputUsername -> _username.value = event.username
            is RegisterEvent.InputPassword -> _password.value = event.password
            is RegisterEvent.InputRePassword -> _rePassword.value = event.rePassword
            is RegisterEvent.InputVerifyCode -> _verifyCode.value = event.verifyCode
            RegisterEvent.DismissLoading -> _showLoading.value = false
            RegisterEvent.Register -> register(closePage)
        }
    }

    private fun register(closePage: () -> Unit) {
        _showLoading.value = true
        viewModelScope.launch {
            val result =
                api.register(_username.value, _password.value, _rePassword.value, _verifyCode.value)
            _showLoading.value = false
            when (result) {
                is RequestResult.Error -> withContext(Dispatchers.Main) {
                    _snackMessage.value = result.msg
                }

                is RequestResult.Success -> withContext(Dispatchers.Main) {
                    _snackMessage.value = "注册成功"
                    closePage()
                }
            }
        }
    }
}