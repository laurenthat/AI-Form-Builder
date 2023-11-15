package com.draw2form.ai.application

import android.app.Application
import com.draw2form.ai.application.connectivity.AppConnectivityManager
import com.draw2form.ai.application.connectivity.InternetConnectionState
import com.draw2form.ai.datasource.DataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber

class MyApplication : Application() {
    companion object {
        private val parentJob = Job()
        private val coroutineScope = CoroutineScope(Dispatchers.Default + parentJob)
    }

    val internetConnectionState = MutableStateFlow(InternetConnectionState.UNKNOWN)


    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */

    // Service to save key value format data
    lateinit var dataStore: DataStore

    // Service to check internet connection
    private lateinit var appConnectivityManager: AppConnectivityManager


    // Container of repositories
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        dataStore = DataStore(this)

        appConnectivityManager = AppConnectivityManager(this) {
            Timber.d("[AppConnectivityManager] ${it.toTitle()}")
            internetConnectionState.value = it
        }

        container = AppDataContainer(this)

        coroutineScope.launch {
            val state = internetConnectionState.first()
            if (state.isConnected()) {
                // Call Apis when app launches.
            } else {
                Timber.d("Skipping api calls since there is no internet")
            }
        }


    }
}