package com.shalj.wanandroid.data.local.article

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ArticleEntity(
    @PrimaryKey val id: Int = 0,
    val apkLink: String? = "",
    val audit: Int? = 0,
    val author: String? = "",
    val canEdit: Boolean? = false,
    val chapterId: Int? = 0,
    val chapterName: String? = "",
    var collect: Boolean? = false,
    val courseId: Int? = 0,
    val desc: String? = "",
    val descMd: String? = "",
    val envelopePic: String? = "",
    val fresh: Boolean? = false,
    val host: String? = "",
    val isAdminAdd: Boolean? = false,
    val link: String? = "",
    val niceDate: String? = "",
    val niceShareDate: String? = "",
    val origin: String? = "",
    val prefix: String? = "",
    val projectLink: String? = "",
    val publishTime: Long? = 0,
    val realSuperChapterId: Int? = 0,
    val selfVisible: Int? = 0,
    val shareDate: Long? = 0,
    val shareUser: String? = "",
    val superChapterId: Int? = 0,
    val superChapterName: String? = "",
    val title: String? = "",
    val type: Int? = 0,
    val userId: Int? = 0,
    val visible: Int? = 0,
    val zan: Int? = 0,
    @ColumnInfo(name = "likeState", defaultValue = "0")
    var isUpdatingLikeState: Boolean = false,
    @ColumnInfo(name = "read", defaultValue = "0")
    var read: Boolean = false,
)

fun ArticleEntity.toReadEntity() = ReadEntity(
    id = id,
    title = title,
    lastReadTime = System.currentTimeMillis(),
)