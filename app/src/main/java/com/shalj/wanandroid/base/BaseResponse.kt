package com.shalj.wanandroid.base

data class BaseResponse<T>(
    val errorCode: Int = 0,
    val errorMsg: String = "",
    val data: T,
)

data class PageResponse<T>(
    val curPage: Int = 0,
    val datas: List<T> = listOf(),
    val offset: Int = 0,
    val over: Boolean = false,
    val pageCount: Int = 0,
    val size: Int = 0,
    val total: Int = 0
)