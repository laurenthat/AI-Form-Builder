package com.draw2form.ai.api

import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
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
}