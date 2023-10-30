package com.sbma.linkup.tag

import java.util.UUID

class TagRepository(private val dao: TagDao) : ITagRepository {
    override suspend fun insertItem(item: Tag) = dao.insert(item)
    override suspend fun upsertItem(item: Tag) = dao.upsert(item)
    override suspend fun deleteItem(item: Tag) = dao.delete(item)
    override suspend fun updateItem(item: Tag) = dao.update(item)
    override fun getAllItemsStream() = dao.getAllItems()
    override fun getItemStream(id: UUID) = dao.getItem(id)

}