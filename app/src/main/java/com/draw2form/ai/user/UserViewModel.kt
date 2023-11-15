package com.draw2form.ai.user

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.draw2form.ai.ApiService
import com.draw2form.ai.datasource.DataStore
import com.draw2form.ai.toUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import timber.log.Timber
import java.io.File
import java.util.UUID

class UserViewModel(
    private val userRepository: IUserRepository,
    private val apiService: ApiService,
    private val dataStore: DataStore,
) : ViewModel() {
    private val responseStatus: MutableStateFlow<String?> = MutableStateFlow(null)


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
        val authorization = dataStore.getAuthorizationHeaderValue.first()
        authorization?.let {
            apiService.getProfile(authorization)
                .onSuccess { apiUser ->
                    // asSequence() is not necessary but improves performance

                    val user = apiUser.toUser()

                    Timber.d("Sync User Profile")
                    Timber.d("Sync User Cards")
                    Timber.d("Sync Started.")
                    userRepository.insertItem(user)
                    Timber.d("Sync Completed.")
                }.onFailure {
                    Timber.d(it)
                }

        }
    }

    suspend fun insertItem(user: User) = userRepository.insertItem(user)

    suspend fun saveLoginData(accessToken: String, expiresAt: String, userId: UUID) =
        dataStore.saveLoginData(accessToken = accessToken, expiresAt = expiresAt, userId = userId)

    suspend fun deleteLoginData() = dataStore.deleteLoginData()

    suspend fun setWelcomeScreenSeen() = dataStore.setWelcomeScreenSeen()

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

}



