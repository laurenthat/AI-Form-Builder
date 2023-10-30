package com.sbma.linkup.bluetooth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sbma.linkup.bluetooth.models.BluetoothDeviceDomain
import com.sbma.linkup.bluetooth.models.ConnectionResult
import com.sbma.linkup.bluetooth.models.toFoundedBluetoothDeviceDomain
import com.sbma.linkup.broadcast.AppBroadcastReceiver
import com.sbma.linkup.presentation.screens.bluetooth.components.BluetoothUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber


/**
 * ViewModel for using AppBluetoothManager and AppBroadcastReceiver
 */
class AppBluetoothViewModel(
    private val appBluetoothManager: AppBluetoothManager,
    private val appBroadcastReceiver: AppBroadcastReceiver,
) : ViewModel() {
    val isBluetoothEnabled = appBroadcastReceiver.bluetoothEnabled
    private val isScanning = appBluetoothManager.isScanning

    val pairedDevices = appBluetoothManager.pairedDevices
    val foundedDevices = appBroadcastReceiver.foundedDevices

    private var deviceConnectionJob: Job? = null

    private val _state = MutableStateFlow(BluetoothUiState())

    val state = combine(foundedDevices, pairedDevices, _state) { scannedDevices, pairedDevices, state ->
        state.copy(
            scannedDevices = scannedDevices.map { it.toFoundedBluetoothDeviceDomain() },
            pairedDevices = pairedDevices,
            messages = if (state.isConnected) state.messages else emptyList()
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _state.value)


    fun scan() {
        if (isScanning.value) {
            return
        }
        viewModelScope.launch {
            appBluetoothManager.scan()
            delay(5000)
            appBluetoothManager.stopScan()
        }
    }

    fun askToTurnBluetoothOn() {
        appBroadcastReceiver.launchEnableBtAdapter()
    }

    fun launchMakeBluetoothDiscoverable() {
        appBroadcastReceiver.launchMakeBluetoothDiscoverable()
    }

    fun connectToDevice(device: BluetoothDeviceDomain) {
        Timber.d("connectToDevice: $device")
        _state.update { it.copy(isConnecting = true) }
        deviceConnectionJob = appBluetoothManager
            .connectToDevice(device)
            .listen()
    }


    private fun Flow<ConnectionResult>.listen(): Job {
        return onEach { result ->
            Timber.d("Flow listen: $result")

            when (result) {
                ConnectionResult.ConnectionEstablished -> {
                    _state.update {
                        it.copy(
                            isConnected = true,
                            isConnecting = false,
                            errorMessage = null
                        )
                    }
                }

                is ConnectionResult.TransferSucceeded -> {
                    _state.update {
                        it.copy(
                            messages = it.messages + result.message
                        )
                    }
                }

                is ConnectionResult.Error -> {
                    _state.update {
                        it.copy(
                            isConnected = false,
                            isConnecting = false,
                            errorMessage = result.message
                        )
                    }
                }
            }
        }
            .catch { throwable ->
                Timber.d("Flow listen catch")
                Timber.d(throwable)
                appBluetoothManager.closeConnection()
                _state.update {
                    it.copy(
                        isConnected = false,
                        isConnecting = false,
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun startServer() {
        appBluetoothManager.startBluetoothServer().listen()
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            val bluetoothMessage = appBluetoothManager.trySendMessage(message)
            if (bluetoothMessage != null) {
                _state.update {
                    it.copy(
                        messages = it.messages + bluetoothMessage
                    )
                }
            }
        }
    }

    fun updatePaired() {
        appBluetoothManager.updatePairedDevices()
    }
}