package com.draw2form.ai.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.draw2form.ai.api.ApiForm
import com.draw2form.ai.api.ApiFormButton
import com.draw2form.ai.api.ApiFormCheckbox
import com.draw2form.ai.api.ApiFormCheckboxResponse
import com.draw2form.ai.api.ApiFormImage
import com.draw2form.ai.api.ApiFormLabel
import com.draw2form.ai.api.ApiFormSubmission
import com.draw2form.ai.api.ApiFormTextField
import com.draw2form.ai.api.ApiFormTextFieldResponse
import com.draw2form.ai.api.ApiFormToggleSwitch
import com.draw2form.ai.api.ApiFormToggleSwitchResponse
import com.draw2form.ai.api.ApiService
import com.draw2form.ai.api.ApiUploadedFileState
import com.draw2form.ai.api.NewFormSubmissionRequestBody
import com.draw2form.ai.api.toUser
import com.draw2form.ai.datasource.DataStore
import com.draw2form.ai.models.Direction
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

    private val _submittedForms: MutableStateFlow<List<ApiFormSubmission>> = MutableStateFlow(
        emptyList()
    )

    val apiSubmittedForms: StateFlow<List<ApiFormSubmission>> = _submittedForms.asStateFlow()

    val apiUserForms: StateFlow<List<ApiForm>> get() = _apiUserForms.asStateFlow()


    val scannedForm: MutableStateFlow<Pair<ApiForm, List<UIComponent>>?> = MutableStateFlow(null)


    private val _apiUiElements: MutableStateFlow<List<UIComponent>?> = MutableStateFlow(null)
    val apiUiElements: StateFlow<List<UIComponent>?> get() = _apiUiElements.asStateFlow()


    fun onScannedFormUpdated(updatedList: List<UIComponent>) {
        scannedForm.value?.let {
            scannedForm.value = it.copy(
                second = updatedList
            )
        }
    }


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

    private fun convertApiFormToUIComponents(apiForm: ApiForm): List<UIComponent> {
        val checkboxes = apiForm.checkboxes ?: listOf()
        val textFields = apiForm.textFields ?: listOf()
        val toggleSwitches = apiForm.toggleSwitches ?: listOf()
        val buttons = apiForm.buttons ?: listOf()
        val labels = apiForm.labels ?: listOf()
        val images = apiForm.images ?: listOf()

        val uiElements = mutableListOf<UIComponent>()

        uiElements.addAll(checkboxes.map {
            UIComponent(
                checkbox = it,
                checkboxResponse = ApiFormCheckboxResponse("", null, "", null, it.id, true)
            )
        })
        uiElements.addAll(textFields.map {
            UIComponent(
                textField = it,
                textFieldResponse = ApiFormTextFieldResponse("", null, "", null, it.id, "")
            )
        })
        uiElements.addAll(toggleSwitches.map {
            UIComponent(
                toggleSwitch = it,
                toggleSwitchResponse = ApiFormToggleSwitchResponse("", null, "", null, it.id, true)
            )
        })
        uiElements.addAll(buttons.map { UIComponent(button = it) })
        uiElements.addAll(labels.map { UIComponent(label = it) })
        uiElements.addAll(images.map { UIComponent(image = it) })
        uiElements.sortBy { it.order() }

        return uiElements
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
                    uiElements.sortBy {
                        it.order()
                    }
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

    fun getSubmittedForms(id: String) {
        viewModelScope.launch {
            val authorization = dataStore.getAuthorizationHeaderValue.first()
            authorization?.let {
                apiService.getFormDetails(authorization, id)
                    .onSuccess { form ->
                        _submittedForms.value = form.formSubmissions ?: emptyList()
                    }.onFailure {
                        println(it)
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

    fun deleteFormLabel(formLabel: ApiFormLabel) {
        viewModelScope.launch {
            val authorization = dataStore.getAuthorizationHeaderValue.first()
            authorization?.let {
                apiService.deleteFormLabel(
                    authorization,
                    formId = formLabel.formId,
                    id = formLabel.id,
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

    suspend fun publishForm(id: String, onSuccess: (form: ApiForm) -> Unit) {
        viewModelScope.launch {
            val authorization = dataStore.getAuthorizationHeaderValue.first()
            authorization?.let {
                apiService.formShare(
                    authorization,
                    id
                )
                    .onSuccess {
                        onSuccess(it)
                        Timber.d("Success result: $it")

                    }.onFailure {
                        Timber.d("Failed result: $it")
                        println(it)
                    }
            }
        }
    }

    suspend fun getForm(id: String) {
        viewModelScope.launch {

            val authorization = dataStore.getAuthorizationHeaderValue.first()
            authorization?.let {
                apiService.getFormDetails(
                    authorization,
                    id
                )
                    .onSuccess {
                        scannedForm.value = Pair(it, convertApiFormToUIComponents(it))
                        Timber.d(it.toString())
                    }.onFailure {
                        println(it)
                    }
            }
        }
    }

    fun submitForm(
        formId: String,
        formBody: NewFormSubmissionRequestBody,
        onResult: (Boolean) -> Unit
    ) {
        Timber.d("Form Id: $formId, formBody: $formBody")
        viewModelScope.launch {

            val authorization = dataStore.getAuthorizationHeaderValue.first()

            apiService.submitFormApi(
                authorization,
                formId,
                formBody,
            )
                .onSuccess {
                    onResult(true)
                    Timber.d("Success result: $it")
                }.onFailure {
                    onResult(false)
                    Timber.d("Success result: $it")
                    println(it)
                }
        }

    }

    fun updateFormTextField(textField: ApiFormTextField) {
        viewModelScope.launch {
            val authorization = dataStore.getAuthorizationHeaderValue.first()
            authorization?.let {
                apiService.updateFormTextField(
                    authorization,
                    formId = textField.formId,
                    id = textField.id,
                    newFormTextField = textField
                )
                    .onSuccess {
                        getFormDetails(textField.formId)
                    }
                    .onFailure {
                        Timber.d(it)
                    }
            }
        }
    }

    fun deleteFormTextField(textField: ApiFormTextField) {
        viewModelScope.launch {
            val authorization = dataStore.getAuthorizationHeaderValue.first()
            authorization?.let {
                apiService.deleteFormTextField(
                    authorization,
                    formId = textField.formId,
                    id = textField.id,
                )
                    .onSuccess {
                        getFormDetails(textField.formId)
                    }
                    .onFailure {
                        Timber.d(it)
                    }
            }
        }
    }

    fun updateFormCheckbox(checkbox: ApiFormCheckbox) {
        viewModelScope.launch {
            val authorization = dataStore.getAuthorizationHeaderValue.first()
            authorization?.let {
                apiService.updateFormCheckbox(
                    authorization,
                    formId = checkbox.formId,
                    id = checkbox.id,
                    newFormCheckbox = checkbox
                )
                    .onSuccess {
                        getFormDetails(checkbox.formId)
                    }
                    .onFailure {
                        Timber.d(it)
                    }
            }
        }
    }

    fun deleteFormCheckbox(checkbox: ApiFormCheckbox) {
        viewModelScope.launch {
            val authorization = dataStore.getAuthorizationHeaderValue.first()
            authorization?.let {
                apiService.deleteFormCheckbox(
                    authorization,
                    formId = checkbox.formId,
                    id = checkbox.id,
                )
                    .onSuccess {
                        getFormDetails(checkbox.formId)
                    }
                    .onFailure {
                        Timber.d(it)
                    }
            }
        }
    }

    fun updateFormToggleSwitch(toggleSwitch: ApiFormToggleSwitch) {
        viewModelScope.launch {
            val authorization = dataStore.getAuthorizationHeaderValue.first()
            authorization?.let {
                apiService.updateFormToggleSwitch(
                    authorization,
                    formId = toggleSwitch.formId,
                    id = toggleSwitch.id,
                    newFormToggleSwitch = toggleSwitch
                )
                    .onSuccess {
                        getFormDetails(toggleSwitch.formId)
                    }
                    .onFailure {
                        Timber.d(it)
                    }
            }
        }
    }

    fun deleteFormToggleSwitch(toggleSwitch: ApiFormToggleSwitch) {
        viewModelScope.launch {
            val authorization = dataStore.getAuthorizationHeaderValue.first()
            authorization?.let {
                apiService.deleteFormToggleSwitch(
                    authorization,
                    formId = toggleSwitch.formId,
                    id = toggleSwitch.id,
                )
                    .onSuccess {
                        getFormDetails(toggleSwitch.formId)
                    }
                    .onFailure {
                        Timber.d(it)
                    }
            }
        }
    }

    fun updateFormButton(button: ApiFormButton) {
        viewModelScope.launch {
            val authorization = dataStore.getAuthorizationHeaderValue.first()
            authorization?.let {
                apiService.updateFormButton(
                    authorization,
                    formId = button.formId,
                    id = button.id,
                    newFormButton = button
                )
                    .onSuccess {
                        getFormDetails(button.formId)
                    }
                    .onFailure {
                        Timber.d(it)
                    }
            }
        }
    }

    fun deleteFormButton(button: ApiFormButton) {
        viewModelScope.launch {
            val authorization = dataStore.getAuthorizationHeaderValue.first()
            authorization?.let {
                apiService.deleteFormButton(
                    authorization,
                    formId = button.formId,
                    id = button.id,
                )
                    .onSuccess {
                        getFormDetails(button.formId)
                    }
                    .onFailure {
                        Timber.d(it)
                    }
            }
        }
    }

    fun deleteFormImage(image: ApiFormImage) {
        viewModelScope.launch {
            val authorization = dataStore.getAuthorizationHeaderValue.first()
            authorization?.let {
                apiService.deleteFormImage(
                    authorization,
                    formId = image.formId,
                    id = image.id,
                )
                    .onSuccess {
                        getFormDetails(image.formId)
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

    fun swapUIComponents(list: List<UIComponent>, first: Int, second: Int) {
        print("First: $first second: $second")

        val newList = list.toMutableList()
        newList[first] = list[first].updateOrder(second)
        newList[second] = list[second].updateOrder(first)
        newList.sortBy {
            it.order()
        }
        _apiUiElements.value = newList

        val bottomOne = listOf(first, second).max()

        viewModelScope.launch {
            val authorization = dataStore.getAuthorizationHeaderValue.first()
            authorization?.let {
                apiService.updateFieldOrder(
                    it,
                    formId = list[bottomOne].formId(),
                    fieldType = list[bottomOne].resourceType().value,
                    fieldId = list[bottomOne].id(),
                    direction = Direction.Up.value
                ).onSuccess {
                    Timber.d(it.toString())
                }.onFailure {
                    Timber.d(it)
                }
            }
        }
    }

    fun addUiComponents(newUIComponent: UIComponent) {

        val uiComponentsCopy = _apiUiElements.value?.toMutableList() ?: mutableListOf()
        uiComponentsCopy.add(newUIComponent)
        _apiUiElements.value = uiComponentsCopy

    }


    fun postFormTextField(textField: ApiFormTextField) {
        viewModelScope.launch {
            val authorization = dataStore.getAuthorizationHeaderValue.first()
            authorization?.let {
                apiService.postTextField(
                    authorization,
                    formId = textField.formId,
                    newFormTextField = textField
                )
                    .onSuccess {
                        addUiComponents(newUIComponent = UIComponent(textField = it))
                        Timber.d("Added new form text field")
                    }
                    .onFailure {
                        Timber.d(it)
                    }
            }

        }
    }

    fun postFormButton(button: ApiFormButton) {
        viewModelScope.launch {
            val authorization = dataStore.getAuthorizationHeaderValue.first()
            authorization?.let {
                apiService.postFormButton(
                    authorization,
                    formId = button.formId,
                    newFormButton = button

                )
                    .onSuccess {
                        addUiComponents(newUIComponent = UIComponent(button = it))
                        Timber.d("Added new form button")
                    }
                    .onFailure {
                        Timber.d(it)
                    }
            }

        }
    }

    fun postFormCheckbox(checkbox: ApiFormCheckbox) {
        viewModelScope.launch {
            val authorization = dataStore.getAuthorizationHeaderValue.first()
            authorization?.let {
                apiService.postFormCheckBox(
                    authorization,
                    formId = checkbox.formId,
                    newFormCheckbox = checkbox

                )
                    .onSuccess {
                        addUiComponents(newUIComponent = UIComponent(checkbox = it))
                        Timber.d("Added new form checkbox")
                    }
                    .onFailure {
                        Timber.d(it)
                    }
            }

        }
    }

    fun postFormToggleSwitch(toggleSwitch: ApiFormToggleSwitch) {
        viewModelScope.launch {
            val authorization = dataStore.getAuthorizationHeaderValue.first()
            authorization?.let {
                apiService.postFormToggleSwitch(
                    authorization,
                    formId = toggleSwitch.formId,
                    newFormToggleSwitch = toggleSwitch

                )
                    .onSuccess {
                        addUiComponents(newUIComponent = UIComponent(toggleSwitch = it))
                        Timber.d("Added new form toggle switch")
                    }
                    .onFailure {
                        Timber.d(it)
                    }
            }

        }
    }

    fun postFormImage(image: ApiFormImage) {
        viewModelScope.launch {
            val authorization = dataStore.getAuthorizationHeaderValue.first()
            authorization?.let {
                apiService.postFormImageWithoutImage(
                    authorization,
                    formId = image.formId,
                )
                    .onSuccess {
                        addUiComponents(newUIComponent = UIComponent(image = it))
                        Timber.d("Added new form image")
                    }
                    .onFailure {
                        Timber.d(it)
                    }
            }

        }
    }

    fun updateFormImage(image: ApiFormImage, file: MultipartBody.Part) {
        viewModelScope.launch {
            val authorization = dataStore.getAuthorizationHeaderValue.first()
            authorization?.let {
                apiService.updateFormImage(
                    authorization,
                    formId = image.formId,
                    id = image.id,
                    image = file
                )
                    .onSuccess {
                        getFormDetails(image.formId)
                    }
                    .onFailure {
                        Timber.d(it)
                    }
            }

        }
    }

    fun postFormLabel(label: ApiFormLabel) {
        viewModelScope.launch {
            val authorization = dataStore.getAuthorizationHeaderValue.first()
            authorization?.let {
                apiService.postFormLabel(
                    authorization,
                    formId = label.formId,
                    newFormLabel = label

                )
                    .onSuccess {
                        addUiComponents(newUIComponent = UIComponent(label = it))
                        Timber.d("Added new form label")
                    }
                    .onFailure {
                        Timber.d(it)
                    }
            }

        }
    }

}



