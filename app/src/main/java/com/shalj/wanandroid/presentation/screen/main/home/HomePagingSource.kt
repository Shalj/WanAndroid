package com.shalj.wanandroid.presentation.screen.main.home

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shalj.wanandroid.data.local.ArticleEntity
import com.shalj.wanandroid.net.Api
import com.shalj.wanandroid.net.RequestResult
import com.shalj.wanandroid.presentation.components.multistatewidget.MultiStateWidgetState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

class HomePagingSource(
    private val api: Api,
    private val multiState: MutableStateFlow<MultiStateWidgetState>,
    private val errorMsg: MutableStateFlow<String>
) : PagingSource<Int, ArticleEntity>() {
    override fun getRefreshKey(state: PagingState<Int, ArticleEntity>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleEntity> {
        return try {

            val nextPage = params.key ?: 0

            when (val result = api.getHomeArticleList(nextPage, params.loadSize)) {
                is RequestResult.Error -> withContext(Dispatchers.Main) {
                    errorMsg.value = result.msg
                    multiState.value = MultiStateWidgetState.Error
                    LoadResult.Error(Throwable(result.msg))
                }

                is RequestResult.Success -> withContext(Dispatchers.Main) {
                    multiState.value = MultiStateWidgetState.Content
                    LoadResult.Page(
                        data = result.data.datas,
                        nextKey = nextPage.plus(1),
                        prevKey = if (nextPage == 0) null else nextPage - 1
                    )
                }
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}