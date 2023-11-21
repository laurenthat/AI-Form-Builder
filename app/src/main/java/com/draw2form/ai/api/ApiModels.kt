package com.draw2form.ai.api

import com.draw2form.ai.presentation.screens.UIElement
import com.draw2form.ai.user.User
import java.util.UUID

data class ApiUser(
    val id: String,
    val email: String,
    val name: String,
    val picture: String?,
    val forms: List<ApiForm>?,
    val formSubmissions: List<ApiFormSubmission>?,
)

data class ApiForm(
    val id: String,
    val name: String,
    val status: String,
    val owner: ApiUser?,
    val ownerId: String,
    val textFields: List<ApiFormTextField>?,
    val checkboxes: List<ApiFormCheckbox>?,
    val toggleSwitches: List<ApiFormToggleSwitch>?,
    val images: List<ApiFormImage>?,
    val buttons: List<ApiFormButton>?,
    val labels: List<ApiFormLabel>?,
    val formSubmissions: List<ApiFormSubmission>?,
    val upload: ApiUploadedFile?

)

data class ApiFormSubmission(
    val id: String,
    val name: String,
    val status: String,
    val owner: ApiUser?,
    val ownerId: String,
    val form: ApiForm?,
    val formId: String,
    val textFieldResponses: List<ApiFormTextFieldResponse>?,
    val checkboxResponse: List<ApiFormCheckboxResponse>?,
    val toggleSwitchResponses: List<ApiFormToggleSwitchResponse>?
)

data class ApiFormTextField(
    val id: String,
    val form: ApiForm?,
    val formId: String,
    val label: String,
    override val order: Int,
    val responses: List<ApiFormTextFieldResponse>?
) : UIElement

data class ApiFormTextFieldResponse(
    val id: String,
    val submission: ApiFormSubmission?,
    val submissionId: String,
    val textField: ApiFormTextField?,
    val textFieldId: String,
    val value: String
)

data class ApiFormCheckbox(
    val id: String,
    val label: String,
    val form: ApiForm?,
    val formId: String,
    override val order: Int,
    val responses: List<ApiFormCheckboxResponse>?
) : UIElement

data class ApiFormCheckboxResponse(
    val id: String,
    val submission: ApiFormSubmission?,
    val submissionId: String,
    val checkbox: ApiFormCheckbox?,
    val checkboxId: String,
    val value: String
)

data class ApiFormToggleSwitch(
    val id: String,
    val label: String,
    val form: ApiForm?,
    val formId: String,
    override val order: Int,
    val responses: List<ApiFormToggleSwitchResponse>?
) : UIElement

data class ApiFormToggleSwitchResponse(
    val id: String,
    val submission: ApiFormSubmission?,
    val submissionId: String,
    val toggleSwitch: ApiFormToggleSwitch?,
    val toggleSwitchId: String,
    val value: String
)

data class ApiFormImage(
    val id: String,
    val form: ApiForm?,
    val formId: String,
    override val order: Int,
    val imageId: String
) : UIElement

data class ApiFormButton(
    val id: String,
    val label: String,
    val form: ApiForm?,
    val formId: String,
    override val order: Int,
    val type: String
) : UIElement

data class ApiFormLabel(
    val id: String,
    val form: ApiForm?,
    val formId: String,
    override val order: Int,
    val label: String
) : UIElement

data class ApiImageEvent(
    val id: String,
    val event: String,
    val payload: Any?,
    val file: ApiUploadedFile?,
    val fileId: String
)

data class ApiUploadedFile(
    val id: String,
    val form: ApiForm?,
    val formId: String?,
    val key: String,
    val url: String,
    val events: List<ApiImageEvent>?
)

data class LoginResponseToken(
    val accessToken: String,
    val expiresAt: String
)

data class GetUploadFileDetails(
    val id: String,
    val owner: ApiUser?,
    val ownerId: String?,
    val key: String,
    val url: String,
    val events: List<ApiImageEvent>?
)

data class ApiUploadedFileState(
    val objectRecognition: String,
    val textRecognition: String,
    val formGeneration: String,
)


fun ApiUser.toUser(): User = User(UUID.fromString(id), name, email, "", picture)
fun List<ApiUser>.toUserList(): List<User> = this.map { it.toUser() }
