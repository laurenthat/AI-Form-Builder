package com.sbma.linkup.connection

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class ConnectionCard(
    @PrimaryKey val id: UUID,
    @ColumnInfo val cardId: UUID,
    @ColumnInfo val connectionId: UUID,
)

