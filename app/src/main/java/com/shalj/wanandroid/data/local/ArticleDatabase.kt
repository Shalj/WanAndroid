package com.shalj.wanandroid.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 1,
    entities = [ArticleEntity::class],
)
abstract class ArticleDatabase : RoomDatabase() {
    abstract val dao: ArticleDao
}