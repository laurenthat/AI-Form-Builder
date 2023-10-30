package com.sbma.linkup.connection

import java.util.UUID

class ConnectionRepository(private val dao: ConnectionDao, private val connectionCardDao: ConnectionCardDao) : IConnectionRepository {
    override fun getItemStream(id: String) = dao.getItem(id)
    override fun getUserItemsStream(userId: UUID) = dao.getUserItems(userId)
    override suspend fun insertItem(item: Connection) = dao.insert(item)
    override suspend fun insertItem(item: List<Connection>) = dao.insert(item)
    override suspend fun syncUserConnections(userId: UUID, items: List<Connection>) = dao.syncUserConnections(userId, items)
    override suspend fun syncUserReverseConnections(userId: UUID, items: List<Connection>) = dao.syncReverseConnections(userId, items)
    override suspend fun deleteItem(item: Connection) = dao.delete(item)
    override suspend fun updateItem(item: Connection) = dao.update(item)
    override suspend fun syncConnectionCardItems(connectionId: UUID, items: List<ConnectionCard>) = connectionCardDao.syncConnectionCardItems(connectionId, items)

}