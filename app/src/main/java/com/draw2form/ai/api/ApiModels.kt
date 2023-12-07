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
    val upload: ApiUploadedFile?,
    val createdAt: String,
    val updatedAt: String
)

data class ApiFormSubmission(
    val id: String,
    val owner: ApiUser?,
    val ownerId: String?,
    val form: ApiForm?,
    val formId: String,
    val textFieldResponses: List<ApiFormTextFieldResponse>?,
    val checkboxResponse: List<ApiFormCheckboxResponse>?,
    val toggleSwitchResponses: List<ApiFormToggleSwitchResponse>?,
    val createdAt: String,
    val updatedAt: String
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
    var value: String
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
    val value: Boolean
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
    val value: Boolean
)

data class ApiFormImage(
    val id: String,
    val form: ApiForm?,
    val formId: String,
    override val order: Int,
    val url: String?
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
    val ObjectDetectionResponseReceived: String,
    val TextDetectionResponseReceived: String,
    val PredictionsUnified: String,
    val UnifiedPredictionCoordinatesRounded: String,
    val UnifiedPredictionsLeveledInYAxis: String,
    val ChatGPT4ImageDescribed: String,
    val ChatGPT3P5JsonGenerated: String,
    val FormComponentsCreated: String,

    ) {


    fun items(): List<Pair<String, String>> {
        return listOf(
            Pair(ObjectDetectionResponseReceived, "Object Detection Response Received"),
            Pair(TextDetectionResponseReceived, "Text Detection Response Received"),
            Pair(PredictionsUnified, "Predictions Unified"),
            Pair(UnifiedPredictionCoordinatesRounded, "Unified Prediction Coordinates Rounded"),
            Pair(UnifiedPredictionsLeveledInYAxis, "Unified Predictions Leveled In Y Axis"),
            Pair(ChatGPT4ImageDescribed, "ChatGPT 4 Image Described"),
            Pair(ChatGPT3P5JsonGenerated, "ChatGPT 3.5 Json Generated"),
            Pair(FormComponentsCreated, "Form Components Created"),
        )
    }

    fun failed(): Boolean {
        if (ObjectDetectionResponseReceived == "error") {
            return true
        }
        if (TextDetectionResponseReceived == "error") {
            return true
        }
        if (PredictionsUnified == "error") {
            return true
        }
        if (UnifiedPredictionCoordinatesRounded == "error") {
            return true
        }
        if (UnifiedPredictionsLeveledInYAxis == "error") {
            return true
        }
        if (ChatGPT4ImageDescribed == "error") {
            return true
        }
        if (ChatGPT3P5JsonGenerated == "error") {
            return true
        }
        if (FormComponentsCreated == "error") {
            return true
        }
        return false
    }

    fun succeeded(): Boolean {
        if (
            ObjectDetectionResponseReceived == "success"
            && TextDetectionResponseReceived == "success"
            && PredictionsUnified == "success"
            && UnifiedPredictionCoordinatesRounded == "success"
            && UnifiedPredictionsLeveledInYAxis == "success"
            && ChatGPT4ImageDescribed == "success"
            && ChatGPT3P5JsonGenerated == "success"
            && FormComponentsCreated == "success"
        ) {
            return true
        }
        return false
    }
}

data class ApiFormShare(
    val id: String,
    val owner: ApiUser?,
    val ownerId: String?,
    val form: ApiForm?,
    val formId: String,
)

data class ApiFormId(
    val formId: String?
)

data class NewFormSubmissionTextFieldResponse(
    val id: String,
    var value: String
)

data class NewFormSubmissionCheckboxResponse(
    val id: String,
    var value: Boolean
)

data class NewFormSubmissionToggleSwitchResponse(
    val id: String,
    var value: Boolean
)

data class NewFormSubmissionRequestBody(
    val textFieldResponses: List<NewFormSubmissionTextFieldResponse>,
    val checkboxResponses: List<NewFormSubmissionCheckboxResponse>,
    val toggleSwitchResponses: List<NewFormSubmissionToggleSwitchResponse>
)


fun ApiUser.toUser(): User = User(UUID.fromString(id), name, email, "", picture)
fun List<ApiUser>.toUserList(): List<User> = this.map { it.toUser() }
