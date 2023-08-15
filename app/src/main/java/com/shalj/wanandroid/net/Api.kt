package com.shalj.wanandroid.net

import okhttp3.MultipartBody

object Api {
    private val service = RetrofitHelper.create<ApiService>()

    //获取首页文章列表
    suspend fun getHomeArticleList(page: Int) = withRequestResult {
        service.getHomeArticleList(page).handleResponse()
    }

    //获取首页banner信息
    suspend fun getBannerData() = withRequestResult {
        service.getBannerData().handleResponse()
    }

    suspend fun login(username: String, password: String) = withRequestResult {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("username", username)
            .addFormDataPart("password", password)
            .build()
        service.login(requestBody).handleResponse()
    }

    suspend fun register(
        username: String,
        password: String,
        rePassword: String,
        verifyCode: String
    ) =
        withRequestResult {
            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("username", username)
                .addFormDataPart("password", password)
                .addFormDataPart("rePassword", rePassword)
                .addFormDataPart("code", verifyCode)
                .build()
            service.register(requestBody).handleResponse()
        }
}