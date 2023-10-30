package com.sbma.linkup.broadcast

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.sbma.linkup.bluetooth.AppBluetoothManager
import com.sbma.linkup.bluetooth.extensions.toFoundedBluetoothDeviceDomain
import com.sbma.linkup.bluetooth.models.FoundedBluetoothDeviceDomain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import timber.log.Timber

/**
 * Main responsibility of this class is to observe Bluetooth status stop scan if it turned off.
 * And it is the only place to access broadcasts in app.
 */
class AppBroadcastReceiver(
    private val activity: ComponentActivity,
    private val appBluetoothManager: AppBluetoothManager,
    private val bluetoothAdapter: BluetoothAdapter
) : DefaultLifecycleObserver {
    private val _bluetoothEnabled: MutableStateFlow<Boolean> = MutableStateFlow(bluetoothAdapter.isEnabled)
    val bluetoothEnabled: StateFlow<Boolean> get() = _bluetoothEnabled.asStateFlow()

    private val _bluetoothDeviceConnectionStatus: MutableStateFlow<Map<String, Boolean>> = MutableStateFlow(mutableMapOf())
    val bluetoothDeviceConnectionStatus get() = _bluetoothDeviceConnectionStatus.asStateFlow()

    private val _foundedDevices: MutableStateFlow<Map<String, FoundedBluetoothDeviceDomain>> = MutableStateFlow(mutableMapOf())
    val foundedDevices get() = _foundedDevices.map { it.values.toList() }

    // broadcastReceiver reference. It still should be registered in onResume and unregistered on onPause lifecycle.
    private lateinit var broadcastReceiver: BroadcastReceiver

    // Intent Launcher. We use it show a popup to user to turn the bluetooth on.
    private lateinit var intentActivityResultLauncher: ActivityResultLauncher<Intent>


    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        Timber.d("onCreate")

        // Initialization
        broadcastReceiver = broadcastReceiverFactory(
            onBluetoothStateChange = {
                onBluetoothAdapterStateChange(bluetoothAdapter.state)
            },
            onBluetoothDeviceStateChange = { isConnected, bluetoothDevice ->
                val copy = _bluetoothDeviceConnectionStatus.value.toMutableMap()
                copy[bluetoothDevice.address] = isConnected
                _bluetoothDeviceConnectionStatus.value = copy
            },
            onDeviceFound = {
                val copy = _foundedDevices.value.toMutableMap()
                copy[it.address] = it.toFoundedBluetoothDeviceDomain(System.currentTimeMillis())
                _foundedDevices.value = copy
            }
        )

        // Initialization
        intentActivityResultLauncher = activityResultLauncherFactory(activity, owner, "AppAppBroadcastReceiverLauncher") {
            onBluetoothAdapterStateChange(bluetoothAdapter.state)
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        Timber.d("onPause called")

        try {
            unregisterReceiver()
        } catch (e: Exception) {
            Timber.d(e)
        } finally {
            appBluetoothManager.stopScan()
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        Timber.d("onResume called")
        Timber.d("registerReceiver")
        registerReceiver()
    }

    private fun registerReceiver() {
        ContextCompat.registerReceiver(
            activity, broadcastReceiver,
            IntentFilter().apply {
                addAction(BluetoothDevice.ACTION_FOUND)
                addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
                addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)
                addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
                addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
            },
            ContextCompat.RECEIVER_EXPORTED
        )
    }

    private fun unregisterReceiver() {
        activity.unregisterReceiver(broadcastReceiver)
    }


    fun launchEnableBtAdapter() {
        try {
            intentActivityResultLauncher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
        } catch (e: Exception) {
            Timber.d(e)
        }
    }

    fun launchMakeBluetoothDiscoverable() {
        try {
            val discoverableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300) // 300 seconds (5 minutes)
            intentActivityResultLauncher.launch(discoverableIntent)
        } catch (e: Exception) {
            Timber.d(e)
        }
    }


    companion object {
        private fun broadcastReceiverFactory(
            onBluetoothStateChange: () -> Unit,
            onDeviceFound: (bluetoothDevice: BluetoothDevice) -> Unit,
            onBluetoothDeviceStateChange: (isConnected: Boolean, bluetoothDevice: BluetoothDevice) -> Unit

        ): BroadcastReceiver {
            Timber.d("broadcastReceiverFactory")
            return object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent) {
                    Timber.d("broadcastReceiver onReceive ${intent.action}")

                    // It means the user has changed their bluetooth state.
                    if (intent.action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                        onBluetoothStateChange()
                        return
                    }

                    val device = getDeviceFromIntent(intent)

                    device?.let {
                        when (intent.action) {
                            BluetoothDevice.ACTION_FOUND -> {
                                onDeviceFound(it)
                            }

                            BluetoothDevice.ACTION_ACL_CONNECTED -> {
                                onBluetoothDeviceStateChange(true, it)
                            }

                            BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
                                onBluetoothDeviceStateChange(false, it)
                            }
                        }
                    }
                }

                private fun getDeviceFromIntent(intent: Intent): BluetoothDevice? {
                    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        intent.getParcelableExtra(
                            BluetoothDevice.EXTRA_DEVICE,
                            BluetoothDevice::class.java
                        )
                    } else {
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    }
                }
            }
        }

        private fun activityResultLauncherFactory(activity: ComponentActivity, owner: LifecycleOwner, key: String, onAccept: () -> Unit) =
            activity.activityResultRegistry.register(
                key, owner, ActivityResultContracts.StartActivityForResult()
            ) { result ->
                val accepted: Boolean = result.resultCode == Activity.RESULT_OK
                if (accepted) {
                    // Since when intent is launched app will call on onPause and Broadcast receiver will not get the bluetooth state change event.
                    // therefore we need to call it manually.
                    onAccept()
                }
            }
    }

    private fun onBluetoothAdapterStateChange(state: Int) {
        _bluetoothEnabled.value = state == BluetoothAdapter.STATE_ON
        Timber.d("onBluetoothAdapterStateChange $state")
//
//        if (state == BluetoothAdapter.STATE_OFF) {
//            appBluetoothManager.stopScan()
//        }
//        if (state == BluetoothAdapter.STATE_ON) {
//            Timber.d("btadapter back on...")
//        }
    }
}