package com.shalj.wanandroid.data.local.article

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<ArticleEntity>)

    @Query("SELECT * FROM articleentity ORDER BY publishTime DESC")
    fun pagingSource(): PagingSource<Int, ArticleEntity>

    @Query("DELETE FROM articleentity")
    suspend fun clearAll()

    @Update
    suspend fun update(article: ArticleEntity)

    @Query("SELECT * FROM articleentity WHERE id=:id")
    suspend fun queryArticleById(id: Int?): ArticleEntity
}