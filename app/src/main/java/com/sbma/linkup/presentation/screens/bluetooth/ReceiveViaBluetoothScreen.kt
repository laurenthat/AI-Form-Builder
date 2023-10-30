package com.sbma.linkup.presentation.screens.bluetooth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sbma.linkup.R
import com.sbma.linkup.application.AppViewModelProvider
import com.sbma.linkup.bluetooth.AppBluetoothViewModel
import com.sbma.linkup.user.UserViewModel
import com.sbma.linkup.util.uuidOrNull
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@Composable
fun ReceiveViaBluetoothScreenProvider(
    appBluetoothViewModel: AppBluetoothViewModel = viewModel(factory = AppViewModelProvider.Factory),
    userViewModel: UserViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onSuccess: () -> Unit,
    onFailure: () -> Unit,

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
            var idReceived by rememberSaveable {
                mutableStateOf(false)
            }
            LaunchedEffect(true) {
                Timber.d("Collecting  state now!")

                appBluetoothViewModel.state.collectLatest {state ->
                    if (idReceived) {
                        return@collectLatest
                    }
                    Timber.d("Collect state")
                    state.messages.lastOrNull()?.let { message ->
                    Timber.d("Collect message")
                        message.message.uuidOrNull()?.let { shareId ->
                            Timber.d("ShareId received:")
                            idReceived = true
                            userViewModel.scanShareId(
                                id = shareId,
                                onSuccess = {
                                    onSuccess()
                                },
                                onFailure = {
                                    onFailure()
                                }

                            )
                        }
                    }
                }
            }
            ReceiveViaBluetoothScreen {
                appBluetoothViewModel.launchMakeBluetoothDiscoverable()
                appBluetoothViewModel.startServer()
            }
        }
    }
}

@Composable
fun ReceiveViaBluetoothScreen(
    startServer: () -> Unit
) {
    val scanningLottie by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_bluetooth_scanning))

    LaunchedEffect(true){
        startServer()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Waiting for connection",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LottieAnimation(
            composition = scanningLottie,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier.size(250.dp)
        )
    }

}