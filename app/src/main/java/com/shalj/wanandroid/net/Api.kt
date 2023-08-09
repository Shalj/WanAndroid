package com.shalj.wanandroid.net

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
}