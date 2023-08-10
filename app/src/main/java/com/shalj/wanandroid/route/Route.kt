package com.shalj.wanandroid.route

object Route {
    const val startScreen = "/startScreen"
    const val mainScreen = "/mainScreen"
    const val articleDetail = "/articleDetail?link={link}&title={title}"
    const val meScreen = "/meScreen"

    fun horizontalValues() = listOf(startScreen, mainScreen, articleDetail)
    fun verticalValues() = listOf(meScreen)
}

object LoginRoute {
    const val root = "/login"
    const val login = "$root/login"
    const val register = "$root/register"
}