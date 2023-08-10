package com.shalj.wanandroid.ui.screen.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.shalj.wanandroid.route.Route
import com.shalj.wanandroid.ui.screen.article.ArticleDetailScreen
import com.shalj.wanandroid.ui.screen.start.StartScreen
import com.shalj.wanandroid.ui.theme.WanAndroidTheme

@Composable
fun App(){
    WanAndroidTheme {
        val appNavController = rememberNavController()
        NavHost(navController = appNavController, startDestination = Route.mainScreen) {
            composable(Route.startScreen) {
                StartScreen {
                    val options = NavOptions.Builder()
                        .setLaunchSingleTop(true)
                        .build()
                    appNavController.navigate(Route.mainScreen, options)
                }
            }
            composable(Route.mainScreen) {
                MainScreen { link, title ->
                    appNavController.navigate(
                        Route.articleDetail
                            .replace("{link}", link)
                            .replace("{title}", title)
                    )
                }
            }
            composable(
                Route.articleDetail,
                arguments = listOf(
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
                    appNavController.navigateUp()
                }
            }
        }
    }
}