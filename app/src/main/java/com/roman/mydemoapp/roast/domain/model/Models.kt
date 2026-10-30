package com.roman.mydemoapp.roast.domain.model

import com.roman.mydemoapp.settings.domain.model.RoastLevel

data class Track(val title: String, val artist: String)

data class Playlist(val title: String, val trackCount: Int, val tracks: List<Track>)

/** Raw verdict from the model; [title] and [trackCount] are only filled when read from a screenshot. */
data class RoastResult(
    val score: Int,
    val text: String,
    val title: String? = null,
    val trackCount: Int? = null
)

data class RoastedPlaylist(
    val title: String?,
    val trackCount: Int?,
    val level: RoastLevel,
    val score: Int,
    val text: String
)

enum class RoastError {
    NoNetwork,
    Unauthorized,
    RateLimited,
    PlaylistNotFound,
    UnsupportedLink,
    NotAPlaylist,
    BadResponse,
    Unknown
}

class RoastException(val error: RoastError, cause: Throwable? = null) : Exception(error.name, cause)
