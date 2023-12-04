package com.draw2form.ai.api

import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    // Profile GET Request
    @GET("api/profile")
    @Headers("Content-Type: application/json")
    suspend fun getProfile(@Header("Authorization") authorization: String): Result<ApiUser>


    @Multipart
    @POST("api/upload")
    suspend fun uploadImage(
        @Header("Authorization") authorization: String,
        @Part image: MultipartBody.Part
    ): Result<ApiUploadedFile>

    // Upload GET Request
    @GET("api/upload/{id}/status")
    @Headers("Content-Type: application/json")
    suspend fun getUploadState(
        @Header("Authorization") authorization: String,
        @Path("id") id: String
    ): Result<ApiUploadedFileState>

    @GET("api/upload/{id}")
    @Headers("Content-Type: application/json")
    suspend fun getUploadDetails(
        @Header("Authorization") authorization: String,
        @Path("id") id: String
    ): Result<GetUploadFileDetails>

    @GET("api/upload/{id}/event/{eventName}")
    @Headers("Content-Type: application/json")
    suspend fun getUploadEvent(
        @Header("Authorization") authorization: String,
        @Path("id") id: String,
        @Path("eventName") eventName: String
    ): Result<ApiImageEvent?>

    @GET("api/upload/{id}/event/{eventName}/payload")
    @Headers("Content-Type: application/json")
    suspend fun <T> getUploadEventPayload(
        @Header("Authorization") authorization: String,
        @Path("id") id: String,
        @Path("eventName") eventName: String
    ): Result<T?>

    @GET("api/forms")
    @Headers("Content-Type: application/json")
    suspend fun getForms(@Header("Authorization") authorization: String): Result<List<ApiForm>>

    @PATCH("/api/forms/{formId}/fields/label/{id}")
    @Headers("Content-Type: application/json")
    suspend fun updateFormLabel(
        @Header("Authorization") authorization: String,
        @Path("id") id: String,
        @Path("formId") formId: String,
        @Body newFormLabel: ApiFormLabel
    ): Result<ApiFormLabel>

    @DELETE("/api/forms/{formId}/fields/label/{id}")
    @Headers("Content-Type: application/json")
    suspend fun deleteFormLabel(
        @Header("Authorization") authorization: String,
        @Path("id") id: String,
        @Path("formId") formId: String,
    ): Result<ApiFormLabel>

    @PATCH("/api/forms/{formId}/fields/text-field/{id}")
    @Headers("Content-Type: application/json")
    suspend fun updateFormTextField(
        @Header("Authorization") authorization: String,
        @Path("id") id: String,
        @Path("formId") formId: String,
        @Body newFormTextField: ApiFormTextField
    ): Result<ApiFormTextField>

    @DELETE("/api/forms/{formId}/fields/text-field/{id}")
    @Headers("Content-Type: application/json")
    suspend fun deleteFormTextField(
        @Header("Authorization") authorization: String,
        @Path("id") id: String,
        @Path("formId") formId: String,
    ): Result<ApiFormTextField>

    @PATCH("/api/forms/{formId}/fields/checkbox/{id}")
    @Headers("Content-Type: application/json")
    suspend fun updateFormCheckbox(
        @Header("Authorization") authorization: String,
        @Path("id") id: String,
        @Path("formId") formId: String,
        @Body newFormCheckbox: ApiFormCheckbox
    ): Result<ApiFormCheckbox>

    @DELETE("/api/forms/{formId}/fields/checkbox/{id}")
    @Headers("Content-Type: application/json")
    suspend fun deleteFormCheckbox(
        @Header("Authorization") authorization: String,
        @Path("id") id: String,
        @Path("formId") formId: String,
    ): Result<ApiFormCheckbox>

    @PATCH("/api/forms/{formId}/fields/toggle-switch/{id}")
    @Headers("Content-Type: application/json")
    suspend fun updateFormToggleSwitch(
        @Header("Authorization") authorization: String,
        @Path("id") id: String,
        @Path("formId") formId: String,
        @Body newFormToggleSwitch: ApiFormToggleSwitch
    ): Result<ApiFormToggleSwitch>

    @DELETE("/api/forms/{formId}/fields/toggle-switch/{id}")
    @Headers("Content-Type: application/json")
    suspend fun deleteFormToggleSwitch(
        @Header("Authorization") authorization: String,
        @Path("id") id: String,
        @Path("formId") formId: String,
    ): Result<ApiFormToggleSwitch>

    @PATCH("/api/forms/{formId}/fields/button/{id}")
    @Headers("Content-Type: application/json")
    suspend fun updateFormButton(
        @Header("Authorization") authorization: String,
        @Path("id") id: String,
        @Path("formId") formId: String,
        @Body newFormButton: ApiFormButton
    ): Result<ApiFormButton>

    @DELETE("/api/forms/{formId}/fields/button/{id}")
    @Headers("Content-Type: application/json")
    suspend fun deleteFormButton(
        @Header("Authorization") authorization: String,
        @Path("id") id: String,
        @Path("formId") formId: String,
    ): Result<ApiFormButton>

    @DELETE("/api/forms/{formId}/fields/image/{id}")
    @Headers("Content-Type: application/json")
    suspend fun deleteFormImage(
        @Header("Authorization") authorization: String,
        @Path("id") id: String,
        @Path("formId") formId: String,
    ): Result<ApiFormImage>

    @Multipart
    @PATCH("/api/forms/{formId}/fields/image/{id}")
    suspend fun updateFormImage(
        @Header("Authorization") authorization: String,
        @Path("id") id: String,
        @Path("formId") formId: String,
        @Part image: MultipartBody.Part
    ): Result<ApiFormImage>


    @Multipart
    @POST("api/forms")
    suspend fun createForm(
        @Header("Authorization") authorization: String,
        @Part image: MultipartBody.Part
    ): Result<ApiForm>

    @GET("api/forms/{id}")
    @Headers("Content-Type: application/json")
    suspend fun getFormDetails(
        @Header("Authorization") authorization: String,
        @Path("id") id: String
    ): Result<ApiForm>

    // Upload GET Request
    @GET("api/forms/{id}/status")
    @Headers("Content-Type: application/json")
    suspend fun getFormUploadStatus(
        @Header("Authorization") authorization: String,
        @Path("id") id: String
    ): Result<ApiUploadedFileState>


    @GET("api/forms/{id}/event/{eventName}")
    @Headers("Content-Type: application/json")
    suspend fun getFormUploadEvent(
        @Header("Authorization") authorization: String,
        @Path("id") id: String,
        @Path("eventName") eventName: String
    ): Result<ApiImageEvent?>

    @GET("api/forms/{id}/event/{eventName}/payload")
    @Headers("Content-Type: application/json")
    suspend fun <T> getFormUploadEventPayload(
        @Header("Authorization") authorization: String,
        @Path("id") id: String,
        @Path("eventName") eventName: String
    ): Result<T?>


    //Share via QR Code
    @POST("api/forms/{formId}/publish")
    @Headers("Content-Type: application/json")
    suspend fun formShare(
        @Header("Authorization") authorization: String,
        @Path("formId") formId: String
    ): Result<ApiForm>

    // Delete form by id
    @DELETE("api/forms/{id}")
    @Headers("Content-Type: application/json")
    suspend fun deleteForm(
        @Header("Authorization") authorization: String,
        @Path("id") id: String
    ): Result<ApiForm>

    @POST("/api/forms/{formId}/submit")
    @Headers("Content-Type: application/json")
    suspend fun submitFormApi(
        @Header("Authorization") authorization: String?,
        @Path("formId") formId: String,
        @Body request: NewFormSubmissionRequestBody
    ): Result<ApiFormSubmission>


    // Create new form components
    @POST("/api/forms/{formId}/fields/label")
    @Headers("Content-Type: application/json")
    suspend fun postFormLabel(
        @Header("Authorization") authorization: String,
        @Path("formId") formId: String,
        @Body newFormLabel: ApiFormLabel
    ): Result<ApiFormLabel>

    @POST("/api/forms/{formId}/fields/checkbox")
    @Headers("Content-Type: application/json")
    suspend fun postFormCheckBox(
        @Header("Authorization") authorization: String,
        @Path("formId") formId: String,
        @Body newFormCheckbox: ApiFormCheckbox
    ): Result<ApiFormCheckbox>

    @POST("/api/forms/{formId}/fields/toggle-switch")
    @Headers("Content-Type: application/json")
    suspend fun postFormToggleSwitch(
        @Header("Authorization") authorization: String,
        @Path("formId") formId: String,
        @Body newFormToggleSwitch: ApiFormToggleSwitch
    ): Result<ApiFormToggleSwitch>

    @POST("/api/forms/{formId}/fields/button")
    @Headers("Content-Type: application/json")
    suspend fun postFormButton(
        @Header("Authorization") authorization: String,
        @Path("formId") formId: String,
        @Body newFormButton: ApiFormButton
    ): Result<ApiFormButton>

    @POST("/api/forms/{formId}/fields/image")
    @Headers("Content-Type: application/json")
    suspend fun postFormImageWithoutImage(
        @Header("Authorization") authorization: String,
        @Path("formId") formId: String,
    ): Result<ApiFormImage>

    @Multipart
    @POST("/api/forms/{formId}/fields/image")
    suspend fun postFormImage(
        @Header("Authorization") authorization: String,
        @Path("formId") formId: String,
        @Part image: MultipartBody.Part?
    ): Result<ApiFormImage>


    @POST("api/forms/{formId}/fields/text-field")
    @Headers("Content-Type: application/json")
    suspend fun postTextField(
        @Header("Authorization") authorization: String,
        @Path("formId") formId: String,
        @Body newFormTextField: ApiFormTextField
    ): Result<ApiFormTextField>

}