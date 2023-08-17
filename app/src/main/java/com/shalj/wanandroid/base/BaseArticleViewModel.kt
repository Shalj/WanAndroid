package com.shalj.wanandroid.base

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.viewModelScope
import com.shalj.wanandroid.data.local.article.ArticleDatabase
import com.shalj.wanandroid.data.local.article.ArticleEntity
import com.shalj.wanandroid.di.userInfoKey
import com.shalj.wanandroid.domain.LoginResp
import com.shalj.wanandroid.net.Api
import com.shalj.wanandroid.net.RequestResult
import com.shalj.wanandroid.net.toModel
import com.shalj.wanandroid.toast
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

open class BaseArticleViewModel(
    private val articleDb: ArticleDatabase,
    private val api: Api,
    private val dataStore: DataStore<Preferences>
) : BaseViewModel() {

    protected val isUpdatingLikeState = MutableStateFlow(false)

    protected val needLogin = MutableStateFlow(false)

    protected fun collectArticle(articleData: ArticleEntity) {
        viewModelScope.launch {
            val userInfo =
                dataStore.data.map { it[userInfoKey] }.firstOrNull().orEmpty().ifEmpty { "{}" }
                    .toModel<LoginResp>()
            //用户没有登录
            if (userInfo.username.orEmpty().isEmpty() || userInfo.password.orEmpty().isEmpty()) {
                toast.value = "请先登录"
                needLogin.value = true
                delay(300)
                needLogin.value = false
                return@launch
            }

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