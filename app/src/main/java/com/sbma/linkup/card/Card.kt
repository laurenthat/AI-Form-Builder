package com.sbma.linkup.card

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.UUID

/**
 * Serializable Added to support Android 6
 * And prevent java.lang.RuntimeException: Parcelable encountered IOException writing serializable object (name = kotlin.Pair)
 */
@Entity
data class Card(
    @PrimaryKey val id: UUID,
    @ColumnInfo val userId: UUID,
    @ColumnInfo val title: String,
    @ColumnInfo val value: String,
    @ColumnInfo val picture: String?,
): Serializable

