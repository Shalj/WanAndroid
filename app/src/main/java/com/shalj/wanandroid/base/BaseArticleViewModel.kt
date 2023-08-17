package com.shalj.wanandroid.base

import androidx.lifecycle.viewModelScope
import com.shalj.wanandroid.data.local.article.ArticleDatabase
import com.shalj.wanandroid.data.local.article.ArticleEntity
import com.shalj.wanandroid.net.Api
import com.shalj.wanandroid.net.RequestResult
import com.shalj.wanandroid.toast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

open class BaseArticleViewModel(
    private val articleDb: ArticleDatabase,
    private val api: Api
) : BaseViewModel() {

    protected val isUpdatingLikeState = MutableStateFlow(false)

    protected fun collectArticle(articleData: ArticleEntity) {
        viewModelScope.launch {
            updateArticles(articleData.apply { isUpdatingLikeState = true })
            val result = if (articleData.collect == true) {
                //取消收藏
                api.unCollectArticle(articleData.id ?: -1)
            } else {
                api.collectArticle(articleData.id ?: -1)
            }

            when (result) {
                is RequestResult.Error -> {
                    toast.value = result.msg
                    updateArticles(articleData.apply { isUpdatingLikeState = false })
                }

                is RequestResult.Success -> {
                    articleData.collect = !(articleData.collect ?: false)
                    updateArticles(articleData.apply { isUpdatingLikeState = false })
                }
            }
        }
    }

    private fun updateArticles(articleData: ArticleEntity) {
        viewModelScope.launch {
            isUpdatingLikeState.value = articleData.isUpdatingLikeState
            articleDb.dao.update(articleData)
        }
    }
}