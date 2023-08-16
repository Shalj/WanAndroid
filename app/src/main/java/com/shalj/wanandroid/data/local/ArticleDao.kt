package com.shalj.wanandroid.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert

@Dao
interface ArticleDao {
    @Upsert
    suspend fun upsertAll(articles: List<ArticleEntity>)

    @Query("SELECT * FROM articleentity ORDER BY publishTime DESC")
    fun pagingSource(): PagingSource<Int, ArticleEntity>

    @Query("DELETE FROM articleentity")
    suspend fun clearAll()

    @Update
    suspend fun update(article: ArticleEntity)
}