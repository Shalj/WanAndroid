package com.shalj.wanandroid.presentation.screen.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shalj.wanandroid.R
import com.shalj.wanandroid.data.local.article.ArticleEntity
import com.shalj.wanandroid.presentation.screen.main.home.HomeScreen
import com.shalj.wanandroid.presentation.screen.main.project.ProjectScreen
import com.shalj.wanandroid.presentation.screen.main.wechat.WechatAccountScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    navigateToSearch: () -> Unit = {},
    navigateToMe: () -> Unit = {},
    mainViewModel: MainViewModel = hiltViewModel(),
    navigateToArticleDetailScreen: (ArticleEntity) -> Unit
) {
    val items = listOf(
        Pair("主页", R.drawable.ic_main_home),
        Pair("项目", R.drawable.ic_main_project),
        Pair("公众号", R.drawable.ic_main_wechat),
    )
    val pagerState = rememberPagerState { items.size }
    val scope = rememberCoroutineScope()
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NavigationBar {
                items.mapIndexed { index, pair ->
                    NavigationItem(
                        currentTab = pagerState.currentPage,
                        index = index,
                        pair = pair
                    ) {
                        scope.launch {
                            pagerState.scrollToPage(index)
                        }
                    }
                }
            }
        },
        content = {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.padding(it)
            ) { index ->
                when (index) {
                    0 -> HomeScreen(
                        navigateToSearch = navigateToSearch,
                        navigateToMe = navigateToMe,
                        navigateToArticleDetailScreen = navigateToArticleDetailScreen
                    )

                    1 -> ProjectScreen()
                    2 -> WechatAccountScreen()
                }
            }
        }
    )
}

/**
 * 构建BottomNavigationItem
 *
 * @param currentTab 当前选责的item下标
 * @param index 当前item本身的下标
 * @param pair item的标题和icon存储对象
 * @param onClick item的点击事件
 * */
@Composable
fun RowScope.NavigationItem(
    currentTab: Int,
    index: Int,
    pair: Pair<String, Int>,
    onClick: (Int) -> Unit
) {
    val selectedColor = MaterialTheme.colorScheme.primary
    val defaultColor = MaterialTheme.colorScheme.secondary
    NavigationBarItem(
        selected = currentTab == index,
        onClick = { onClick(index) },
        icon = {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = pair.second),
                contentDescription = pair.first,
            )
        },
        label = { Text(text = pair.first, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold) },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = selectedColor,
            selectedTextColor = selectedColor,
            unselectedIconColor = defaultColor,
            unselectedTextColor = defaultColor,
            //注释打开可以去掉点击阴影
//            indicatorColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
//                LocalAbsoluteTonalElevation.current
//            ),
        ),
    )
}