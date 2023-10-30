package com.sbma.linkup.connection

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.sbma.linkup.user.User
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface ConnectionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Connection)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: List<Connection>)

    @Update
    suspend fun update(item: Connection)

    @Query("DELETE FROM Connection WHERE userId = :userId AND id NOT IN (:idList)")
    suspend fun deleteNotInTheListUserConnections(userId: UUID, idList: List<UUID>)

    @Query("DELETE FROM Connection WHERE connectedUserId = :userId AND id NOT IN (:idList)")
    suspend fun deleteNotInTheListReverseUserConnections(userId: UUID, idList: List<UUID>)

    @Upsert
    suspend fun upsert(item: Connection)

    @Delete
    suspend fun delete(item: Connection)

    @Query("SELECT * from Connection WHERE id = :id")
    fun getItem(id: String): Flow<Connection?>

    @Query("SELECT * from Connection JOIN User ON user.id = Connection.connectedUserId where Connection.userId = :userId")
    fun getUserItems(userId: UUID): Flow<Map<Connection, User>>

    @Query("SELECT * from Connection where Connection.userId = :userId")
    fun getUserConnections(userId: UUID): Flow<List<Connection>>

    @Transaction
    suspend fun syncUserConnections(userId: UUID, items: List<Connection>) {
        val userItems = items.filter { it.userId == userId }
        insert(userItems)
        deleteNotInTheListUserConnections(userId, userItems.map { it.id })
    }

    @Transaction
    suspend fun syncReverseConnections(userId: UUID, items: List<Connection>) {
        val userItems = items.filter { it.connectedUserId == userId }
        insert(userItems)
        deleteNotInTheListReverseUserConnections(userId, userItems.map { it.id })
    }

}
