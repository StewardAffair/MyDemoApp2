package com.roman.mydemoapp.roast.data

import com.roman.mydemoapp.roast.domain.extractYoutubePlaylistId
import com.roman.mydemoapp.roast.domain.model.Playlist
import com.roman.mydemoapp.roast.domain.model.RoastError
import com.roman.mydemoapp.roast.domain.model.RoastException
import com.roman.mydemoapp.roast.domain.model.Track
import com.roman.mydemoapp.roast.domain.repository.PlaylistRepository

private const val MAX_TRACKS = 200
private val unavailableTitles = setOf("Private video", "Deleted video")

class PlaylistRepositoryImpl internal constructor(private val api: YoutubeApi) : PlaylistRepository {
    constructor() : this(YoutubeApi())

    override suspend fun getPlaylist(url: String): Playlist = apiCall {
        val id = extractYoutubePlaylistId(url) ?: throw RoastException(RoastError.UnsupportedLink)
        val title = api.getPlaylistTitle(id) ?: throw RoastException(RoastError.PlaylistNotFound)

        val tracks = mutableListOf<Track>()
        var total = 0
        var pageToken: String? = null
        do {
            val page = api.getPlaylistItems(id, pageToken)
            total = page.pageInfo?.totalResults ?: total
            page.items
                .map { it.snippet }
                .filter { it.title !in unavailableTitles }
                .mapTo(tracks) { Track(it.title, it.videoOwnerChannelTitle.orEmpty().removeSuffix(" - Topic")) }
            pageToken = page.nextPageToken
        } while (pageToken != null && tracks.size < MAX_TRACKS)

        Playlist(title, total.coerceAtLeast(tracks.size), tracks.take(MAX_TRACKS))
    }
}
