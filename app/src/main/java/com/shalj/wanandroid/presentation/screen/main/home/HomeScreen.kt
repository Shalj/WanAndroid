package com.shalj.wanandroid.presentation.screen.main.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pullrefresh.PullRefreshIndicator
import androidx.compose.material3.pullrefresh.pullRefresh
import androidx.compose.material3.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.shalj.wanandroid.R
import com.shalj.wanandroid.data.local.article.ArticleEntity
import com.shalj.wanandroid.domain.BannerData
import com.shalj.wanandroid.presentation.components.WanTopAppBar
import com.shalj.wanandroid.presentation.components.banner.Banner
import com.shalj.wanandroid.presentation.components.multistatewidget.MultiStateWidget
import com.shalj.wanandroid.presentation.components.multistatewidget.MultiStateWidgetState
import com.shalj.wanandroid.presentation.screen.article.ArticleItem
import com.shalj.wanandroid.presentation.screen.start.SetupLifeCycle
import com.shalj.wanandroid.ui.theme.WanAndroidTheme
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    WanAndroidTheme {
        Scaffold(
            floatingActionButton = { BackToTopBtn() },
            topBar = { MyTopBar({}, {}) },
            content = {
                LazyColumn(
                    modifier = Modifier.padding(it),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(vertical = 20.dp),
                ) {
                    item(key = "banner") {
                        Banner(bannerData = listOf())
                    }
                    items(count = 5) {
                        ArticleItem(
                            ArticleEntity(
                                title = "Title of the article...",
                                author = "作者",
                                niceDate = "1天前",
                                chapterName = "chapterName",
                                superChapterName = "superChapterName",
                            )
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun HomeScreen(
    navigateToSearch: () -> Unit = {},
    navigateToMe: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToArticleDetailScreen: (ArticleEntity) -> Unit
) {
    SetupLifeCycle(lifecycleOwner = LocalLifecycleOwner.current, viewModel)

    //ui状态？
    val uiState by viewModel.uiState.collectAsStateWithLifecycle(HomeState())
    val articles = viewModel.articles.collectAsLazyPagingItems()

    //刷新状态
    val refreshState = rememberPullRefreshState(
        refreshing = articles.loadState.refresh == LoadState.Loading,
        onRefresh = { articles.refresh() },
    )
    val lazyListState = rememberLazyListState()

    Scaffold(
        contentColor = MaterialTheme.colorScheme.background,
        floatingActionButton = { BackToTopBtn(lazyListState) },
        topBar = { MyTopBar(navigateToSearch, navigateToMe) },
        content = {
            MultiStateWidget(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                providerState = {
                    if (refreshState.refreshing || it != MultiStateWidgetState.Content) MultiStateWidgetState.Loading else MultiStateWidgetState.Content
                },
            ) {
                Box(modifier = Modifier.pullRefresh(refreshState)) {
                    val providerPageData = fun(): LazyPagingItems<ArticleEntity> { return articles }
                    val pageData = providerPageData()
                    LazyColumn(
                        state = lazyListState,
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 20.dp),
                    ) {
                        //banner
                        item(key = "banner") {
                            Banner(bannerData = uiState.banner)
                        }

                        //articles
                        items(
                            count = pageData.itemCount,
                            key = { index -> index },
                        ) { index ->
                            pageData[index]?.let { data ->
                                ArticleItem(data,
                                    onCollectClick = { article ->
                                        viewModel.onEvent(HomeEvent.CollectArticle(article))
                                    },
                                    onClick = { article ->
                                        viewModel.onEvent(HomeEvent.CollectArticle(article))
                                        navigateToArticleDetailScreen(article)
                                    }
                                )
                            }
                        }
                        item {
                            if (pageData.loadState.append is LoadState.Loading) {
                                CircularProgressIndicator()
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
    )
}

@Composable
fun MyTopBar(
    navigateToSearch: () -> Unit,
    navigateToMe: () -> Unit,
) {
    WanTopAppBar(
        navigationIcon = {
            Image(
                modifier = Modifier.width(120.dp),
                contentScale = ContentScale.FillWidth,
                painter = painterResource(id = R.drawable.logo_horizontal),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                contentDescription = "lonavigateTo"
            )
        },
        actions = {
            IconButton(onClick = navigateToSearch) {
                Icon(
                    modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "iconSearch",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            IconButton(onClick = navigateToMe) {
                Image(
                    modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
                    painter = painterResource(id = R.drawable.ic_avatar),
                    contentDescription = "iconUser",
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

@Composable
fun LazyItemScope.Banner(bannerData: List<BannerData>) {
    //banner
    Banner(count = bannerData.size) { index ->
        Surface(
            modifier = Modifier
                .height(220.dp)
                .padding(horizontal = 16.dp),
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