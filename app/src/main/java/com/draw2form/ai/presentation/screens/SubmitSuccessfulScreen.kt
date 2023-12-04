package com.draw2form.ai.presentation.screens

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
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.draw2form.ai.R

@Composable
fun SubmitSuccessfulScreen(submitResult: Boolean) {
    val successQrScan by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.form_submitted_animation))
    val failedQrScan by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.failed_qr_scan))

    if (submitResult) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Form submitted Successfully",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LottieAnimation(
                composition = successQrScan,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier.size(250.dp)
            )
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Failed to submit your form. Something went wrong",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LottieAnimation(
                composition = failedQrScan,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier.size(250.dp)
            )
        }
    }
}
