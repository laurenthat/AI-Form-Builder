package com.sbma.linkup.application

import android.content.Context
import com.sbma.linkup.api.ApiService
import com.sbma.linkup.api.RetrofitFactory
import com.sbma.linkup.card.CardRepository
import com.sbma.linkup.card.ICardRepository
import com.sbma.linkup.connection.ConnectionRepository
import com.sbma.linkup.connection.IConnectionRepository
import com.sbma.linkup.datasource.AppDatabase
import com.sbma.linkup.tag.ITagRepository
import com.sbma.linkup.tag.TagRepository
import com.sbma.linkup.user.IUserRepository
import com.sbma.linkup.user.UserRepository


/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val appDatabase: AppDatabase
    val apiService: ApiService
    val userRepository: IUserRepository
    val cardRepository: ICardRepository
    val userConnectionRepository: IConnectionRepository
    val tagRepository: ITagRepository
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
    override val cardRepository: ICardRepository by lazy {
        CardRepository(appDatabase.userCardDao())
    }
    override val userConnectionRepository: IConnectionRepository by lazy {
        ConnectionRepository(appDatabase.userConnectionDao(), appDatabase.connectionCardDao())
    }
    override val tagRepository: ITagRepository by lazy {
        TagRepository(appDatabase.tagDao())
    }
}