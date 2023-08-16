package com.shalj.wanandroid.domain

data class LoginResp(
    val admin: Boolean? = false,
    val chapterTops: List<Any?>? = listOf(),
    val coinCount: Int? = 0,
    val collectIds: List<Any?>? = listOf(),
    val email: String? = "",
    val icon: String? = "",
    val id: Int? = 0,
    val nickname: String? = "",
    var password: String? = "",
    val publicName: String? = "",
    val token: String? = "",
    val type: Int? = 0,
    val username: String? = ""
)