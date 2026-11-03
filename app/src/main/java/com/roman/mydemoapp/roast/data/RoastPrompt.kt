package com.roman.mydemoapp.roast.data

import com.roman.mydemoapp.roast.domain.model.Playlist
import com.roman.mydemoapp.settings.domain.model.RoastLevel

internal object RoastPrompt {
    const val SCREENSHOT_REQUEST = "Ось скріншот плейлиста. Прочитай треки та просмаж цей плейлист."

    fun system(level: RoastLevel, fromScreenshot: Boolean): String = """
        Ти — «Просмажка», AI, що жартівливо «просмажує» музичний смак за плейлистом.
        Відповідай українською. Жартуй лише про музичний смак і добірку треків, не зачіпай расу, релігію, зовнішність чи особисте життя.

        Тон: ${tone(level)}

        Напиши 3–5 речень вердикту (до 700 символів), згадай конкретні треки чи виконавців із плейлиста.
        Оціни музичний смак від 1 до 5 (5 — найкращий).

        Відповідай ЛИШЕ одним JSON-обʼєктом без markdown і пояснень:
        {"score": <1-5>, "text": "<вердикт>"${if (fromScreenshot) SCREENSHOT_FIELDS else ""}}
    """.trimIndent()

    fun playlistRequest(playlist: Playlist): String = buildString {
        appendLine("Плейлист «${playlist.title}», треків: ${playlist.trackCount}.")
        appendLine("Треки:")
        playlist.tracks.forEachIndexed { index, track ->
            val artist = track.artist.takeIf { it.isNotBlank() }?.let { " — $it" }.orEmpty()
            appendLine("${index + 1}. ${track.title}$artist")
        }
    }

    private const val SCREENSHOT_FIELDS =
        ", \"title\": \"<назва плейлиста зі скріншота або null>\", \"trackCount\": <кількість треків, яку видно, або null>, " +
            "\"notPlaylist\": <true, якщо на зображенні немає списку треків, інакше false>"

    private fun tone(level: RoastLevel): String = when (level) {
        RoastLevel.Rare -> "дуже м’який і добрий, лише ніжні жарти та похвала."
        RoastLevel.MediumRare -> "легкі дружні підколки."
        RoastLevel.Medium -> "чесно й з гумором, іронія без жорстокості."
        RoastLevel.MediumWell -> "гострий сарказм, без жалю."
        RoastLevel.WellDone -> "максимально жорстка, нищівна просмажка з чорним гумором."
    }
}
