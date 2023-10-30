package com.sbma.linkup.card

import kotlinx.coroutines.flow.Flow
import java.util.UUID

/**
 * Repository that provides insert, update, delete, and retrieve of the item from a given data source.
 */
interface ICardRepository {
    fun getUserItemsStream(userId: UUID): Flow<List<Card>>
    fun getItemStream(id: UUID): Flow<Card?>
    suspend fun insertItem(item: Card)
    suspend fun deleteItem(item: Card)
    suspend fun updateItem(item: Card)
    suspend fun syncUserItems(userId: UUID, items: List<Card>)
}
