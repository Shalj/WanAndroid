package com.shalj.wanandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.shalj.wanandroid.route.AppRoute
import com.shalj.wanandroid.ui.screen.article.ArticleDetailScreen
import com.shalj.wanandroid.ui.screen.main.MainScreen
import com.shalj.wanandroid.ui.screen.start.StartScreen
import com.shalj.wanandroid.ui.theme.WanAndroidTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WanAndroidTheme {
                val appNavController = rememberNavController()
                NavHost(navController = appNavController, startDestination = AppRoute.mainScreen) {
                    composable(AppRoute.startScreen) {
                        StartScreen(appNavController = appNavController)
                    }
                    composable(AppRoute.mainScreen) {
                        MainScreen(navController = appNavController)
                    }
                    composable(
                        AppRoute.articleDetail,
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
                        ArticleDetailScreen(appNavController, link, title)
                    }
                }
            }
        }
    }
}