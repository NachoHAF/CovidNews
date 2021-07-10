package com.upsa.covidnews.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news_table")
data class NewsRoom (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "title") val title:String,
    @ColumnInfo(name = "publishedAt") val publishedAt:String,
    @ColumnInfo(name = "urlToImage") val urlToImage:String?,
    @ColumnInfo(name = "url")  val url:String?



)

