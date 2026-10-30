package com.roman.mydemoapp.roast.data

import com.roman.mydemoapp.BuildConfig
import com.roman.mydemoapp.network.HttpClientProvider
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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

/** Claude Messages API (api.anthropic.com). */
internal class ClaudeApi(
    private val client: HttpClient = HttpClientProvider.client,
    private val apiKey: String = BuildConfig.ANTHROPIC_API_KEY
) {
    /** Sends a single user turn and returns the concatenated text of the reply. */
    suspend fun complete(system: String, content: List<ContentBlock>): String {
        val response = client.post(URL) {
            header("x-api-key", apiKey)
            header("anthropic-version", API_VERSION)
            contentType(ContentType.Application.Json)
            setBody(
                MessagesRequest(
                    model = MODEL,
                    maxTokens = MAX_TOKENS,
                    system = system,
                    messages = listOf(Message(role = "user", content = content))
                )
            )
        }
        response.ensureSuccess()
        return response.body<MessagesResponse>().content
            .filter { it.type == "text" }
            .joinToString("") { it.text.orEmpty() }
    }

    private companion object {
        const val URL = "https://api.anthropic.com/v1/messages"
        const val API_VERSION = "2023-06-01"
        const val MODEL = "claude-sonnet-5-5"
        const val MAX_TOKENS = 1024
    }
}
