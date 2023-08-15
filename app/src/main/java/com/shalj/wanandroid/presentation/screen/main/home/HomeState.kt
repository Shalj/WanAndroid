package com.shalj.wanandroid.presentation.screen.main.home

import com.shalj.wanandroid.model.ArticleData
import com.shalj.wanandroid.model.BannerData
import com.shalj.wanandroid.presentation.components.multistatewidget.MultiStateWidgetState

data class HomeState(
    var multiState: MultiStateWidgetState = MultiStateWidgetState.Loading,
    var banner: List<BannerData> = listOf(),
    var errorMsg: String = ""
)

sealed class HomeEvent {
    data class CollectArticle(var articleData: ArticleData) : HomeEvent()
}