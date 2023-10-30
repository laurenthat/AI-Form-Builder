package com.sbma.linkup.connection

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Connection(
    @PrimaryKey val id: UUID,
    @ColumnInfo val userId: UUID,
    @ColumnInfo val connectedUserId: UUID,
)

