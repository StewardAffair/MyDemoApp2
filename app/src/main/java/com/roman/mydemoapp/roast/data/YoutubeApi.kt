package com.roman.mydemoapp.roast.data

import com.roman.mydemoapp.roast.domain.model.RoastError
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

@Serializable
internal data class PlaylistsDto(val items: List<PlaylistDto> = emptyList())

@Serializable
internal data class PlaylistDto(val snippet: SnippetDto)

@Serializable
internal data class PlaylistItemsDto(
    val items: List<PlaylistItemDto> = emptyList(),
    val nextPageToken: String? = null,
    val pageInfo: PageInfoDto? = null
)

@Serializable
internal data class PlaylistItemDto(val snippet: SnippetDto)

@Serializable
internal data class SnippetDto(val title: String, val videoOwnerChannelTitle: String? = null)

@Serializable
internal data class PageInfoDto(val totalResults: Int = 0)

internal interface YoutubeService {
    @GET("playlists")
    suspend fun playlists(
        @Query("id") playlistId: String,
        @Query("key") apiKey: String,
        @Query("part") part: String = "snippet"
    ): Response<PlaylistsDto>

    @GET("playlistItems")
    suspend fun playlistItems(
        @Query("playlistId") playlistId: String,
        @Query("pageToken") pageToken: String?,
        @Query("key") apiKey: String,
        @Query("part") part: String = "snippet",
        @Query("maxResults") maxResults: Int = 50
    ): Response<PlaylistItemsDto>
}

/** YouTube Data API v3. Only public and unlisted playlists are readable with an API key. */
internal class YoutubeApi(
    private val service: YoutubeService,
    private val apiKey: String
) {
    suspend fun getPlaylistTitle(playlistId: String): String? =
        service.playlists(playlistId, apiKey)
            .requireBody(notFound = RoastError.PlaylistNotFound, badRequest = RoastError.Unauthorized)
            .items.firstOrNull()?.snippet?.title

    suspend fun getPlaylistItems(playlistId: String, pageToken: String?): PlaylistItemsDto =
        service.playlistItems(playlistId, pageToken, apiKey)
            .requireBody(notFound = RoastError.PlaylistNotFound, badRequest = RoastError.Unauthorized)
}
