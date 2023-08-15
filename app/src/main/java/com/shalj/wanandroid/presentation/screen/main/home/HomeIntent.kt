package com.shalj.wanandroid.presentation.screen.main.home

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.shalj.wanandroid.base.BaseViewModel
import com.shalj.wanandroid.model.ArticleData
import com.shalj.wanandroid.model.BannerData
import com.shalj.wanandroid.net.Api
import com.shalj.wanandroid.net.RequestResult
import com.shalj.wanandroid.presentation.components.multistatewidget.MultiStateWidgetState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val api: Api
) : BaseViewModel() {

    private val _multiState = MutableStateFlow<MultiStateWidgetState>(MultiStateWidgetState.Loading)
    private val _errorMsg = MutableStateFlow("")

    private val _banner = MutableStateFlow(listOf<BannerData>())

    var articles = Pager(
        config = PagingConfig(pageSize = 20, initialLoadSize = 60),
        pagingSourceFactory = { HomePagingSource(api, _multiState, _errorMsg) },
    ).flow.cachedIn(viewModelScope)

    val uiState = combine(
        _multiState, _banner, _errorMsg
    ) { refreshing, banner, errorMsg ->
        HomeState(refreshing, banner, errorMsg)
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
                is RequestResult.Error -> _errorMsg.value = result.msg
                is RequestResult.Success -> {
                    articleData.collect = !(articleData.collect ?: false)
                    updateArticles(articleData)
                }
            }
        }
    }

    private fun updateArticles(articleData: ArticleData) {
        viewModelScope.launch {
            articles.onEach { pagingData ->
                pagingData.map { article ->
                    if (article.id == articleData.id) {
                        article.copy(collect = articleData.collect)
                    } else {
                        article
                    }
                }
            }
        }
    }
}