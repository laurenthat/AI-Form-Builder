package com.sbma.linkup.tag

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Tag(
    @PrimaryKey val id: UUID,
    @ColumnInfo val shareId: String,
    @ColumnInfo val tagId: String
)

