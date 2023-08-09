package com.shalj.wanandroid.net

import com.shalj.wanandroid.utils.logE
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

sealed class RequestResult<out T> {
    data class Success<out T>(val data: T) : RequestResult<T>()
    data class Error(val msg: String) : RequestResult<Nothing>()
}


object RetrofitHelper {
    private const val baseUrl = "https://www.wanandroid.com"

    private fun getRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(OkHttpClient.Builder().addInterceptor(loggingInterceptor).build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun <T> create(serviceClass: Class<T>): T = getRetrofit().create(serviceClass)

    inline fun <reified T> create(): T = create(T::class.java)

    private val loggingInterceptor: HttpLoggingInterceptor by lazy {
        HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                logE("RetrofitHelper", "message:$message")
            }
        }).apply { level = HttpLoggingInterceptor.Level.BODY }
    }
}