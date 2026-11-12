package com.roman.mydemoapp.history.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RoastEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun roastDao(): RoastDao
}
