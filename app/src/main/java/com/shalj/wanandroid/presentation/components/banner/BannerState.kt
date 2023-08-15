package com.shalj.wanandroid.presentation.components.banner

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
fun rememberBannerState(initialPage: Int = 0): BannerState {
    return rememberSaveable(saver = BannerState.Saver) {
        BannerState(initialPage = initialPage)
    }
}

data class BannerState constructor(
    var initialPage: Int = 0,
    var isTouching: Boolean = false,
) {

    @OptIn(ExperimentalFoundationApi::class)
    var pagerState: PagerState? = null

    @OptIn(ExperimentalFoundationApi::class)
    suspend fun nextPage(count: Int) {
        pagerState ?: return

        var targetIndex = pagerState!!.currentPage + 1
        if (targetIndex >= count) targetIndex = 0
        pagerState!!.animateScrollToPage(targetIndex)
    }

    @OptIn(ExperimentalFoundationApi::class)
    val currentPage: Int
        get() = pagerState?.currentPage ?: initialPage

    companion object {
        /**
         * To keep current page
         */
        val Saver: Saver<BannerState, *> = listSaver(
            save = {
                listOf(
                    it.currentPage
                )
            },
            restore = {
                BannerState(initialPage = it[0])
            }
        )
    }
}