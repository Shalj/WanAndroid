package com.shalj.wanandroid.ui.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.pullrefresh.PullRefreshIndicator
import androidx.compose.material3.pullrefresh.PullRefreshState
import androidx.compose.material3.pullrefresh.pullRefresh
import androidx.compose.material3.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.shalj.wanandroid.model.ArticleData
import com.shalj.wanandroid.model.BannerData
import com.shalj.wanandroid.route.AppRoute
import com.shalj.wanandroid.ui.components.WanSnackBar
import com.shalj.wanandroid.ui.components.banner.Banner
import com.shalj.wanandroid.ui.components.multistatewidget.MultiStateWidget
import com.shalj.wanandroid.ui.screen.article.ArticleItem
import com.shalj.wanandroid.ui.screen.start.setupLifeCycle
import com.shalj.wanandroid.ui.theme.WanAndroidTheme
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavController, viewModel: HomeViewModel = hiltViewModel()
) {
    setupLifeCycle(lifecycleOwner = LocalLifecycleOwner.current, viewModel)

    //ui状态？
    val uiState by viewModel.uiState.collectAsStateWithLifecycle(HomeState())
    val articles = viewModel.articles.collectAsLazyPagingItems()

    //刷新状态
    val refreshState = rememberPullRefreshState(
        refreshing = articles.loadState.refresh == LoadState.Loading,
        onRefresh = { articles.refresh() },
    )
    val snackbarHostState = remember { SnackbarHostState() }
    val lazyListState = rememberLazyListState()

    Scaffold(
        contentColor = MaterialTheme.colorScheme.background,
        snackbarHost = {
            WanSnackBar(
                modifier = Modifier
                    .padding(bottom = 40.dp)
                    .padding(horizontal = 32.dp),
                snackbarHostState = snackbarHostState,
                action = {
                    TextButton(onClick = { snackbarHostState.currentSnackbarData?.dismiss() }) {
                        Text(text = "关闭")
                    }
                },
            )
        },
        floatingActionButton = { BackToTopBtn(lazyListState) },
        topBar = { MyTopBar() },
        content = { Articles(it, uiState, navController, refreshState, articles, lazyListState) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar() {
    TopAppBar(
        title = { },
        colors = topAppBarColors(),
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
                    imageVector = Icons.Outlined.Menu,
                    contentDescription = "iconMenu",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "iconSearch",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
                    imageVector = Icons.Outlined.MailOutline,
                    contentDescription = "iconMessage",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    )
}

@Composable
fun BackToTopBtn(lazyListState: LazyListState = rememberLazyListState()) {
    val scope = rememberCoroutineScope()
    //是否展示返回顶部按钮
    val showBackToTop by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex > 3
        }
    }

    AnimatedVisibility(visible = showBackToTop, enter = scaleIn(), exit = scaleOut()) {
        FloatingActionButton(
            containerColor = MaterialTheme.colorScheme.secondary,
            onClick = {
                scope.launch {
                    lazyListState.animateScrollToItem(0)
                }
            },
            content = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowUp,
                        contentDescription = "backToTop"
                    )
                    Text(
                        text = "返回顶部",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
        )
    }
}

fun LazyListScope.banner(bannerData: List<BannerData>) {
    item {
        //banner
        Banner(count = bannerData.size) { index ->
            Surface(
                modifier = Modifier
                    .height(220.dp)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 10.dp),
                shape = RoundedCornerShape(10.dp),
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = bannerData[index].imagePath ?: "",
                        contentDescription = "banner-$index",
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

@Composable
fun Articles(
    paddingValues: PaddingValues,
    uiState: HomeState = HomeState(),
    navController: NavController,
    refreshState: PullRefreshState,
    pageData: LazyPagingItems<ArticleData>,
    lazyListState: LazyListState
) {
    MultiStateWidget(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        state = uiState.multiState,
    ) {
        Box(modifier = Modifier.pullRefresh(refreshState)) {

            LazyColumn(
                state = lazyListState,
                contentPadding = PaddingValues(bottom = 20.dp),
            ) {
                //banner
                banner(bannerData = uiState.banner)

                //articles
                items(
                    count = pageData.itemCount,
                    key = { index -> pageData[index]?.id ?: 0 },
                ) { index ->
                    pageData[index]?.let { data ->
                        ArticleItem(data) { link ->
                            navController.navigate(
                                AppRoute.articleDetail
                                    .replace("{link}", link)
                                    .replace("{title}", data.title.orEmpty())
                            )
                        }
                    }
                }
            }

            PullRefreshIndicator(
                refreshing = refreshState.refreshing,
                state = refreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArticlesPreview() {
    WanAndroidTheme {
        Scaffold(
            floatingActionButton = { BackToTopBtn() },
            topBar = { MyTopBar() },
            content = {
                LazyColumn(
                    modifier = Modifier.padding(it),
                    contentPadding = PaddingValues(vertical = 20.dp),
                ) {
                    items(count = 5) {
                        ArticleItem {}
                    }
                }
            }
        )
    }
}