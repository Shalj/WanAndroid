package com.shalj.wanandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
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
                    animateComposable(AppRoute.startScreen) {
                        StartScreen(appNavController = appNavController)
                    }
                    animateComposable(AppRoute.mainScreen) {
                        MainScreen(navController = appNavController)
                    }
                    animateComposable(
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

fun NavGraphBuilder.animateComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable (NavBackStackEntry) -> Unit
) {
    composable(route, arguments, deepLinks, content = {
        AnimationContent(route == it.destination.route) {
            content(it)
        }
    })
}

@Composable
fun AnimationContent(visible: Boolean, content: @Composable AnimatedVisibilityScope.() -> Unit) {
    val visible by remember {
        mutableStateOf(visible)
    }
    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(),
        exit = slideOutHorizontally(),
        content = content
    )
}