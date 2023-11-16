package com.draw2form.ai.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitFactory {
    fun makeApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl("https://draw2form.ericaskari.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ResultCallAdapterFactory())
            .build().create(ApiService::class.java)
    }
}