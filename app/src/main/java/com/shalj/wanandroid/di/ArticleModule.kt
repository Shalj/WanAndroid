package com.shalj.wanandroid.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.shalj.wanandroid.data.local.ArticleDatabase
import com.shalj.wanandroid.data.local.ArticleEntity
import com.shalj.wanandroid.data.remote.ArticleRemoteMediator
import com.shalj.wanandroid.net.Api
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ArticleModule {

    @Provides
    @Singleton
    fun provideArticleDatabase(@ApplicationContext context: Context): ArticleDatabase =
        Room.databaseBuilder(context, ArticleDatabase::class.java, "article.db")
            .fallbackToDestructiveMigration()
            .build()

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideArticlePager(db: ArticleDatabase, api: Api): Pager<Int, ArticleEntity> =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true,
            ),
            remoteMediator = ArticleRemoteMediator(db, api),
            pagingSourceFactory = { db.dao.pagingSource() }
        )
}