package com.roman.mydemoapp.history.domain.usecase

import com.roman.mydemoapp.history.domain.model.SavedRoast
import com.roman.mydemoapp.history.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow

class ObserveRecentRoastsUseCase(private val repository: HistoryRepository) {
    operator fun invoke(limit: Int): Flow<List<SavedRoast>> = repository.observeRecent(limit)
}

class GetSavedRoastUseCase(private val repository: HistoryRepository) {
    suspend operator fun invoke(id: Long): SavedRoast? = repository.get(id)
}

class SaveRoastUseCase(private val repository: HistoryRepository) {
    suspend operator fun invoke(roast: SavedRoast): Long = repository.save(roast)
}

class DeleteRoastUseCase(private val repository: HistoryRepository) {
    suspend operator fun invoke(id: Long) = repository.delete(id)
}
