package com.sbma.linkup.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import com.sbma.linkup.bluetooth.connect.BluetoothDataTransferService
import com.sbma.linkup.bluetooth.extensions.toBluetoothDeviceDomain
import com.sbma.linkup.bluetooth.models.BluetoothDeviceDomain
import com.sbma.linkup.bluetooth.models.BluetoothMessage
import com.sbma.linkup.bluetooth.models.ConnectionResult
import com.sbma.linkup.bluetooth.models.toByteArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.update
import timber.log.Timber
import java.io.IOException
import java.util.UUID


/**
 * Responsible for using bluetooth adapter in application
 */
class AppBluetoothManager(
    val bluetoothAdapter: BluetoothAdapter?,
) {
    private var currentServerSocket: BluetoothServerSocket? = null
    private var currentClientSocket: BluetoothSocket? = null
    private var dataTransferService: BluetoothDataTransferService? = null

    private val _isScanning = MutableStateFlow(false)
    val isScanning get() = _isScanning.asStateFlow()

    private val _pairedDevices = MutableStateFlow<List<BluetoothDeviceDomain>>(emptyList())
    val pairedDevices: StateFlow<List<BluetoothDeviceDomain>> get() = _pairedDevices.asStateFlow()

    @SuppressLint("MissingPermission")
    fun updatePairedDevices() {
        bluetoothAdapter?.let {
            Timber.d("Bounded devices: ${bluetoothAdapter.bondedDevices.count()}")
            bluetoothAdapter
                .bondedDevices
                ?.map { it.toBluetoothDeviceDomain() }
                ?.also { devices ->
                    _pairedDevices.update { devices }
                }
        }
    }

    @SuppressLint("MissingPermission")
    fun scan() {
        Timber.d("scan")

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled || _isScanning.value) {
            return
        }

        try {
            updatePairedDevices()
            bluetoothAdapter.startDiscovery()
            _isScanning.value = true
        } catch (e: Exception) {
            Timber.d("scan ERROR")
            Timber.d(e)
            Timber.d(e.message)
            _isScanning.value = false
        }
    }

    @SuppressLint("MissingPermission")
    fun stopScan() {
        Timber.d("stopScan")

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled || !_isScanning.value) {
            return
        }

        try {
            bluetoothAdapter.cancelDiscovery()
            _isScanning.value = false
        } catch (e: Exception) {
            Timber.d(e.message)
        }
    }


    @SuppressLint("MissingPermission")
    fun startBluetoothServer(): Flow<ConnectionResult> {

        return flow {
            Timber.d("listenUsingRfcommWithServiceRecord")
            currentServerSocket = bluetoothAdapter?.listenUsingRfcommWithServiceRecord(
                "chat_service",
                UUID.fromString(SERVICE_UUID)
            )

            var shouldLoop = true
            while (shouldLoop) {
                Timber.d("shouldLoop")

                currentClientSocket = try {
                    currentServerSocket?.accept()
                } catch (e: IOException) {
                    Timber.d("e")
                    Timber.d(e)
                    shouldLoop = false
                    null
                }

                Timber.d("ConnectionEstablished")
                emit(ConnectionResult.ConnectionEstablished)
                currentClientSocket?.let {
                    Timber.d("currentServerSocket?.close")
                    currentServerSocket?.close()
                    val service = BluetoothDataTransferService(it)
                    dataTransferService = service

                    Timber.d("emitAll")
                    emitAll(
                        service
                            .listenForIncomingMessages()
                            .map { message ->
                                ConnectionResult.TransferSucceeded(message)
                            }
                    )
                }
            }
        }.onCompletion {
            closeConnection()
        }.flowOn(Dispatchers.IO)
    }

    @SuppressLint("MissingPermission")
    suspend fun trySendMessage(message: String): BluetoothMessage? {

        if (dataTransferService == null) {
            return null
        }

        val bluetoothMessage = BluetoothMessage(
            message = message,
            senderName = bluetoothAdapter?.name ?: "Unknown name"
        )

        dataTransferService?.sendMessage(bluetoothMessage.toByteArray())

        return bluetoothMessage
    }

    @SuppressLint("MissingPermission")
    fun stopDiscovery() {
        bluetoothAdapter?.cancelDiscovery()
    }

    @SuppressLint("MissingPermission")
    fun connectToDevice(device: BluetoothDeviceDomain): Flow<ConnectionResult> {
        Timber.d("[connectToDevice]")
        return flow {

            val btDevice = bluetoothAdapter?.getRemoteDevice(device.address)
            Timber.d(btDevice.toString())
            Timber.d("connectToDevice createRfcommSocketToServiceRecord")
            currentClientSocket = btDevice?.createRfcommSocketToServiceRecord(UUID.fromString(SERVICE_UUID))
            stopDiscovery()

            currentClientSocket?.let { socket ->
                try {
                    Timber.d("connectToDevice connect")
                    socket.connect()
                    Timber.d("connectToDevice emit")
                    emit(ConnectionResult.ConnectionEstablished)

                    BluetoothDataTransferService(socket).also {
                        dataTransferService = it
                        emitAll(
                            it.listenForIncomingMessages()
                                .map { message ->
                                    ConnectionResult.TransferSucceeded(message)
                                }
                        )
                    }
                } catch (e: IOException) {
                    Timber.d("currentClientSocket e")
                    Timber.d(e)
                    socket.close()
                    currentClientSocket = null
                    emit(ConnectionResult.Error("Connection was interrupted"))
                }
            }
        }.onCompletion {
            Timber.d(it)
            closeConnection()
        }.flowOn(Dispatchers.IO)
    }

    fun closeConnection() {
        Timber.d("closeConnection")
        currentClientSocket?.close()
        currentServerSocket?.close()
        currentClientSocket = null
        currentServerSocket = null
    }

    companion object {
        const val SERVICE_UUID = "27b7d1da-08c7-4505-a6d1-2459987e5e2d"
    }
}