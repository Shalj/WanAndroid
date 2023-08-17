package com.shalj.wanandroid.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKey>)

    @Query("SELECT * FROM remotekey WHERE id = :id")
    suspend fun remoteKeysId(id: Int?): RemoteKey?

    @Query("DELETE FROM remotekey")
    suspend fun clearRemoteKeys()
}