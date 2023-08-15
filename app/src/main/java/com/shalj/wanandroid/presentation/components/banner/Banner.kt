package com.shalj.wanandroid.presentation.components.banner

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Banner(
    count: Int,
    bannerState: BannerState = rememberBannerState(),
    delay: Long = 2000,
    content: @Composable (page: Int) -> Unit,
) {
    if (count <= 0) return

    val pagerState = rememberPagerState(
        initialPage = bannerState.initialPage,
        initialPageOffsetFraction = 0f
    ) {
        count
    }
    bannerState.pagerState = pagerState

    LaunchedEffect(bannerState.pagerState?.settledPage) {
        delay(delay)
        try {
            bannerState.nextPage(count)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    HorizontalPager(
        state = pagerState,
    ) {
        content(it)
    }
}