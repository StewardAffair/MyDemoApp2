package com.roman.mydemoapp.roast.data

import com.roman.mydemoapp.BuildConfig
import com.roman.mydemoapp.network.HttpClientProvider
import com.roman.mydemoapp.roast.domain.model.RoastError
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.serialization.Serializable

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

/** YouTube Data API v3. Only public and unlisted playlists are readable with an API key. */
internal class YoutubeApi(
    private val client: HttpClient = HttpClientProvider.client,
    private val apiKey: String = BuildConfig.YOUTUBE_API_KEY
) {
    suspend fun getPlaylistTitle(playlistId: String): String? {
        val response = client.get("$BASE_URL/playlists") {
            parameter("part", "snippet")
            parameter("id", playlistId)
            parameter("key", apiKey)
        }
        response.ensureSuccess(notFound = RoastError.PlaylistNotFound, badRequest = RoastError.Unauthorized)
        return response.body<PlaylistsDto>().items.firstOrNull()?.snippet?.title
    }

    suspend fun getPlaylistItems(playlistId: String, pageToken: String?): PlaylistItemsDto {
        val response = client.get("$BASE_URL/playlistItems") {
            parameter("part", "snippet")
            parameter("playlistId", playlistId)
            parameter("maxResults", PAGE_SIZE)
            parameter("pageToken", pageToken)
            parameter("key", apiKey)
        }
        response.ensureSuccess(notFound = RoastError.PlaylistNotFound, badRequest = RoastError.Unauthorized)
        return response.body()
    }

    private companion object {
        const val BASE_URL = "https://www.googleapis.com/youtube/v3"
        const val PAGE_SIZE = 50
    }
}
