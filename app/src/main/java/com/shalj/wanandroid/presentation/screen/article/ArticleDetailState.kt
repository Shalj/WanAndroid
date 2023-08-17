package com.shalj.wanandroid.presentation.screen.article

import com.shalj.wanandroid.data.local.article.ArticleEntity

data class ArticleDetailState(
    val article: ArticleEntity = ArticleEntity(),
    val isUpdatingLikeState: Boolean = false,
    val showMoreMenu: Boolean = false,
)
