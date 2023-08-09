package com.shalj.wanandroid.net

import com.shalj.wanandroid.base.BaseResponse
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

//通用的异常处理
inline fun <reified T> withRequestResult(io: () -> RequestResult<T>): RequestResult<T> =
    try {
        io()
    } catch (e: Exception) {
        e.printStackTrace()
        RequestResult.Error("请求失败")
    }

//通用的返回错误码处理处理
fun <T> BaseResponse<T>.handleResponse(): RequestResult<T> {
    return when (errorCode) {
        0 -> RequestResult.Success(data)
        -1001 -> {
            RequestResult.Error("登录信息已过期，请重新登录")
        }

        else -> RequestResult.Error(errorMsg)
    }
}

/**
 * try封装
 * */
@OptIn(ExperimentalContracts::class)
inline fun <T, R> T.inTry(
    isPrintStackTrace: Boolean = true,
    catch: (e: Exception) -> Unit = {},
    block: T.() -> R?
): R? {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return try {
        block()
    } catch (e: Exception) {
        if (isPrintStackTrace) e.printStackTrace()
        catch(e)
        null
    }
}

///**
// * Retrofit网络返回处理
// */
//suspend fun <T> Call<BaseResponse<T>>.go(): RequestResult<T> = suspendCoroutine {
//    enqueue(object : Callback<BaseResponse<T>> {
//        override fun onResponse(call: Call<BaseResponse<T>>, response: Response<BaseResponse<T>>) {
//            val body = response.body()
//            if (body != null) it.resume(body.handleResponse())
//            else it.resumeWithException(RuntimeException("response body is null"))
//        }
//
//        override fun onFailure(call: Call<BaseResponse<T>>, t: Throwable) {
//            it.resumeWithException(t)
//        }
//    })
//}