package com.shalj.wanandroid.data.local.article

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ReadDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(readEntity: ReadEntity)

    @Query("SELECT * FROM readentity WHERE id = :id")
    suspend fun readId(id: Int?): ReadEntity?

    @Query("DELETE FROM readentity")
    suspend fun clearAll()
}