package com.roman.mydemoapp.navigation

import android.content.Intent

private val urlRegex = Regex("https?://\\S+")

/** Extracts a playlist URL from a share (ACTION_SEND) or deep link (ACTION_VIEW) intent. */
fun Intent.extractPlaylistLink(): String? = when (action) {
    Intent.ACTION_SEND -> getStringExtra(Intent.EXTRA_TEXT)?.let { urlRegex.find(it)?.value }
    Intent.ACTION_VIEW -> dataString
    else -> null
}
