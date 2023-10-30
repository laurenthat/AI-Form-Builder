package com.sbma.linkup.presentation.screens.bluetooth

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sbma.linkup.application.AppViewModelProvider
import com.sbma.linkup.bluetooth.AppBluetoothViewModel
import com.sbma.linkup.bluetooth.models.toFoundedBluetoothDeviceDomain
import com.sbma.linkup.presentation.screens.bluetooth.components.BluetoothDeviceList
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@Composable
fun ShareViaBluetoothScreenProvider(
    shareId: String,
    onSuccess: () -> Unit,
    onFailure: () -> Unit,
    appBluetoothViewModel: AppBluetoothViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    var permissionsAllowed by rememberSaveable {
        mutableStateOf(false)
    }
    Column {
        if (!permissionsAllowed) {
            GetAllBluetoothPermissionsProvider {
                Timber.d("All permissions are good now")
                permissionsAllowed = true
            }
        } else {
            var idSent by rememberSaveable {
                mutableStateOf(false)
            }
            LaunchedEffect(true) {
                appBluetoothViewModel.scan()
                appBluetoothViewModel.updatePaired()
            }
            LaunchedEffect(true) {
                Timber.d("Collecting  state now!")

                appBluetoothViewModel.state.collectLatest { state ->
                    if (idSent) {
                        return@collectLatest
                    }
                    if (state.errorMessage != null) {
                        onFailure()
                        return@collectLatest
                    }
                    Timber.d("Collect state")
                    if (state.isConnected) {
                        appBluetoothViewModel.sendMessage(shareId)
                        idSent = true
                        Timber.d("ShareId sent!")
                        onSuccess()
                    }
                }
            }
            ShareViaBluetoothScreen()
        }
    }
}

@Composable
fun ShareViaBluetoothScreen(
    appBluetoothViewModel: AppBluetoothViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val bluetoothDevicesFound = appBluetoothViewModel.foundedDevices.collectAsState(initial = listOf())
    val pairedDevices = appBluetoothViewModel.pairedDevices.collectAsState(initial = listOf())

    Column {
        BluetoothDeviceList(data = bluetoothDevicesFound.value.map { it.toFoundedBluetoothDeviceDomain() } + pairedDevices.value) {
            appBluetoothViewModel.connectToDevice(it)
        }
    }
}