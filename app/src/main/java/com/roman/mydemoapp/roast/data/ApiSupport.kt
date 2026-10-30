package com.roman.mydemoapp.roast.data

import com.roman.mydemoapp.roast.domain.model.RoastError
import com.roman.mydemoapp.roast.domain.model.RoastException
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import java.io.IOException

/** Runs [block], translating transport and parsing failures into [RoastException]. */
internal suspend fun <T> apiCall(block: suspend () -> T): T = try {
    block()
} catch (e: RoastException) {
    throw e
} catch (e: CancellationException) {
    throw e
} catch (e: IOException) {
    throw RoastException(RoastError.NoNetwork, e)
} catch (e: SerializationException) {
    throw RoastException(RoastError.BadResponse, e)
} catch (e: Exception) {
    throw RoastException(RoastError.Unknown, e)
}

/** Throws a [RoastException] for non-2xx responses; [notFound] and [badRequest] let each API pick the meaning. */
internal fun HttpResponse.ensureSuccess(
    notFound: RoastError = RoastError.Unknown,
    badRequest: RoastError = RoastError.BadResponse
) {
    if (status.isSuccess()) return
    throw RoastException(
        when (status.value) {
            400 -> badRequest
            401, 403 -> RoastError.Unauthorized
            404 -> notFound
            429, 529 -> RoastError.RateLimited
            else -> RoastError.Unknown
        }
    )
}
