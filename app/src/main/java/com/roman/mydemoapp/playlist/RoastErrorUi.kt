package com.roman.mydemoapp.playlist

import androidx.annotation.StringRes
import com.roman.mydemoapp.R
import com.roman.mydemoapp.roast.domain.model.RoastError

@get:StringRes
val RoastError.message: Int
    get() = when (this) {
        RoastError.NoNetwork -> R.string.error_no_network
        RoastError.Unauthorized -> R.string.error_unauthorized
        RoastError.RateLimited -> R.string.error_rate_limited
        RoastError.PlaylistNotFound -> R.string.error_playlist_not_found
        RoastError.UnsupportedLink -> R.string.error_unsupported_link
        RoastError.NotAPlaylist -> R.string.error_not_a_playlist
        RoastError.BadResponse -> R.string.error_bad_response
        RoastError.Unknown -> R.string.error_unknown
    }
