package com.shalj.wanandroid.presentation.screen.login

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.viewModelScope
import com.shalj.wanandroid.base.BaseViewModel
import com.shalj.wanandroid.di.userInfoKey
import com.shalj.wanandroid.net.Api
import com.shalj.wanandroid.net.RequestResult
import com.shalj.wanandroid.net.toJson
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
class LoginVM @Inject constructor(
    private val userDataStore: DataStore<Preferences>,
    private val api: Api,
) : BaseViewModel() {
    private val _username = MutableStateFlow("")
    private val _password = MutableStateFlow("")
    private val _loginSuccess = MutableStateFlow(false)

    val state = combine(
        _username,
        _password,
        _loginSuccess,
        _showLoading,
        _snackMessage
    ) { username, password, loginSuccess, showLoading, snackMessage ->
        LoginState(username, password, loginSuccess, showLoading, snackMessage)
    }.shareIn(viewModelScope, SharingStarted.WhileSubscribed())

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.InputPassword -> _password.value = event.password
            is LoginEvent.InputUsername -> _username.value = event.username
            LoginEvent.Login -> login()
            LoginEvent.DismissLoading -> _showLoading.value = false
        }
    }

    fun login() {
        _showLoading.value = true
        viewModelScope.launch {
            val result = api.login(_username.value, _password.value)
            _showLoading.value = false
            when (result) {
                is RequestResult.Error -> withContext(Dispatchers.Main) {
                    _snackMessage.value = result.msg
                }

                is RequestResult.Success -> withContext(Dispatchers.Main) {
                    //保存登录信息
                    userDataStore.edit { settings ->
                        settings[userInfoKey] =
                            result.data.apply { password = _password.value }.toJson()
                    }
                    _loginSuccess.value = true
                }
            }
        }
    }
}