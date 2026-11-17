package com.roman.mydemoapp.history.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RoastDao {
    @Query("SELECT * FROM roasts ORDER BY createdAt DESC LIMIT :limit")
    fun observeRecent(limit: Int): Flow<List<RoastEntity>>

    @Query("SELECT * FROM roasts WHERE id = :id")
    suspend fun getById(id: Long): RoastEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: RoastEntity): Long

    @Query("DELETE FROM roasts WHERE id = :id")
    suspend fun deleteById(id: Long)
}
