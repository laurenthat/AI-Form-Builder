package com.draw2form.ai.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.draw2form.ai.api.ApiForm
import com.draw2form.ai.api.ApiFormLabel
import com.draw2form.ai.api.ApiService
import com.draw2form.ai.api.ApiUploadedFileState
import com.draw2form.ai.api.toUser
import com.draw2form.ai.datasource.DataStore
import com.draw2form.ai.presentation.screens.UIComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant.Companion.parse
import okhttp3.MultipartBody
import timber.log.Timber
import java.util.UUID

class UserViewModel(
    private val userRepository: IUserRepository,
    private val apiService: ApiService,
    private val dataStore: DataStore,
) : ViewModel() {
    private val _apiUploadedFileState: MutableStateFlow<ApiUploadedFileState?> =
        MutableStateFlow(null)
    val apiUploadedFileState: StateFlow<ApiUploadedFileState?> get() = _apiUploadedFileState.asStateFlow()
    private val _apiUserForms: MutableStateFlow<List<ApiForm>> = MutableStateFlow(emptyList())
    val apiUserForms: StateFlow<List<ApiForm>> get() = _apiUserForms.asStateFlow()


    private val _apiUiElements: MutableStateFlow<List<UIComponent>?> = MutableStateFlow(null)
    val apiUiElements: StateFlow<List<UIComponent>?> get() = _apiUiElements.asStateFlow()

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
            apiService.getProfile(it)
                .onSuccess { apiUser ->
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

    suspend fun insertItem(user: User) = userRepository.insertItem(user)

    suspend fun saveLoginData(accessToken: String, expiresAt: String, userId: UUID) =
        dataStore.saveLoginData(accessToken = accessToken, expiresAt = expiresAt, userId = userId)

    suspend fun deleteLoginData() = dataStore.deleteLoginData()

    suspend fun setWelcomeScreenSeen() = dataStore.setWelcomeScreenSeen()

    fun getFormStatus(id: String) {
        viewModelScope.launch {
            val authorization = dataStore.getAuthorizationHeaderValue.first()
            authorization?.let {
                apiService.getFormUploadStatus(authorization, id)
                    .onSuccess {
                        Timber.d("Updating _apiUploadedFileState")
                        _apiUploadedFileState.value = it
                    }.onFailure {
                        Timber.d(it)
                    }
            }
        }
    }

    fun getFormDetails(id: String) {
        viewModelScope.launch {
            val authorization = dataStore.getAuthorizationHeaderValue.first()

            authorization?.let { token ->
                apiService.getFormDetails(
                    token,
                    id,
                ).onSuccess { form ->
                    val checkboxes = form.checkboxes ?: listOf()
                    val textFields = form.textFields ?: listOf()
                    val toggleSwitches = form.toggleSwitches ?: listOf()
                    val buttons = form.buttons ?: listOf()
                    val labels = form.labels ?: listOf()
                    val images = form.images ?: listOf()
                    var uiElements = mutableListOf<UIComponent>()
                    uiElements.addAll(checkboxes.map { UIComponent(checkbox = it) })
                    uiElements.addAll(textFields.map { UIComponent(textField = it) })
                    uiElements.addAll(toggleSwitches.map { UIComponent(toggleSwitch = it) })
                    uiElements.addAll(buttons.map { UIComponent(button = it) })
                    uiElements.addAll(labels.map { UIComponent(label = it) })
                    uiElements.addAll(images.map { UIComponent(image = it) })
                    println(uiElements)
//                    uiElements.sortBy {
////                        it.order
//                    }
                    _apiUiElements.value = uiElements

                }.onFailure {
                    println(it)
                }
            }
        }
    }

    fun uploadFormImage(
        imgFile: MultipartBody.Part,
        onSuccess: (form: ApiForm) -> Unit
    ) {
        viewModelScope.launch {
            val authorization = dataStore.getAuthorizationHeaderValue.first()
            authorization?.let {
                apiService.createForm(authorization, imgFile)
                    .onSuccess {
                        Timber.d(it.toString())
                        onSuccess(it)
                    }.onFailure {
                        Timber.d(it)
                    }
            }
        }
    }

    fun getUserForms() {
        viewModelScope.launch {
            val authorization = dataStore.getAuthorizationHeaderValue.first()
            authorization?.let {
                apiService.getForms(authorization)
                    .onSuccess {
                        _apiUserForms.value =
                            it.sortedBy { parse(it.createdAt).epochSeconds }.reversed()
                    }
                    .onFailure {
                        Timber.d(it)
                    }
            }
        }
    }

    fun updateFormLabel(formLabel: ApiFormLabel) {
        viewModelScope.launch {
            val authorization = dataStore.getAuthorizationHeaderValue.first()
            authorization?.let {
                apiService.updateFormLabel(
                    authorization,
                    formId = formLabel.formId,
                    id = formLabel.id,
                    newFormLabel = formLabel
                )
                    .onSuccess {
                        getFormDetails(formLabel.formId)
                    }
                    .onFailure {
                        Timber.d(it)
                    }
            }
        }
    }

    suspend fun formShareId(id: String) {
        viewModelScope.launch {
            val authorization = dataStore.getAuthorizationHeaderValue.first()
            authorization?.let {
                apiService.formShare(
                    authorization,
                    id
                )
                    .onSuccess {
                        Timber.d(it.toString())
                    }.onFailure {
                        println(it)
                    }
            }
        }
    }

    fun deleteFormItem(form: ApiForm) {
        val formsList = _apiUserForms.value
        val formsCopy = formsList.toMutableList()
        formsCopy.remove(form)
        _apiUserForms.value = formsCopy

        viewModelScope.launch {
            val authorization = dataStore.getAuthorizationHeaderValue.first()
            authorization?.let {
                apiService.deleteForm(authorization, form.id)
                    .onSuccess { response ->
                        Timber.d("Deleted form item")
                        Timber.d(response.toString())
                    }.onFailure {
                        Timber.d(it)
                    }
            }

        }

    }

}



