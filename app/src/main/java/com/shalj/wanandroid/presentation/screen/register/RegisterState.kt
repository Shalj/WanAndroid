package com.shalj.wanandroid.presentation.screen.register

data class RegisterState(
    var username: String = "",
    var password: String = "",
    var rePassword: String = "",
    var verifyCode: String = "",
    var showLoading: Boolean = true,
    var errorMsg: String = "",
) {
    val registerBtnEnabled: Boolean get() = username.isNotEmpty() && password.isNotEmpty() && password == rePassword
}

fun RegisterState.fromArray(values:Array<Any>):RegisterState{
    if (values.size < 6) return this
    username = values[0] as String
    password = values[1] as String
    rePassword = values[2] as String
    verifyCode = values[3] as String
    showLoading = values[4] as Boolean
    errorMsg = values[5] as String
    return this
}

sealed class RegisterEvent {
    object Register : RegisterEvent()
    object DismissLoading : RegisterEvent()
    data class InputUsername(val username: String) : RegisterEvent()
    data class InputPassword(val password: String) : RegisterEvent()
    data class InputRePassword(val rePassword: String) : RegisterEvent()
    data class InputVerifyCode(val verifyCode: String) : RegisterEvent()
}
