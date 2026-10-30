package com.roman.mydemoapp.roast.domain.repository

import com.roman.mydemoapp.roast.domain.model.Playlist
import com.roman.mydemoapp.roast.domain.model.RoastResult
import com.roman.mydemoapp.settings.domain.model.RoastLevel

interface PlaylistRepository {
    /** Throws [com.roman.mydemoapp.roast.domain.model.RoastException] on failure. */
    suspend fun getPlaylist(url: String): Playlist
}

interface RoastRepository {
    /** Throws [com.roman.mydemoapp.roast.domain.model.RoastException] on failure. */
    suspend fun roast(playlist: Playlist, level: RoastLevel): RoastResult

    suspend fun roastScreenshot(imageUri: String, level: RoastLevel): RoastResult
}
