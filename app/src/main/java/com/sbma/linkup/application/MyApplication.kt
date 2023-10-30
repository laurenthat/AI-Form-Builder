package com.sbma.linkup.application

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.nfc.NfcAdapter
import androidx.activity.ComponentActivity
import com.sbma.linkup.application.connectivity.AppConnectivityManager
import com.sbma.linkup.application.connectivity.InternetConnectionState
import com.sbma.linkup.bluetooth.AppBluetoothManager
import com.sbma.linkup.broadcast.AppBroadcastReceiver
import com.sbma.linkup.datasource.DataStore
import com.sbma.linkup.nfc.AppNfcManager
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

    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var bluetoothAdapter: BluetoothAdapter
    lateinit var appBluetoothManager: AppBluetoothManager
    lateinit var appBroadcastReceiver: AppBroadcastReceiver
    lateinit var appNfcManager: AppNfcManager
    private var nfcAdapter: NfcAdapter? = null


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

        bluetoothManager = getSystemService(BluetoothManager::class.java)

        bluetoothAdapter = bluetoothManager.adapter
        appBluetoothManager = AppBluetoothManager(
            bluetoothAdapter = bluetoothAdapter,
        )
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        coroutineScope.launch {
            val state = internetConnectionState.first()
            if (state.isConnected()) {
                // Call Apis when app launches.
            } else {
                Timber.d("Skipping api calls since there is no internet")
            }
        }


    }

    fun initAppNfcManager(activity: ComponentActivity): AppNfcManager {
        appNfcManager = AppNfcManager(activity, nfcAdapter)
        return appNfcManager
    }

    fun initAppBroadcastReceiver(activity: ComponentActivity): AppBroadcastReceiver {
        appBroadcastReceiver = AppBroadcastReceiver(
            activity = activity,
            bluetoothAdapter = bluetoothAdapter,
            appBluetoothManager = appBluetoothManager,
        )

        return appBroadcastReceiver
    }

}