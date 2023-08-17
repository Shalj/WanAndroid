package com.shalj.wanandroid.presentation.screen.main

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import com.shalj.wanandroid.presentation.screen.article.ArticleDetailScreen
import com.shalj.wanandroid.presentation.screen.login.LoginScreen
import com.shalj.wanandroid.presentation.screen.main.me.MeScreen
import com.shalj.wanandroid.presentation.screen.register.RegisterScreen
import com.shalj.wanandroid.presentation.screen.start.StartScreen
import com.shalj.wanandroid.route.LoginRoute
import com.shalj.wanandroid.route.Route
import com.shalj.wanandroid.route.horizontalValues
import com.shalj.wanandroid.route.verticalValues
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
                    Route.mainScreen -> scaleIn(initialScale = .5f) + fadeIn()

                    in verticalValues -> slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        animationSpec = tween(duration)
                    )

                    in horizontalValues -> slideIntoContainer(
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
                    Route.startScreen -> fadeOut() + scaleOut(targetScale = .5f)
                    in verticalValues -> slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        animationSpec = tween(duration)
                    )

                    in horizontalValues -> slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(duration)
                    )

                    else -> ExitTransition.None
                }
            }
        ) {
            composable(Route.startScreen) {
                StartScreen {
                    navController.popBackStack()
                    val options = NavOptions.Builder().setLaunchSingleTop(true).build()
                    navController.navigate(Route.mainScreen, options)
                }
            }
            loginGraph(navController)
            composable(Route.mainScreen) {
                MainScreen(
                    login = { navController.navigate(LoginRoute.login) },
                    navigateToSearch = {},
                    navigateToMe = { navController.navigate(Route.meScreen) },
                    navigateToArticleDetailScreen = { article ->
                        navController.navigate(
                            Route.articleDetail.replace("{id}", article.id.toString())
                        )
                    }
                )
            }
            composable(
                Route.articleDetail, arguments = listOf(
                    navArgument("id") {
                        type = NavType.IntType
                        defaultValue = -1
                    },
                )
            ) {
                val id = it.arguments?.getInt("id") ?: -1
                ArticleDetailScreen(
                    id,
                    login = { navController.navigate(LoginRoute.login) },
                    navigateUp = navController::navigateUp
                )
            }
            composable(Route.meScreen) {
                MeScreen(
                    login = {
                        navController.navigate(LoginRoute.login)
                    },
                    navigateToMessage = {},
                    navigateToCollect = {},
                    navigateToShare = {},
                    navigateToSetting = {},
                    navigateToTools = {},
                    onBackPressed = navController::navigateUp
                )
            }
        }
    }
}

private fun NavGraphBuilder.loginGraph(navController: NavController) {
    navigation(startDestination = LoginRoute.login, route = LoginRoute.root) {
        composable(LoginRoute.login) {
            LoginScreen(
                register = { navController.navigate(LoginRoute.register) },
                onBackPressed = navController::navigateUp
            )
        }
        composable(LoginRoute.register) {
            RegisterScreen(onBackPressed = navController::navigateUp)
        }
    }
}