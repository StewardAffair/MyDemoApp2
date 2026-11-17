package com.roman.mydemoapp.history.domain.usecase

import com.roman.mydemoapp.history.domain.model.SavedRoast
import com.roman.mydemoapp.history.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveRecentRoastsUseCase @Inject constructor(private val repository: HistoryRepository) {
    operator fun invoke(limit: Int): Flow<List<SavedRoast>> = repository.observeRecent(limit)
}

class GetSavedRoastUseCase @Inject constructor(private val repository: HistoryRepository) {
    suspend operator fun invoke(id: Long): SavedRoast? = repository.get(id)
}

class SaveRoastUseCase @Inject constructor(private val repository: HistoryRepository) {
    suspend operator fun invoke(roast: SavedRoast): Long = repository.save(roast)
}

class DeleteRoastUseCase @Inject constructor(private val repository: HistoryRepository) {
    suspend operator fun invoke(id: Long) = repository.delete(id)
}
