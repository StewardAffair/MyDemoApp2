package com.roman.mydemoapp.roast.data

import android.content.Context
import com.roman.mydemoapp.roast.domain.model.Playlist
import com.roman.mydemoapp.roast.domain.model.RoastError
import com.roman.mydemoapp.roast.domain.model.RoastException
import com.roman.mydemoapp.roast.domain.model.RoastResult
import com.roman.mydemoapp.roast.domain.repository.RoastRepository
import com.roman.mydemoapp.settings.domain.model.RoastLevel
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
private data class RoastJson(
    val score: Int,
    val text: String,
    val title: String? = null,
    val trackCount: Int? = null,
    val notPlaylist: Boolean = false
)

private val json = Json { ignoreUnknownKeys = true }

class RoastRepositoryImpl internal constructor(
    private val api: ClaudeApi,
    private val screenshotLoader: ScreenshotLoader
) : RoastRepository {
    constructor(context: Context) : this(ClaudeApi(), ScreenshotLoader(context))

    override suspend fun roast(playlist: Playlist, level: RoastLevel): RoastResult = apiCall {
        val reply = api.complete(
            system = RoastPrompt.system(level, fromScreenshot = false),
            content = listOf(ContentBlock.text(RoastPrompt.playlistRequest(playlist)))
        )
        parse(reply)
    }

    override suspend fun roastScreenshot(imageUri: String, level: RoastLevel): RoastResult = apiCall {
        val image = screenshotLoader.loadBase64Jpeg(imageUri)
        val reply = api.complete(
            system = RoastPrompt.system(level, fromScreenshot = true),
            content = listOf(ContentBlock.jpeg(image), ContentBlock.text(RoastPrompt.SCREENSHOT_REQUEST))
        )
        parse(reply)
    }

    private fun parse(reply: String): RoastResult {
        val start = reply.indexOf('{')
        val end = reply.lastIndexOf('}')
        if (start < 0 || end < start) throw RoastException(RoastError.BadResponse)
        val parsed = json.decodeFromString<RoastJson>(reply.substring(start, end + 1))
        if (parsed.notPlaylist) throw RoastException(RoastError.NotAPlaylist)
        return RoastResult(
            score = parsed.score.coerceIn(1, 5),
            text = parsed.text.trim(),
            title = parsed.title?.takeIf { it.isNotBlank() },
            trackCount = parsed.trackCount
        )
    }
}
