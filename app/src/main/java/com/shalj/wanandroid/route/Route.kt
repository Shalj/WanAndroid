package com.shalj.wanandroid.route

object Route {
    const val startScreen = "/startScreen"
    const val mainScreen = "/mainScreen"
    const val articleDetail = "/articleDetail?id={id}"

    const val meScreen = "/meScreen"

}

val horizontalValues =
    listOf(LoginRoute.register, Route.startScreen, Route.mainScreen, Route.articleDetail)
val verticalValues = listOf(LoginRoute.login, Route.meScreen)

object LoginRoute {
    const val root = "/login"
    const val login = "$root/login"
    const val register = "$root/register"
}