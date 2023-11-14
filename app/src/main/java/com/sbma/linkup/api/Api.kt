package com.sbma.linkup.api

import com.sbma.linkup.api.apimodels.ApiCard
import com.sbma.linkup.api.apimodels.ApiConnection
import com.sbma.linkup.api.apimodels.ApiForm
import com.sbma.linkup.api.apimodels.ApiShare
import com.sbma.linkup.api.apimodels.ApiTag
import com.sbma.linkup.api.apimodels.ApiUser
import com.sbma.linkup.api.apimodels.AssignTagRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import java.io.File

interface ApiService {

    // Profile GET Request
    @GET("/profile")
    @Headers("Content-Type: application/json")
    suspend fun getProfile(@Header("Authorization") authorization: String): Result<ApiUser>

    // New Card POST Request
    @POST("/card")
    @Headers("Content-Type: application/json")
    suspend fun createNewCard(@Header("Authorization") authorization: String, @Body request: ApiCard): Result<ApiCard>

    // Update Card PUT Request
    @PUT("/card/{id}")
    @Headers("Content-Type: application/json")
    suspend fun updateCard(@Header("Authorization") authorization: String, @Path("id") id: String, @Body request: ApiCard): Result<ApiCard>

    // Update Card PUT Request
    @DELETE("/card/{id}")
    @Headers("Content-Type: application/json")
    suspend fun deleteCard(@Header("Authorization") authorization: String, @Path("id") id: String): Result<Unit>

    // Share POST Request
    @POST("/share")
    @Headers("Content-Type: application/json")
    suspend fun share(@Header("Authorization") authorization: String, @Body shareData: List<String>): Result<ApiShare>

    // Scan POST Request
    @POST("/share/{shareId}/scan")
    @Headers("Content-Type: application/json")
    suspend fun scanShare(@Header("Authorization") authorization: String, @Path("shareId") shareId: String): Result<ApiConnection>

    // Tag Assign POST Request
    @POST("/tag")
    @Headers("Content-Type: application/json")
    suspend fun assignTag(@Header("Authorization") authorization: String, @Body request: AssignTagRequest): Result<ApiTag>

    // Tag Scan POST Request
    @POST("/tag/{tagId}/scan")
    @Headers("Content-Type: application/json")
    suspend fun scanTag(@Header("Authorization") authorization: String, @Path("tagId") tagId: String): Result<ApiConnection>

    // Tag Delete DELETE Request
    @DELETE("/tag/{tagId}")
    @Headers("Content-Type: application/json")
    suspend fun deleteTag(@Header("Authorization") authorization: String, @Path("tagId") tagId: String): Result<Unit>

    @Multipart
    @POST("upload-image")
    suspend fun uploadImage(
        @Header("Authorization") authorization: String,
        @Part image: MultipartBody.Part
    ): Result<ApiForm>
}