package com.draw2form.ai

import com.draw2form.ai.user.User
import java.util.UUID

data class ApiUser(
    val id: String,
    val email: String,
    val name: String,
    val picture: String?,
    val forms: List<ApiForm>?,
    val formSubmissions: List<ApiFormSubmission>?,
    val uploads: List<ApiUploadedFile>?
)

data class ApiForm(
    val id: String,
    val name: String,
    val available: Boolean,
    val owner: ApiUser?,
    val ownerId: String,
    val textFields: List<ApiFormTextField>?,
    val checkboxes: List<ApiFormCheckbox>?,
    val toggleSwitches: List<ApiFormToggleSwitch>?,
    val images: List<ApiFormImage>?,
    val buttons: List<ApiFormButton>?,
    val labels: List<ApiFormLabel>?,
    val formSubmissions: List<ApiFormSubmission>?
)

data class ApiFormSubmission(
    val id: String,
    val name: String,
    val public: Boolean,
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
    val order: Int,
    val responses: List<ApiFormTextFieldResponse>?
)

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
    val order: Int,
    val responses: List<ApiFormCheckboxResponse>?
)

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
    val order: Int,
    val responses: List<ApiFormToggleSwitchResponse>?
)

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
    val order: Int,
    val imageId: String
)

data class ApiFormButton(
    val id: String,
    val label: String,
    val form: ApiForm?,
    val formId: String,
    val order: Int,
    val type: String
)

data class ApiFormLabel(
    val id: String,
    val form: ApiForm?,
    val formId: String,
    val order: Int,
    val value: String
)

data class ApiImageEvent(
    val id: String,
    val event: String,
    val payload: String?,
    val file: ApiUploadedFile?,
    val fileId: String
)

data class ApiUploadedFile(
    val id: String,
    val owner: ApiUser?,
    val ownerId: String?,
    val key: String,
    val url: String,
    val events: List<ApiImageEvent>?
)

data class LoginResponseToken(
    val accessToken: String,
    val expiresAt: String
)

fun ApiUser.toUser(): User = User(UUID.fromString(id), name, email, "", picture)
fun List<ApiUser>.toUserList(): List<User> = this.map { it.toUser() }
