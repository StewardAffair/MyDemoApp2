package com.roman.mydemoapp.roast.domain.usecase

import com.roman.mydemoapp.roast.domain.model.RoastedPlaylist
import com.roman.mydemoapp.roast.domain.repository.PlaylistRepository
import com.roman.mydemoapp.roast.domain.repository.RoastRepository
import com.roman.mydemoapp.settings.domain.usecase.GetRoastLevelUseCase
import kotlinx.coroutines.flow.first

class RoastPlaylistUseCase(
    private val playlistRepository: PlaylistRepository,
    private val roastRepository: RoastRepository,
    private val getRoastLevel: GetRoastLevelUseCase
) {
    suspend operator fun invoke(url: String): RoastedPlaylist {
        val level = getRoastLevel().first()
        val playlist = playlistRepository.getPlaylist(url)
        val result = roastRepository.roast(playlist, level)
        return RoastedPlaylist(playlist.title, playlist.trackCount, level, result.score, result.text)
    }
}

class RoastScreenshotUseCase(
    private val roastRepository: RoastRepository,
    private val getRoastLevel: GetRoastLevelUseCase
) {
    suspend operator fun invoke(imageUri: String): RoastedPlaylist {
        val level = getRoastLevel().first()
        val result = roastRepository.roastScreenshot(imageUri, level)
        return RoastedPlaylist(result.title, result.trackCount, level, result.score, result.text)
    }
}
