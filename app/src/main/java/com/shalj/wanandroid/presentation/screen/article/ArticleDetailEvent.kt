package com.shalj.wanandroid.presentation.screen.article

import com.shalj.wanandroid.data.local.article.ArticleEntity

sealed class ArticleDetailEvent {
    data class Init(val id: Int?) : ArticleDetailEvent()
    data class CollectArticle(val article: ArticleEntity) : ArticleDetailEvent()
    object ShowMoreMenu : ArticleDetailEvent()
    object HideMoreMenu : ArticleDetailEvent()
}
