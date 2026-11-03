package com.roman.mydemoapp.roast.data

import com.roman.mydemoapp.BuildConfig
import com.roman.mydemoapp.network.RetrofitProvider
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

@Serializable
internal data class MessagesRequest(
    val model: String,
    @SerialName("max_tokens") val maxTokens: Int,
    val system: String,
    val messages: List<Message>
)

@Serializable
internal data class Message(val role: String, val content: List<ContentBlock>)

@Serializable
internal data class ContentBlock(
    val type: String,
    val text: String? = null,
    val source: ImageSource? = null
) {
    companion object {
        fun text(text: String) = ContentBlock(type = "text", text = text)

        fun jpeg(base64: String) = ContentBlock(
            type = "image",
            source = ImageSource(type = "base64", mediaType = "image/jpeg", data = base64)
        )
    }
}

@Serializable
internal data class ImageSource(
    val type: String,
    @SerialName("media_type") val mediaType: String,
    val data: String
)

@Serializable
internal data class MessagesResponse(val content: List<ResponseBlock> = emptyList())

@Serializable
internal data class ResponseBlock(val type: String, val text: String? = null)

internal interface ClaudeService {
    @POST("v1/messages")
    suspend fun messages(
        @Header("x-api-key") apiKey: String,
        @Header("anthropic-version") version: String,
        @Body request: MessagesRequest
    ): Response<MessagesResponse>
}

/** Claude Messages API (api.anthropic.com). */
internal class ClaudeApi(
    private val service: ClaudeService = RetrofitProvider.create(BASE_URL, ClaudeService::class.java),
    private val apiKey: String = BuildConfig.ANTHROPIC_API_KEY
) {
    /** Sends a single user turn and returns the concatenated text of the reply. */
    suspend fun complete(system: String, content: List<ContentBlock>): String {
        val request = MessagesRequest(
            model = MODEL,
            maxTokens = MAX_TOKENS,
            system = system,
            messages = listOf(Message(role = "user", content = content))
        )
        return service.messages(apiKey, API_VERSION, request).requireBody().content
            .filter { it.type == "text" }
            .joinToString("") { it.text.orEmpty() }
    }

    private companion object {
        const val BASE_URL = "https://api.anthropic.com/"
        const val API_VERSION = "2023-06-01"
        const val MODEL = "claude-sonnet-5-5"
        const val MAX_TOKENS = 1024
    }
}
