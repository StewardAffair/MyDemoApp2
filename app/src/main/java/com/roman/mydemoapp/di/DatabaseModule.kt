package com.roman.mydemoapp.di

import android.content.Context
import androidx.room.Room
import com.roman.mydemoapp.history.data.AppDatabase
import com.roman.mydemoapp.history.data.RoastDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "playlist_roast.db").build()

    @Provides
    fun provideRoastDao(database: AppDatabase): RoastDao = database.roastDao()
}
