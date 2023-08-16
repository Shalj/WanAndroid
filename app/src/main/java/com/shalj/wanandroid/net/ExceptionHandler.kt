package com.shalj.wanandroid.net

import com.google.gson.Gson
import com.shalj.wanandroid.base.BaseResponse
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException

object ExceptionHandler {

    private const val UNKNOWN_ERROR = "请求错误"
    private const val TIME_OUT = "连接服务器超时"
    private const val NOT_FOUND = "404 数据丢失，请联系管理员"
    private const val TOKEN_EXPIRED = "登录信息已过期，请重新登录"

    fun handleError(throwable: Throwable?): RequestResult<Nothing> =
        when (throwable) {
            is ConnectException -> RequestResult.Error(UNKNOWN_ERROR, throwable)

            is SocketTimeoutException -> RequestResult.Error(TIME_OUT, throwable)

            is HttpException -> when (throwable.code()) {
                401, 403 -> {
                    RequestResult.Error(TOKEN_EXPIRED, throwable)
                }

                404 -> RequestResult.Error(NOT_FOUND, throwable)

                500 -> throwable.response()?.errorBody()?.let {
                    val data = Gson().fromJson(it.string(), BaseResponse::class.java)
                    try {
                        RequestResult.Error(data?.errorMsg ?: UNKNOWN_ERROR, throwable)
                    } catch (e: Exception) {
                        RequestResult.Error(UNKNOWN_ERROR, throwable)
                    }
                } ?: RequestResult.Error(throwable.message.orEmpty().ifEmpty { UNKNOWN_ERROR }, throwable)

                else -> RequestResult.Error(throwable.message.orEmpty().ifEmpty { UNKNOWN_ERROR }, throwable)
            }

            else -> RequestResult.Error(throwable?.message.orEmpty().ifEmpty { UNKNOWN_ERROR }, throwable)
        }

}