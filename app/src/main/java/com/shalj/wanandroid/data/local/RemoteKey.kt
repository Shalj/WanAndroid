package com.shalj.wanandroid.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RemoteKey(
    @PrimaryKey
    val id: Int?,
    val prevKey: Int?,
    val nextKey: Int?
)
