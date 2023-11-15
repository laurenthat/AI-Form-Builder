package com.draw2form.ai.user

import kotlinx.coroutines.flow.Flow
import java.util.UUID

/**
 * Repository that provides insert, update, delete, and retrieve of Item from a given data source.
 */
interface IUserRepository {
    fun getAllItemsStream(): Flow<List<User>>
    fun getItemStream(id: UUID): Flow<User?>
    suspend fun insertItem(item: User)
    suspend fun insertItemList(item: List<User>)
    suspend fun upsertItem(item: User)
    suspend fun deleteItem(item: User)

    suspend fun updateItem(item: User)
}
