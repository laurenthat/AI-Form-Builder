package com.draw2form.ai.application

import android.content.Context
import com.draw2form.ai.api.ApiService
import com.draw2form.ai.api.RetrofitFactory
import com.draw2form.ai.datasource.AppDatabase
import com.draw2form.ai.user.IUserRepository
import com.draw2form.ai.user.UserRepository


/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val appDatabase: AppDatabase
    val apiService: ApiService
    val userRepository: IUserRepository
}

class AppDataContainer(private val context: Context) :
    AppContainer {

    override val appDatabase: AppDatabase by lazy {
        AppDatabase.getInstance(context)
    }
    override val apiService: ApiService by lazy {
        RetrofitFactory.makeApiService()
    }
    override val userRepository: IUserRepository by lazy {
        UserRepository(appDatabase.userDao())
    }
}