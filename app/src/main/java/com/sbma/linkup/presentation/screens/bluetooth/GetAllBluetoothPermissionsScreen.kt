package com.sbma.linkup.presentation.screens.bluetooth

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.sbma.linkup.application.AppViewModelProvider
import com.sbma.linkup.bluetooth.AppBluetoothViewModel
import com.sbma.linkup.presentation.ui.theme.LinkUpTheme
import timber.log.Timber


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GetAllBluetoothPermissionsProvider(
    done: () -> Unit
) {
    val appBluetoothViewModel: AppBluetoothViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val enabled by appBluetoothViewModel.isBluetoothEnabled.collectAsState()
    val bluetoothConnectPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        rememberPermissionState(
            Manifest.permission.BLUETOOTH_CONNECT
        )
    } else {
        rememberPermissionState(
            Manifest.permission.BLUETOOTH
        )
    }
    val bluetoothScanPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        rememberPermissionState(
            Manifest.permission.BLUETOOTH_SCAN
        )
    } else {
        rememberPermissionState(
            Manifest.permission.BLUETOOTH
        )
    }
    val fineLocationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    GetAllBluetoothPermissions(
        isBluetoothEnabled = enabled,
        enableBluetooth = {appBluetoothViewModel.askToTurnBluetoothOn()},
        hasBluetoothConnectPermission = bluetoothConnectPermissionState.status.isGranted,
        bluetoothConnectPermissionShouldShowRationale = bluetoothConnectPermissionState.status.shouldShowRationale,
        getBluetoothConnectPermission = {bluetoothConnectPermissionState.launchPermissionRequest()},
        hasBluetoothScanPermission = bluetoothScanPermissionState.status.isGranted,
        bluetoothScanPermissionShouldShowRationale = bluetoothScanPermissionState.status.shouldShowRationale,
        getBluetoothScanPermission = {bluetoothScanPermissionState.launchPermissionRequest()},
        hasFineLocationPermission = fineLocationPermissionState.status.isGranted,
        fineLocationPermissionShouldShowRationale = fineLocationPermissionState.status.shouldShowRationale,
        getFineLocationPermission = {fineLocationPermissionState.launchPermissionRequest()},
        done = done
    )
}

@Composable
fun GetAllBluetoothPermissions(
    isBluetoothEnabled: Boolean,
    enableBluetooth: () -> Unit,
    hasBluetoothConnectPermission: Boolean,
    bluetoothConnectPermissionShouldShowRationale: Boolean,
    getBluetoothConnectPermission: () -> Unit,
    hasBluetoothScanPermission: Boolean,
    bluetoothScanPermissionShouldShowRationale: Boolean,
    getBluetoothScanPermission: () -> Unit,
    hasFineLocationPermission: Boolean,
    fineLocationPermissionShouldShowRationale: Boolean,
    getFineLocationPermission: () -> Unit,
    done: () -> Unit
) {
    var permissions by rememberSaveable { mutableStateOf(mapOf(
        "bluetoothEnabled" to false,
        "bluetoothConnect" to false,
        "bluetoothScan" to false,
        "location" to false,
    )) }

    LaunchedEffect(permissions) {
        Timber.d("permissions changed. Checking now.")
        if (permissions.values.all { it }) {
            done()
        }
    }

    LaunchedEffect(isBluetoothEnabled) {
        if (isBluetoothEnabled) {
            Timber.d("GetEnableBluetoothPermission is good.")
            val mutable = permissions.toMutableMap()
            mutable["bluetoothEnabled"] = true
            permissions = mutable.toMap()
        }
    }
    LaunchedEffect(hasBluetoothConnectPermission) {
        if (hasBluetoothConnectPermission) {
            Timber.d("GetBluetoothConnectPermission is good.")
            val mutable = permissions.toMutableMap()
            mutable["bluetoothConnect"] = true
            permissions = mutable.toMap()
        }
    }
    LaunchedEffect(hasBluetoothScanPermission) {
        if (hasBluetoothScanPermission) {
            Timber.d("GetBluetoothScanPermission is good.")
            val mutable = permissions.toMutableMap()
            mutable["bluetoothScan"] = true
            permissions = mutable.toMap()
        }
    }
    LaunchedEffect(hasFineLocationPermission) {
        if (hasFineLocationPermission) {
            Timber.d("FineLocationPermission is good.")
            val mutable = permissions.toMutableMap()
            mutable["location"] = true
            permissions = mutable.toMap()
        }
    }

    Column {
        if (isBluetoothEnabled) {
            Text("Bluetooth is Enabled")
        } else {
            Button(onClick = { enableBluetooth() }) {
                Text(text = "Allow bluetooth")
            }
        }

        if (hasBluetoothConnectPermission) {
            Text("Bluetooth connect is granted")
        } else {
            Text(if (bluetoothConnectPermissionShouldShowRationale) {
                // If the user has denied the permission but the rationale can be shown,
                // then gently explain why the app requires this permission
                "Bluetooth is important for this app. Please grant the permission."
            } else {
                // If it's the first time the user lands on this feature, or the user
                // doesn't want to be asked again for this permission, explain that the
                // permission is required
                "Bluetooth permission required for this feature to be available. " +
                        "Please grant the permission"
            })
            Button(onClick = {
                getBluetoothConnectPermission()
            }) {
                Text("Request bluetooth connect permission")
            }
        }

        if (hasBluetoothScanPermission) {
            Text("Bluetooth scan is granted")
        } else {
            Text(if (bluetoothScanPermissionShouldShowRationale) {
                // If the user has denied the permission but the rationale can be shown,
                // then gently explain why the app requires this permission
                "Bluetooth is important for this app. Please grant the permission."
            } else {
                // If it's the first time the user lands on this feature, or the user
                // doesn't want to be asked again for this permission, explain that the
                // permission is required
                "Bluetooth permission required for this feature to be available. " +
                        "Please grant the permission"
            })
            Button(onClick = {
                getBluetoothScanPermission()
            }) {
                Text("Request Bluetooth scan permission")
            }
        }

        if (hasFineLocationPermission) {
            Text("Fine location is granted")
        } else {
            Text(if (fineLocationPermissionShouldShowRationale) {
                // If the user has denied the permission but the rationale can be shown,
                // then gently explain why the app requires this permission
                "Bluetooth is important for this app. Please grant the permission."
            } else {
                // If it's the first time the user lands on this feature, or the user
                // doesn't want to be asked again for this permission, explain that the
                // permission is required
                "Bluetooth permission required for this feature to be available. " +
                        "Please grant the permission"
            })
            Button(onClick = {
                getFineLocationPermission()
            }) {
                Text("Request Fine Location permission")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GetAllBluetoothPermissionsPreview() {
    LinkUpTheme {
        Surface {
            GetAllBluetoothPermissions(
                isBluetoothEnabled = true,
                enableBluetooth= {},
            hasBluetoothConnectPermission= false,
            bluetoothConnectPermissionShouldShowRationale= false,
            getBluetoothConnectPermission= {},
            hasBluetoothScanPermission= false,
            bluetoothScanPermissionShouldShowRationale= false,
            getBluetoothScanPermission= {},
            hasFineLocationPermission= false,
            fineLocationPermissionShouldShowRationale=false,
            getFineLocationPermission= {},

            ) {

            }
        }
    }
}
