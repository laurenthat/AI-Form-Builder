package com.sbma.linkup.bluetooth.models

data class BluetoothMessage(
    val message: String,
    val senderName: String,
)
fun String.toBluetoothMessage(): BluetoothMessage {
    val name = substringBeforeLast("#")
    val message = substringAfter("#")
    return BluetoothMessage(
        message = message,
        senderName = name,
    )
}

fun BluetoothMessage.toByteArray(): ByteArray {
    return "$senderName#$message".encodeToByteArray()
}