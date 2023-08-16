package com.shalj.wanandroid.net

import com.shalj.wanandroid.base.BaseResponse
import com.shalj.wanandroid.base.PageResponse
import com.shalj.wanandroid.domain.ArticleData
import com.shalj.wanandroid.domain.BannerData
import com.shalj.wanandroid.domain.LoginResp
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("/article/list/{page}/json")
    suspend fun getHomeArticleList(
        @Path("page") page: Int,
        @Query("page_size") pageSize: Int = 20
    ): BaseResponse<PageResponse<ArticleData>>

    @POST("/lg/collect/{id}/json")
    suspend fun collectArticle(@Path("id") id: Int): BaseResponse<Any>

    @POST("/lg/uncollect_originId/{id}/json")
    suspend fun unCollectArticle(@Path("id") id: Int): BaseResponse<Any>

    @GET("/banner/json")
    suspend fun getBannerData(): BaseResponse<List<BannerData>>

    @POST("/user/login")
    suspend fun login(@Body body: RequestBody): BaseResponse<LoginResp>

    @POST("/user/register")
    suspend fun register(@Body body: RequestBody): BaseResponse<Any>
}