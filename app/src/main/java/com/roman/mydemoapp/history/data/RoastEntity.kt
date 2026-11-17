package com.roman.mydemoapp.history.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/** [level] is stored as the `RoastLevel` name so reordering the enum can't corrupt saved rows. */
@Entity(tableName = "roasts")
data class RoastEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String?,
    val trackCount: Int?,
    val level: String,
    val score: Int,
    val text: String,
    val fromScreenshot: Boolean,
    val sourceRef: String?,
    val createdAt: Long
)
