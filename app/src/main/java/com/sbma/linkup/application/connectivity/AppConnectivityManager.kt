package com.sbma.linkup.application.connectivity

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi


class AppConnectivityManager(
    private val application: Application,
    val onChange: (isConnected: InternetConnectionState) -> Unit
) {
    init {
        register()
    }

    private fun register() {
        (application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).let { connectivityManager ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                registerAPI24AndAbove(connectivityManager)
            } else {
                onChange(checkConnection(application))
                registerAPI24Below(connectivityManager)
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun registerAPI24AndAbove(connectivityManager: ConnectivityManager) {
        connectivityManager.registerDefaultNetworkCallback(buildNetworkCallback())
    }


    @SuppressLint("WrongConstant")
    private fun registerAPI24Below(connectivityManager: ConnectivityManager) {
        NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addCapability(NetworkCapabilities.TRANSPORT_WIFI)
            .build().let { networkRequest ->
                connectivityManager.registerNetworkCallback(networkRequest, buildNetworkCallback())
            }
    }

    private fun buildNetworkCallback(): ConnectivityManager.NetworkCallback {
        return object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                this@AppConnectivityManager.onChange(InternetConnectionState.CONNECTED)
            }

            override fun onLost(network: Network) {
                this@AppConnectivityManager.onChange(InternetConnectionState.DISCONNECTED)
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun checkConnection(context: Context): InternetConnectionState {
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connMgr.activeNetworkInfo
        return activeNetworkInfo?.let {
            when (it.type) {
                ConnectivityManager.TYPE_WIFI -> InternetConnectionState.CONNECTED
                ConnectivityManager.TYPE_MOBILE -> InternetConnectionState.CONNECTED
                else -> InternetConnectionState.CONNECTED
            }
        } ?: InternetConnectionState.DISCONNECTED
    }
}