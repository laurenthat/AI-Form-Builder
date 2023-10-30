package com.sbma.linkup.connection

import androidx.lifecycle.ViewModel
import java.util.UUID

class ConnectionViewModel(
    private val repository: IConnectionRepository,
) : ViewModel() {
    fun allItemsStream(userId: UUID) = repository.getUserItemsStream(userId)
    fun getItemStream(id: String) = repository.getItemStream(id)

}