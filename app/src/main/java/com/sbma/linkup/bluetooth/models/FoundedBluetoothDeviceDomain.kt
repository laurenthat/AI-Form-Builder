package com.sbma.linkup.bluetooth.models

import android.annotation.SuppressLint

data class FoundedBluetoothDeviceDomain(
    val address: String,
    val name: String?,
    val lastSeen: Long
)

@SuppressLint("MissingPermission")
fun FoundedBluetoothDeviceDomain.toFoundedBluetoothDeviceDomain(): BluetoothDeviceDomain {
    return BluetoothDeviceDomain(
        name = name,
        address = address,
    )
}