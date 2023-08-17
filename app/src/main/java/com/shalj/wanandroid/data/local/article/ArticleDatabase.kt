package com.shalj.wanandroid.data.local.article

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shalj.wanandroid.data.local.RemoteKey
import com.shalj.wanandroid.data.local.RemoteKeyDao

@Database(
    version = 1,
    entities = [ArticleEntity::class, RemoteKey::class, ReadEntity::class],
    exportSchema = true,
//    autoMigrations = [AutoMigration(from = 1, to = 2)],
)
abstract class ArticleDatabase : RoomDatabase() {
    abstract val dao: ArticleDao
    abstract val keysDao: RemoteKeyDao
    abstract val readDao: ReadDao
}