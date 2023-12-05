package com.draw2form.ai.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.draw2form.ai.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubmitResultTopBar(
) {
    MediumTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(
                text = "Submission Result",
                style = MaterialTheme.typography.labelLarge,
                fontSize = 20.sp
            )
        },
    )
}

@Composable
fun SubmitResultScreen(submitResult: Boolean, onForwardClick: () -> Unit) {
    val successQrScan by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.form_submitted_animation))
    val failedQrScan by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.failed_qr_scan))


    Scaffold(
        topBar = {
            SubmitResultTopBar()
        },
        modifier = Modifier
            .fillMaxSize()
    ) { padding ->
        if (submitResult) {


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
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
                    modifier = Modifier.size(300.dp)
                )

                Button(
                    onClick = { onForwardClick() },
                    modifier = Modifier
                        .height(60.dp)
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                ) {
                    Text(
                        text = "Continue",
                        fontSize = 18.sp,
                        modifier = Modifier
                            .fillMaxSize(),
                        textAlign = TextAlign.Center
                    )
                }


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
                    modifier = Modifier
                        .size(250.dp)
                        .padding(bottom = 40.dp)
                )

                Button(
                    onClick = { onForwardClick() },
                    modifier = Modifier
                        .height(60.dp)
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                ) {
                    Text(
                        text = "Continue",
                        fontSize = 18.sp,
                        modifier = Modifier
                            .fillMaxSize(),
                        textAlign = TextAlign.Center
                    )
                }

            }
        }
    }
}


