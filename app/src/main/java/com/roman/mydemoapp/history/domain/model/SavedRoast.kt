package com.roman.mydemoapp.history.domain.model

import com.roman.mydemoapp.roast.domain.model.RoastedPlaylist

/** A roast persisted in history; [id] is 0 until it is first saved. [sourceRef] is the playlist URL, null for screenshots. */
data class SavedRoast(
    val id: Long = 0,
    val roasted: RoastedPlaylist,
    val fromScreenshot: Boolean,
    val sourceRef: String?,
    val createdAt: Long
)
