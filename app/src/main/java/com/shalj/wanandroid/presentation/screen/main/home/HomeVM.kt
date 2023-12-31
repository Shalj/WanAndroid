package com.shalj.wanandroid.presentation.screen.main.home

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.room.withTransaction
import com.shalj.wanandroid.base.BaseArticleViewModel
import com.shalj.wanandroid.data.local.article.ArticleDatabase
import com.shalj.wanandroid.data.local.article.ArticleEntity
import com.shalj.wanandroid.data.local.article.toReadEntity
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
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val api: Api,
    pager: Pager<Int, ArticleEntity>,
    private val articleDb: ArticleDatabase,
    dataStore: DataStore<Preferences>
) : BaseArticleViewModel(articleDb, api, dataStore) {

    private val _multiState = MutableStateFlow<MultiStateWidgetState>(MultiStateWidgetState.Loading)

    private val _banner = MutableStateFlow(listOf<BannerData>())


    val articles = pager.flow.cachedIn(viewModelScope)

    val uiState = combine(
        _multiState, _banner, needLogin
    ) { refreshing, banner, needLogin ->
        HomeState(refreshing, banner, needLogin)
    }.shareIn(viewModelScope, started = SharingStarted.WhileSubscribed())

    init {
        getBannerData()
    }

    private fun getBannerData() {
        viewModelScope.launch {
            val result = api.getBannerData()
            withContext(Dispatchers.Main) {
                when (result) {
                    is RequestResult.Error -> {
                        toast.value = result.msg
                        _multiState.value = MultiStateWidgetState.Error
                    }

                    is RequestResult.Success -> {
                        _banner.value = result.data
                        _multiState.value =
                            if (result.data.isEmpty()) MultiStateWidgetState.Empty else MultiStateWidgetState.Content
                    }
                }
            }
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.CollectArticle -> collectArticle(event.articleData)
            is HomeEvent.ReadArticle -> {
                viewModelScope.launch {
                    articleDb.withTransaction {
                        articleDb.readDao.insert(event.articleData.toReadEntity())
                        articleDb.dao.update(event.articleData.apply { read = true })
                    }
                }
            }
        }
    }
}