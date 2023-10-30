package com.sbma.linkup.tag

import kotlinx.coroutines.flow.Flow
import java.util.UUID

/**
 * Repository that provides insert, update, delete, and retrieve of Item from a given data source.
 */
interface ITagRepository {
    fun getAllItemsStream(): Flow<List<Tag>>
    fun getItemStream(id: UUID): Flow<Tag?>
    suspend fun insertItem(item: Tag)
    suspend fun upsertItem(item: Tag)
    suspend fun deleteItem(item: Tag)

    suspend fun updateItem(item: Tag)
}
