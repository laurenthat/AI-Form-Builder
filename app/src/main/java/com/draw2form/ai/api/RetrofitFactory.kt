package com.draw2form.ai

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitFactory {
    fun makeApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl("https://draw2form.ericaskari.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ResultCallAdapterFactory())
            .build().create(ApiService::class.java)
    }
}