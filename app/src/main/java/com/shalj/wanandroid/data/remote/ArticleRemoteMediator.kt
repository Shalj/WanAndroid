package com.shalj.wanandroid.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.shalj.wanandroid.data.local.article.ArticleDatabase
import com.shalj.wanandroid.data.local.article.ArticleEntity
import com.shalj.wanandroid.data.local.RemoteKey
import com.shalj.wanandroid.ext.currentKey
import com.shalj.wanandroid.ext.nextKey
import com.shalj.wanandroid.ext.prevKey
import com.shalj.wanandroid.net.Api
import com.shalj.wanandroid.net.RequestResult

@OptIn(ExperimentalPagingApi::class)
class ArticleRemoteMediator(
    private val articleDb: ArticleDatabase,
    private val api: Api
) : RemoteMediator<Int, ArticleEntity>() {

    companion object {
        const val START_PAGE = 0
    }

    //通过id查询remoteKey
    private val queryKey: suspend (value: ArticleEntity) -> RemoteKey? = {
        articleDb.keysDao.remoteKeysId(it.id)
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ArticleEntity>
    ): MediatorResult {

        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    state.currentKey(queryKey)?.nextKey?.minus(1) ?: START_PAGE
                }

                LoadType.PREPEND -> {
                    val remoteKey = state.prevKey(queryKey)
                    remoteKey?.prevKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
                }

                LoadType.APPEND -> {
                    val remoteKey = state.nextKey(queryKey)
                    remoteKey?.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
                }
            }

            val articles = when (val result = api.getHomeArticleList(page, state.config.pageSize)) {
                is RequestResult.Error -> {
                    return MediatorResult.Error(result.cause ?: Throwable("Unknown Error"))
                }

                is RequestResult.Success -> {
                    result.data.datas
                }
            }
            val endOfPaginationReached = articles.isEmpty()

            articleDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    articleDb.dao.clearAll()
                    articleDb.keysDao.clearRemoteKeys()
                }

                articles.onEach {
                    it.read = articleDb.readDao.readId(it.id) != null
                }

                val prevKey = if (page == START_PAGE) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = articles.map {
                    RemoteKey(it.id, prevKey, nextKey)
                }
                articleDb.dao.insertAll(articles)
                articleDb.keysDao.insertAll(keys)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}