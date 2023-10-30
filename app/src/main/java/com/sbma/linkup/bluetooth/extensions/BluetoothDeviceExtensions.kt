package com.sbma.linkup.bluetooth.extensions

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import com.sbma.linkup.bluetooth.models.BluetoothDeviceDomain
import com.sbma.linkup.bluetooth.models.FoundedBluetoothDeviceDomain

@SuppressLint("MissingPermission")
fun BluetoothDevice.toFoundedBluetoothDeviceDomain(lastSeen: Long): FoundedBluetoothDeviceDomain {
    return FoundedBluetoothDeviceDomain(
        name = name,
        address = address,
        lastSeen = lastSeen
    )
}

@SuppressLint("MissingPermission")
fun BluetoothDevice.toBluetoothDeviceDomain(): BluetoothDeviceDomain {
    return BluetoothDeviceDomain(
        name = name,
        address = address
    )
}
