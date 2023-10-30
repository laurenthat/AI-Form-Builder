package com.sbma.linkup.presentation.screens.bluetooth.components

import com.sbma.linkup.bluetooth.models.BluetoothDeviceDomain
import com.sbma.linkup.bluetooth.models.BluetoothMessage


data class BluetoothUiState(
    val scannedDevices: List<BluetoothDeviceDomain> = emptyList(),
    val pairedDevices: List<BluetoothDeviceDomain> = emptyList(),
    val isConnected: Boolean = false,
    val isConnecting: Boolean = false,
    val errorMessage: String? = null,
    val messages: List<BluetoothMessage> = emptyList()
)
