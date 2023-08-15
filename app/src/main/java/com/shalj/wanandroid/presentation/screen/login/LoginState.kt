package com.shalj.wanandroid.presentation.screen.login

data class LoginState(
    var username: String = "",
    var password: String = "",
    var loginSuccess: Boolean = false,
    val showLoading: Boolean = false,
    val snackMessage: String = "",
) {
    val loginBtnEnabled: Boolean get() = username.isNotEmpty() && password.isNotEmpty()
}

sealed class LoginEvent {
    object Login : LoginEvent()
    object DismissLoading : LoginEvent()
    data class InputUsername(val username: String) : LoginEvent()
    data class InputPassword(val password: String) : LoginEvent()
}
