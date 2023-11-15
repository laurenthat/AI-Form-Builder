package com.draw2form.ai.application.connectivity


enum class InternetConnectionState {
    CONNECTED,
    DISCONNECTED,
    UNKNOWN;

    fun toTitle() = this.name.lowercase().replaceFirstChar { it.uppercase() }
    fun isConnected() = this.name == CONNECTED.name
}

