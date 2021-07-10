package com.upsa.covidnews.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database
(
    entities = [NewsRoom::class], version = 1
)
abstract class NewsDatabase: RoomDatabase() {

    abstract fun newsDao(): NewsDao

    companion object {
        private var instance: NewsDatabase? = null
        fun getInstance( context: Context): NewsDatabase? {
            if (instance == null) {
                synchronized(NewsDatabase::class.java) {
                    instance = Room.databaseBuilder(context.applicationContext, NewsDatabase::class.java, "news_database")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return instance
        }
    }
}