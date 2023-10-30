package com.sbma.linkup.card

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sbma.linkup.api.ApiService
import com.sbma.linkup.api.apimodels.toApiCard
import com.sbma.linkup.api.apimodels.toCard
import com.sbma.linkup.datasource.DataStore
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.UUID

class CardViewModel(
    private val repository: ICardRepository,
    private val dataStore: DataStore,
    private val apiService: ApiService
) : ViewModel() {
    fun allItemsStream(userId: UUID) = repository.getUserItemsStream(userId)
    fun getItemStream(id: UUID) = repository.getItemStream(id)
    suspend fun insertItem(item: Card) = repository.insertItem(item)
    private suspend fun deleteFromLocalDatabase(item: Card) = repository.deleteItem(item)

    suspend fun saveItem(card: Card): Job {
        // Example code of how Api works.
        return viewModelScope.launch {
            val authorization = dataStore.getAuthorizationHeaderValue.first()
            authorization?.let {
                apiService.createNewCard(
                    authorization,
                    card.toApiCard()
                )
                    .onSuccess { response ->
                        Timber.d("create new Card")
                        Timber.d(response.toString())
                        insertItem(response.toCard())
                    }.onFailure {
                        Timber.d(it)
                    }
            }
        }
    }

    suspend fun updateItem(card: Card): Job {
        // Example code of how Api works.
        return viewModelScope.launch {
            val authorization = dataStore.getAuthorizationHeaderValue.first()
            authorization?.let {
                apiService.updateCard(authorization, card.id.toString(), card.toApiCard())
                    .onSuccess { response ->
                        Timber.d("update new Card")
                        Timber.d(response.toString())
                        insertItem(response.toCard())
                    }.onFailure {
                        Timber.d(it)
                    }
            }
        }
    }

    suspend fun deleteItem(card: Card): Job {
        // Example code of how Api works.
        return viewModelScope.launch {
            val authorization = dataStore.getAuthorizationHeaderValue.first()
            authorization?.let {
                apiService.deleteCard(authorization, card.id.toString())
                    .onSuccess { response ->
                        Timber.d("delete new Card")
                        Timber.d(response.toString())
                        deleteFromLocalDatabase(card)
                    }.onFailure {
                        Timber.d(it)
                    }
            }
        }
    }
}