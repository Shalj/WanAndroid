package com.shalj.wanandroid.ui.screen.main

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.shalj.wanandroid.route.LoginRoute
import com.shalj.wanandroid.route.Route
import com.shalj.wanandroid.ui.screen.article.ArticleDetailScreen
import com.shalj.wanandroid.ui.screen.main.me.MeScreen
import com.shalj.wanandroid.ui.screen.start.StartScreen
import com.shalj.wanandroid.ui.theme.WanAndroidTheme

@Composable
fun App() {
    WanAndroidTheme {
        val navController = rememberNavController()
        val duration = 350

        NavHost(
            navController = navController,
            startDestination = Route.startScreen,
            enterTransition = {
                when (targetState.destination.route) {
                    in Route.verticalValues() -> slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        animationSpec = tween(duration)
                    )

                    in Route.horizontalValues() -> slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(duration)
                    )

                    else -> EnterTransition.None
                }
            },
            exitTransition = { fadeOut(animationSpec = tween(duration)) },
            popEnterTransition = { fadeIn(animationSpec = tween(duration)) },
            popExitTransition = {
                when (initialState.destination.route) {
                    in Route.verticalValues() -> slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        animationSpec = tween(duration)
                    )

                    in Route.horizontalValues() -> slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(duration)
                    )

                    else -> ExitTransition.None
                }
            }
        ) {
            composable(Route.startScreen) {
                StartScreen {
                    val options = NavOptions.Builder().setLaunchSingleTop(true).build()
                    navController.navigate(Route.mainScreen, options)
                }
            }
            loginGraph(navController)
            composable(Route.mainScreen) {
                MainScreen(
                    goSearch = {},
                    goMe = { navController.navigate(Route.meScreen) },
                    goArticleDetailScreen = { link, title ->
                        navController.navigate(
                            Route.articleDetail.replace("{link}", link)
                                .replace("{title}", title)
                        )
                    }
                )
            }
            composable(
                Route.articleDetail, arguments = listOf(
                    navArgument("link") {
                        type = NavType.StringType
                        defaultValue = ""
                    },
                    navArgument("title") {
                        type = NavType.StringType
                        defaultValue = ""
                    },
                )
            ) {
                val link = it.arguments?.getString("link") ?: ""
                val title = it.arguments?.getString("title") ?: ""
                ArticleDetailScreen(link, title) {
                    navController.navigateUp()
                }
            }
            composable(Route.meScreen) {
                MeScreen(
                    login = {},
                    goMessage = {},
                    goCollect = {},
                    goShare = {},
                    goSetting = {},
                    goTools = {},
                    onBackPressed = navController::navigateUp
                )
            }
        }
    }
}

private fun NavGraphBuilder.loginGraph(navController: NavController) {
    navigation(startDestination = LoginRoute.login, route = LoginRoute.root) {
        composable(LoginRoute.login) {

        }
        composable(LoginRoute.register) {

        }
    }
}