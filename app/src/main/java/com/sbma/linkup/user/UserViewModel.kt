package com.sbma.linkup.user

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sbma.linkup.api.ApiService
import com.sbma.linkup.api.apimodels.ApiConnection
import com.sbma.linkup.api.apimodels.AssignTagRequest
import com.sbma.linkup.card.ICardRepository
import com.sbma.linkup.connection.IConnectionRepository
import com.sbma.linkup.datasource.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import timber.log.Timber
import java.io.File
import java.util.UUID

class UserViewModel(
    private val userRepository: IUserRepository,
    private val cardRepository: ICardRepository,
    private val userConnectionRepository: IConnectionRepository,
    private val apiService: ApiService,
    private val dataStore: DataStore,
) : ViewModel() {
    fun getItemStream(id: UUID) = userRepository.getItemStream(id)
    private val responseStatus: MutableStateFlow<String?> = MutableStateFlow(null)
    private val assignTagResponseStatus: MutableStateFlow<String?> = MutableStateFlow(null)


    /**
     * combines two flows together. here it combines userId and list of users and returns the user with that id.
     * we used list of user with one element to distinguish between flow not having data and not having user.
     * (null) = when it is used with collectAsState(initial = null) means that there is no data in the flow yet so we
     * cannot know user status.
     * (Empty list) = there is no user with this id or id is null
     */
    val getLoggedInUserProfile: Flow<List<User>> =
        dataStore.getUserId.combine(userRepository.getAllItemsStream()) { userId, users ->
            userId?.let { id ->
                val user = users.find { it.id == id }
                if (user == null) {
                    listOf()
                } else {
                    listOf(user)
                }
            } ?: listOf()
        }

    val welcomeScreenSeen: Flow<Boolean> =
        dataStore.getWelcomeScreenSeen

    suspend fun syncRoomDatabase() {
        // Example code of how Api works.
/*        val authorization = dataStore.getAuthorizationHeaderValue.first()
        authorization?.let {
            apiService.getProfile(authorization)
                .onSuccess { apiUser ->
                    // asSequence() is not necessary but improves performance

                    val user = apiUser.toUser()
                    val cards: List<Card> = (apiUser.cards ?: listOf()).toCardList()
                    val connections = apiUser.connections ?: listOf()
                    val connectionsCards =
                        connections.asSequence().mapNotNull { it.connectionCards }.flatten().groupBy { it.connectionId }.toList()
                    val connectionsUsers = connections.mapNotNull { it.user }
                    val connectionsConnectedUsers = connections.mapNotNull { it.connectedUser }
                    val connectionsCardsCards =
                        connections.asSequence().mapNotNull { it.connectionCards }.flatten().mapNotNull { it.card }.groupBy { it.ownerId }
                            .toList()

                    Timber.d("Sync User Profile")
                    Timber.d("Sync User Cards")
                    Timber.d("Sync Connection Items.          count: ${connections.count()}")
                    Timber.d("Sync Connection User Items.     count: ${connectionsUsers.count()}")
                    Timber.d("Sync ConnectionCard Items.      count: ${connectionsCards.count()}")
                    Timber.d("Sync ConnectionCard Card Items. count: ${connectionsCardsCards.count()}")
                    Timber.d("Sync Started.")
                    cardRepository.syncUserItems(user.id, cards)
                    userRepository.insertItem(user)
                    userConnectionRepository.syncUserConnections(user.id, connections.toConnectionList())
                    connectionsUsers.toUserList().forEach {
                        userRepository.insertItem(it)
                    }
                    connectionsConnectedUsers.toUserList().forEach {
                        userRepository.insertItem(it)
                    }
                    connectionsCards.forEach {
                        userConnectionRepository.syncConnectionCardItems(UUID.fromString(it.first), it.second.toConnectionCardList())
                    }
                    connectionsCardsCards.forEach {
                        cardRepository.syncUserItems(UUID.fromString(it.first), it.second.toCardList())
                    }
                    Timber.d("Sync Completed.")
                }.onFailure {
                    Timber.d(it)
                }

        }*/
    }

    suspend fun insertItem(user: User) = userRepository.insertItem(user)

    suspend fun saveLoginData(accessToken: String, expiresAt: String, userId: UUID) =
        dataStore.saveLoginData(accessToken = accessToken, expiresAt = expiresAt, userId = userId)

    suspend fun deleteLoginData() = dataStore.deleteLoginData()

    suspend fun setWelcomeScreenSeen() = dataStore.setWelcomeScreenSeen()

    suspend fun assignTag(id: String, shareId: String) {
        viewModelScope.launch {
            val authorization = dataStore.getAuthorizationHeaderValue.first()
            authorization?.let {
                apiService.assignTag(
                    authorization,
                    AssignTagRequest(shareId, id)
                )
                    .onSuccess { response ->
                        Timber.d("assignTag")
                        Timber.d(response.toString())
                        assignTagResponseStatus.value =
                            "Congratulations! You successfully added your details on the NFC-enabled card."
                    }.onFailure {
                        Timber.d(it)
                        assignTagResponseStatus.value = "Oh no! Something went wrong."
                    }
            }
        }
    }

    suspend fun scanTag(id: String) {
        viewModelScope.launch {
            val authorization = dataStore.getAuthorizationHeaderValue.first()
            authorization?.let {
                apiService.scanTag(
                    authorization,
                    id
                )
                    .onSuccess { response ->
                        Timber.d("receiveTag")
                        Timber.d(response.toString())
                        syncRoomDatabase()
                        responseStatus.value =
                            "Congratulations! Contact has been successfully added."

                    }.onFailure {
                        Timber.d(it)
                        responseStatus.value = "Oh no! Contact retrieval has failed"
                    }
            }
        }
    }
    suspend fun uploadFormImage(imgFile: File) {
        viewModelScope.launch {

                val authorization = dataStore.getAuthorizationHeaderValue.first()
                authorization?.let {
                    apiService.uploadImage(
                        authorization,
                        MultipartBody.Part.createFormData("image", imgFile.name)
                    )
                        .onSuccess { response ->
                            Log.d("DEBUG", "In APi uploadForm image, file upload successful")
                            Timber.d("uploading Image")
                            Timber.d(response.toString())
                        }
                        .onFailure {

                            Timber.d(it)
                            Log.d("DEBUG", "In APi uploadForm image, file upload failed: $it")
                            responseStatus.value = "Oh no! Contact retrieval has failed"
                        }
                }
        }
    }

    suspend fun scanQRCode(id: String, onScanResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            scanShareId(
                UUID.fromString(id),
                onSuccess = {
                    responseStatus.value = "Congratulations! Contact has been successfully added."
                    Timber.d("Scanning Qr code")
                    onScanResult(true)
                },
                onFailure = {
                    onScanResult(false)
                }
            )
        }
    }

    suspend fun scanShareId(id: UUID, onSuccess: (apiConnection: ApiConnection) -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch {
            val authorization = dataStore.getAuthorizationHeaderValue.first()
            authorization?.let {
                apiService.scanShare(
                    authorization,
                    id.toString()
                )
                    .onSuccess { response ->
                        Timber.d("scanShareId -> onSuccess")
                        syncRoomDatabase()
                        Timber.d(response.toString())
                        onSuccess(response)

                    }.onFailure {
                        onFailure()
                        Timber.d("scanShareId -> onFailure")
                        Timber.d(it)
                    }
            }
        }
    }



    fun observeNfcStatus(): StateFlow<String?> {
        return responseStatus.asStateFlow()
    }

    fun assignNfcStatus(): StateFlow<String?> {
        return assignTagResponseStatus.asStateFlow()
    }
}



