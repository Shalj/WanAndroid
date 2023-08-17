package com.shalj.wanandroid.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ReadEntity(
    @PrimaryKey
    val id: Int?,
    val title: String?,
    val lastReadTime: Long,
)
