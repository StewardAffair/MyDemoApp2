package com.roman.mydemoapp.roast.domain

private val hostRegex = Regex("""^https?://(www\.|m\.|music\.)?(youtube\.com|youtu\.be|google\.com)/""", RegexOption.IGNORE_CASE)
private val listRegex = Regex("""[?&]list=([A-Za-z0-9_-]+)""")

/** Returns the YouTube / YouTube Music playlist id from [url], or null if it isn't such a link. */
fun extractYoutubePlaylistId(url: String): String? {
    if (!hostRegex.containsMatchIn(url)) return null
    return listRegex.find(url)?.groupValues?.get(1)
}
