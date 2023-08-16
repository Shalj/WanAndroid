package com.shalj.wanandroid.presentation.screen.main.home

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.map
import com.shalj.wanandroid.base.BaseViewModel
import com.shalj.wanandroid.data.local.ArticleDatabase
import com.shalj.wanandroid.data.local.ArticleEntity
import com.shalj.wanandroid.data.mappers.toArticleData
import com.shalj.wanandroid.data.mappers.toArticleEntity
import com.shalj.wanandroid.domain.ArticleData
import com.shalj.wanandroid.domain.BannerData
import com.shalj.wanandroid.net.Api
import com.shalj.wanandroid.net.RequestResult
import com.shalj.wanandroid.presentation.components.multistatewidget.MultiStateWidgetState
import com.shalj.wanandroid.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val api: Api,
    pager: Pager<Int, ArticleEntity>,
    private val articleDb: ArticleDatabase,
) : BaseViewModel() {

    private val _multiState = MutableStateFlow<MultiStateWidgetState>(MultiStateWidgetState.Loading)

    private val _banner = MutableStateFlow(listOf<BannerData>())

    val articles = pager.flow.map { pagingData ->
        pagingData.map { article ->
            article.toArticleData()
        }
    }.cachedIn(viewModelScope)

    val uiState = combine(
        _multiState, _banner,
    ) { refreshing, banner ->
        HomeState(refreshing, banner)
    }.shareIn(viewModelScope, started = SharingStarted.WhileSubscribed())

    init {
        getBannerData()
    }

    private fun getBannerData() {
        viewModelScope.launch {
            val result = api.getBannerData()
            withContext(Dispatchers.Main) {
                when (result) {
                    is RequestResult.Error -> result.msg
                    is RequestResult.Success -> _banner.value = result.data
                }
            }
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.CollectArticle -> collectArticle(event.articleData)
        }
    }

    private fun collectArticle(articleData: ArticleData) {
        viewModelScope.launch {
            val result = if (articleData.collect == true) {
                //取消收藏
                api.unCollectArticle(articleData.id ?: -1)
            } else {
                api.collectArticle(articleData.id ?: -1)
            }

            when (result) {
                is RequestResult.Error -> toast.value = result.msg
                is RequestResult.Success -> {
                    articleData.collect = !(articleData.collect ?: false)
                    updateArticles(articleData)
                }
            }
        }
    }

    private fun updateArticles(articleData: ArticleData) {
        viewModelScope.launch {
            articleDb.dao.update(articleData.toArticleEntity())
        }
    }
}