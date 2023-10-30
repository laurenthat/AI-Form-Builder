package com.sbma.linkup.connection

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface ConnectionCardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ConnectionCard)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: List<ConnectionCard>)

    @Update
    suspend fun update(item: ConnectionCard)

    @Query("DELETE FROM ConnectionCard WHERE connectionId = :connectionId AND id NOT IN (:idList)")
    suspend fun deleteNotInTheListConnectionCardItems(connectionId: UUID, idList: List<UUID>)

    @Upsert
    suspend fun upsert(item: ConnectionCard)

    @Delete
    suspend fun delete(item: ConnectionCard)

    @Query("SELECT * from ConnectionCard WHERE id = :id")
    fun getItem(id: String): Flow<ConnectionCard?>

    @Transaction
    suspend fun syncConnectionCardItems(connectionId: UUID, items: List<ConnectionCard>) {
        val items = items.filter { it.connectionId == connectionId }
        insert(items)
        deleteNotInTheListConnectionCardItems(connectionId, items.map { it.id })
    }
}
