package com.sbma.linkup.card

import java.util.UUID

class CardRepository(private val dao: CardDao) : ICardRepository {
    override suspend fun insertItem(item: Card) = dao.insert(item)
    override suspend fun deleteItem(item: Card) = dao.delete(item)
    override suspend fun updateItem(item: Card) = dao.update(item)
    override suspend fun syncUserItems(userId: UUID, items: List<Card>) = dao.syncUserItems(userId, items)
    override fun getUserItemsStream(userId: UUID) = dao.getUserItems(userId)
    override fun getItemStream(id: UUID) = dao.getItem(id)

}