package com.shalj.wanandroid.ui.screen.main.home

import androidx.paging.PagingData
import com.shalj.wanandroid.model.ArticleData
import com.shalj.wanandroid.model.BannerData
import com.shalj.wanandroid.ui.components.multistatewidget.MultiStateWidgetState

data class HomeState(
    var multiState: MultiStateWidgetState = MultiStateWidgetState.Loading,
    var banner: List<BannerData> = listOf(),
    var errorMsg: String = ""
)