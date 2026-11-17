package com.roman.mydemoapp.history.data

import com.roman.mydemoapp.history.domain.model.SavedRoast
import com.roman.mydemoapp.history.domain.repository.HistoryRepository
import com.roman.mydemoapp.roast.domain.model.RoastedPlaylist
import com.roman.mydemoapp.settings.domain.model.DEFAULT_LEVEL
import com.roman.mydemoapp.settings.domain.model.RoastLevel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HistoryRepositoryImpl(private val dao: RoastDao) : HistoryRepository {
    override fun observeRecent(limit: Int): Flow<List<SavedRoast>> =
        dao.observeRecent(limit).map { list -> list.map { it.toDomain() } }

    override suspend fun get(id: Long): SavedRoast? = dao.getById(id)?.toDomain()

    override suspend fun save(roast: SavedRoast): Long = dao.upsert(roast.toEntity())

    override suspend fun delete(id: Long) = dao.deleteById(id)
}

private fun RoastEntity.toDomain() = SavedRoast(
    id = id,
    roasted = RoastedPlaylist(
        title = title,
        trackCount = trackCount,
        level = RoastLevel.entries.firstOrNull { it.name == level } ?: DEFAULT_LEVEL,
        score = score,
        text = text
    ),
    fromScreenshot = fromScreenshot,
    sourceRef = sourceRef,
    createdAt = createdAt
)

private fun SavedRoast.toEntity() = RoastEntity(
    id = id,
    title = roasted.title,
    trackCount = roasted.trackCount,
    level = roasted.level.name,
    score = roasted.score,
    text = roasted.text,
    fromScreenshot = fromScreenshot,
    sourceRef = sourceRef,
    createdAt = createdAt
)
