package com.shalj.wanandroid.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.shalj.wanandroid.data.local.ArticleDatabase
import com.shalj.wanandroid.data.local.ArticleEntity
import com.shalj.wanandroid.data.mappers.toArticleEntity
import com.shalj.wanandroid.net.Api
import com.shalj.wanandroid.net.RequestResult

private var curPage = 0

@OptIn(ExperimentalPagingApi::class)
class ArticleRemoteMediator(
    private val articleDb: ArticleDatabase,
    private val api: Api
) : RemoteMediator<Int, ArticleEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ArticleEntity>
    ): MediatorResult {

        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        curPage = 0
                    }
                    curPage
                }
            }
            var noMore = false

            val articles = when (val result = api.getHomeArticleList(page, state.config.pageSize)) {
                is RequestResult.Error -> {
                    return MediatorResult.Error(result.cause ?: Throwable("Unknown Error"))
                }

                is RequestResult.Success -> {
                    curPage = result.data.curPage
                    noMore = result.data.curPage >= result.data.pageCount
                    result.data.datas
                }
            }

            articleDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    articleDb.dao.clearAll()
                }
                articleDb.dao.upsertAll(articles.map {
                    it.toArticleEntity()
                })
            }

            MediatorResult.Success(endOfPaginationReached = noMore)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}