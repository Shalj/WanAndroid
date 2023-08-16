package com.shalj.wanandroid.presentation.screen.main.home

import com.shalj.wanandroid.domain.ArticleData
import com.shalj.wanandroid.domain.BannerData
import com.shalj.wanandroid.presentation.components.multistatewidget.MultiStateWidgetState

data class HomeState(
    var multiState: MultiStateWidgetState = MultiStateWidgetState.Loading,
    var banner: List<BannerData> = listOf(),
)

sealed class HomeEvent {
    data class CollectArticle(var articleData: ArticleData) : HomeEvent()
}