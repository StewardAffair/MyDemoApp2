package com.roman.mydemoapp.roast.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import com.roman.mydemoapp.roast.domain.model.RoastError
import com.roman.mydemoapp.roast.domain.model.RoastException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

private const val MAX_SIDE = 1568
private const val JPEG_QUALITY = 80

/** Reads a picked image, downscales it and returns it as base64 JPEG for the Claude vision API. */
internal class ScreenshotLoader(context: Context) {
    private val resolver = context.applicationContext.contentResolver

    suspend fun loadBase64Jpeg(uriString: String): String = withContext(Dispatchers.IO) {
        val uri = Uri.parse(uriString)
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        resolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, bounds) }

        var sample = 1
        while (maxOf(bounds.outWidth, bounds.outHeight) / (sample * 2) >= MAX_SIDE) sample *= 2
        val options = BitmapFactory.Options().apply { inSampleSize = sample }
        val decoded = resolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, options) }
            ?: throw RoastException(RoastError.Unknown)

        val scale = MAX_SIDE.toFloat() / maxOf(decoded.width, decoded.height)
        val bitmap = if (scale < 1f) {
            Bitmap.createScaledBitmap(decoded, (decoded.width * scale).toInt(), (decoded.height * scale).toInt(), true)
        } else {
            decoded
        }

        val bytes = ByteArrayOutputStream().also { bitmap.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, it) }
        Base64.encodeToString(bytes.toByteArray(), Base64.NO_WRAP)
    }
}
