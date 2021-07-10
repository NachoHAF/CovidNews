package com.upsa.covidnews.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(newsRoom: NewsRoom)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllNews(newsRoomList: List<NewsRoom>)

    @Query("SELECT * FROM news_table")
    suspend fun getAllNews(): List<NewsRoom>

    @Query("DELETE FROM news_table")
    suspend fun nukeNewsTable()
}