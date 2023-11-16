package com.draw2form.ai.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.draw2form.ai.api.ApiFormButton
import com.draw2form.ai.api.ApiFormCheckbox
import com.draw2form.ai.api.ApiFormImage
import com.draw2form.ai.api.ApiFormLabel
import com.draw2form.ai.api.ApiFormTextField
import com.draw2form.ai.api.ApiFormToggleSwitch
import com.draw2form.ai.api.ApiService
import com.draw2form.ai.api.ApiUploadedFile
import com.draw2form.ai.api.ApiUploadedFileState
import com.draw2form.ai.api.toUser
import com.draw2form.ai.datasource.DataStore
import com.draw2form.ai.presentation.screens.UIElement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import timber.log.Timber
import java.util.UUID

class UserViewModel(
    private val userRepository: IUserRepository,
    private val apiService: ApiService,
    private val dataStore: DataStore,
) : ViewModel() {
    private val _apiUploadedFileState: MutableStateFlow<ApiUploadedFileState?> = MutableStateFlow(null)
    val apiUploadedFileState: StateFlow<ApiUploadedFileState?> get() = _apiUploadedFileState.asStateFlow()


    private val _apiUiElements: MutableStateFlow<List<UIElement>?> = MutableStateFlow(null)
    val apiUiElements: StateFlow<List<UIElement>?> get() = _apiUiElements.asStateFlow()

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
    }

    suspend fun insertItem(user: User) = userRepository.insertItem(user)

    suspend fun saveLoginData(accessToken: String, expiresAt: String, userId: UUID) =
        dataStore.saveLoginData(accessToken = accessToken, expiresAt = expiresAt, userId = userId)

    suspend fun deleteLoginData() = dataStore.deleteLoginData()

    suspend fun setWelcomeScreenSeen() = dataStore.setWelcomeScreenSeen()

    fun getUploadedFileState(id: String) {
        viewModelScope.launch {
            val authorization = dataStore.getAuthorizationHeaderValue.first()
            authorization?.let {
                apiService.getUploadState(authorization, id)
                    .onSuccess {
                        Timber.d("Updating _apiUploadedFileState")
                        _apiUploadedFileState.value = it
                    }.onFailure {
                        Timber.d(it)
                    }
            }
        }
    }

    fun getUploadedFileDetails(id: String) {
        viewModelScope.launch {
            val authorization = dataStore.getAuthorizationHeaderValue.first()

            authorization?.let {
                apiService.getUploadDetails(it, id).onSuccess {
                    println(it)
                    val formData = it.events?.find {
                        it.event == "STRUCTURE_GENERATION_COMPLETED"
                    }
                    val parsedFormData: List<UIElement> = formData?.parsedPayload?.map {
                        if (it is List<*>) {
                            val name: String = it[0] as String
                            val data = it[1]

                            if (data is Map<*, *>) {
                                val order = (data.get("order") as Double).toInt()
                                val label = (data.get("label") as? String) ?: ""

                                val uiElement = when (name) {
                                    "FormLabel" -> ApiFormLabel("", null, "", order, label)
                                    "FormImage" -> ApiFormImage("", null, "", order, "")
                                    "FormTextField" -> ApiFormTextField(
                                        "",
                                        null,
                                        "",
                                        label,
                                        order,
                                        null
                                    )

                                    "FormCheckbox" -> ApiFormCheckbox("", label, null, "", order, null)
                                    "FormToggleSwitch" -> ApiFormToggleSwitch(
                                        "",
                                        label,
                                        null,
                                        "",
                                        order,
                                        null
                                    )

                                    "FormButton" -> ApiFormButton("", label, null, "", order, "")
                                    else -> null
                                }
                                return@map uiElement
                            } else {
                                return@map null
                            }
                        } else {
                            return@map null
                        }

                    }?.filterNotNull() ?: listOf()

                    _apiUiElements.value = parsedFormData
                }.onFailure {
                    println(it)
                }
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

                        Timber.d("Sync User Profile Failed")
                        Timber.d(it)
                    }

            }
        }
    }

    fun uploadFormImage(
        imgFile: MultipartBody.Part,
        onSuccess: (uploadedFile: ApiUploadedFile) -> Unit
    ) {
        viewModelScope.launch {
            val authorization = dataStore.getAuthorizationHeaderValue.first()
            authorization?.let {
                apiService.uploadImage(authorization, imgFile)
                    .onSuccess {
                        Timber.d(it.toString())
                        onSuccess(it)
                    }.onFailure {
                        Timber.d(it)
                    }
            }
        }
    }
}



