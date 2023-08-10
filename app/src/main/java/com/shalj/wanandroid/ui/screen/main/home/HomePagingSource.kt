package com.shalj.wanandroid.ui.screen.main.home

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shalj.wanandroid.model.ArticleData
import com.shalj.wanandroid.net.Api
import com.shalj.wanandroid.net.RequestResult
import com.shalj.wanandroid.ui.components.multistatewidget.MultiStateWidgetState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

class HomePagingSource(
    private val multiState: MutableStateFlow<MultiStateWidgetState>,
    private val errorMsg: MutableStateFlow<String>
) : PagingSource<Int, ArticleData>() {
    override fun getRefreshKey(state: PagingState<Int, ArticleData>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleData> {
        return try {

            val nextPage = params.key ?: 0

            when (val result = Api.getHomeArticleList(nextPage)) {
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