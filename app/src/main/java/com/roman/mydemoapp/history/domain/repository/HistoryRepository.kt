package com.roman.mydemoapp.history.domain.repository

import com.roman.mydemoapp.history.domain.model.SavedRoast
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun observeRecent(limit: Int): Flow<List<SavedRoast>>

    suspend fun get(id: Long): SavedRoast?

    /** Inserts, or replaces the row when [SavedRoast.id] already exists; returns the row id. */
    suspend fun save(roast: SavedRoast): Long

    suspend fun delete(id: Long)
}
