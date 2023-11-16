package com.draw2form.ai.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.draw2form.ai.api.ApiService
import com.draw2form.ai.api.toUser
import com.draw2form.ai.datasource.DataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.UUID
import java.util.concurrent.TimeUnit

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


    fun uploadFormImage(imgFile: MultipartBody.Part) {
        viewModelScope.launch {
            val authorization = dataStore.getAuthorizationHeaderValue.first()
            authorization?.let {
                try {
                    val retrofit = createRetrofitInstance()
                    val apiService = retrofit.create(ApiService::class.java)

                    val response = apiService.uploadImage(authorization, imgFile)

                    if (response.isSuccess) {
                        Timber.d(response.toString())
                    } else {

                        responseStatus.value = "Oh no! Contact retrieval has failed"
                    }
                } catch (t: Throwable) {

                    responseStatus.value = "Oh no! Contact retrieval has failed: $t"
                }
            }
        }
    }

    private suspend fun createRetrofitInstance(): Retrofit {
        return withContext(Dispatchers.IO) {
            Retrofit.Builder()
                .baseUrl("https://draw2form.ericaskari.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(
                    OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .build()
                )
                .build()
        }
    }


}



