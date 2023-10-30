package com.sbma.linkup.presentation.screens.bluetooth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sbma.linkup.R

@Composable
fun BluetoothShareAndReceiveResultScreen(isReceiveMode: Boolean, succeeded: Boolean) {
    val successLottie by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_bluetooth_success))
    val failedLottie by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_error))


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (succeeded) when (isReceiveMode) {
                true -> "Connection complete! Cards received successfully."
                false -> "Connection complete! Cards shared successfully."
            } else when (isReceiveMode) {
                true -> "Sorry, we couldn't establish a Bluetooth connection. Please try again."
                false -> "Sorry, we couldn't establish a Bluetooth connection. Please try again."
            },
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LottieAnimation(
            composition = if (succeeded) successLottie else failedLottie,
            modifier = Modifier.size(250.dp)
        )
    }
}