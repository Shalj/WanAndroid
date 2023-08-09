package com.shalj.wanandroid.net

import com.shalj.wanandroid.base.BaseResponse
import com.shalj.wanandroid.base.PageResponse
import com.shalj.wanandroid.model.ArticleData
import com.shalj.wanandroid.model.BannerData
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("/article/list/{page}/json")
    suspend fun getHomeArticleList(
        @Path("page") page: Int,
        @Query("page_size") pageSize: Int = 20
    ): BaseResponse<PageResponse<ArticleData>>

    @GET("/banner/json")
    suspend fun getBannerData(): BaseResponse<List<BannerData>>
}