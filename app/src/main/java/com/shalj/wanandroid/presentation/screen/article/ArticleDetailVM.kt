package com.shalj.wanandroid.presentation.screen.article

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.viewModelScope
import com.google.accompanist.web.WebContent
import com.google.accompanist.web.WebViewState
import com.shalj.wanandroid.base.BaseArticleViewModel
import com.shalj.wanandroid.data.local.article.ArticleDatabase
import com.shalj.wanandroid.data.local.article.ArticleEntity
import com.shalj.wanandroid.net.Api
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleDetailVM @Inject constructor(
    private val articleDb: ArticleDatabase,
    api: Api,
    dataStore: DataStore<Preferences>
) : BaseArticleViewModel(articleDb, api, dataStore) {

    private val _article = MutableStateFlow(ArticleEntity())
    private val _showMoreMenu = MutableStateFlow(false)

    val state = combine(
        _article,
        isUpdatingLikeState,
        _showMoreMenu,
        needLogin
    ) { article, isUpdatingLikeState, showMoreMenu, needLogin->
        ArticleDetailState(article, isUpdatingLikeState, showMoreMenu, needLogin)
    }.shareIn(viewModelScope, SharingStarted.WhileSubscribed())


    fun onEvent(event: ArticleDetailEvent) {
        when (event) {
            is ArticleDetailEvent.Init -> {
                viewModelScope.launch {
                    _article.value = articleDb.dao.queryArticleById(event.id)
                }
            }

            is ArticleDetailEvent.CollectArticle -> collectArticle(event.article)
            ArticleDetailEvent.ShowMoreMenu -> _showMoreMenu.value = true
            ArticleDetailEvent.HideMoreMenu -> _showMoreMenu.value = false
        }
    }
}