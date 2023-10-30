package com.sbma.linkup.connection

import com.sbma.linkup.user.User
import kotlinx.coroutines.flow.Flow
import java.util.UUID

/**
 * Repository that provides insert, update, delete, and retrieve of the item from a given data source.
 */
interface IConnectionRepository {
    fun getUserItemsStream(userId: UUID): Flow<Map<Connection, User>>
    fun getItemStream(id: String): Flow<Connection?>
    suspend fun insertItem(item: Connection)
    suspend fun insertItem(item: List<Connection>)

    suspend fun deleteItem(item: Connection)

    suspend fun updateItem(item: Connection)
    suspend fun syncConnectionCardItems(connectionId: UUID, items: List<ConnectionCard>)
    suspend fun syncUserConnections(userId: UUID, items: List<Connection>)
    suspend fun syncUserReverseConnections(userId: UUID, items: List<Connection>)

}
