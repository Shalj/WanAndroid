package com.shalj.wanandroid.net

sealed class RequestResult<out T> {
    data class Success<out T>(val data: T) : RequestResult<T>()
    data class Error(val msg: String) : RequestResult<Nothing>()
}