package com.roman.mydemoapp.history.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RoastEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun roastDao(): RoastDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        /** Process-wide singleton; there is no DI yet. */
        fun get(context: Context): AppDatabase = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "playlist_roast.db"
            ).build().also { instance = it }
        }
    }
}
