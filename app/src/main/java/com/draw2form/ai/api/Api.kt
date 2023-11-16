package com.draw2form.ai.api

import com.draw2form.ai.ApiUploadedFile
import com.draw2form.ai.ApiUser
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    // Profile GET Request
    @GET("/profile")
    @Headers("Content-Type: application/json")
    suspend fun getProfile(@Header("Authorization") authorization: String): Result<ApiUser>


    @Multipart
    @POST("upload")
    suspend fun uploadImage(
        @Header("Authorization") authorization: String,
        @Part image: MultipartBody.Part
    ): Result<ApiUploadedFile>
}