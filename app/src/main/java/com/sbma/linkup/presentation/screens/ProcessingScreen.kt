package com.sbma.linkup.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sbma.linkup.R
import com.sbma.linkup.presentation.ui.theme.LinkUpTheme


data class ImageProcessState(
    val objectRecognition: String,
    val textRecognition: String,
    val formGeneration: String,

    )

@Composable
fun ImageProcessStatus(state: String, label: String) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_success))

    when (state) {
        "loading" ->
            Row(
                modifier = Modifier
                    .padding(20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LottieAnimation(
                    composition = composition,
                    iterations = LottieConstants.IterateForever,

                    )
                Text(text = label, textAlign = TextAlign.Center, fontSize = 16.sp)
            }

        "success" ->
            Row(
                modifier = Modifier
                    .padding(20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LottieAnimation(
                    composition = composition,
                    iterations = LottieConstants.IterateForever
                )
                Text(text = label, textAlign = TextAlign.Center, fontSize = 16.sp)

            }

        "error" ->
            Row(
                modifier = Modifier
                    .padding(20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LottieAnimation(
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                )
                Text(text = label, textAlign = TextAlign.Center, fontSize = 16.sp)
            }

    }

}

@Composable
fun ViewInterface(onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(30.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { onClick() }) {
            Text(text = "View Form")

        }
    }
}

@Composable
fun ProcessingScreen() {
    LazyColumn() {
        item {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "PROCESS THE FORM TO AN INTERFACE",
                    textAlign = TextAlign.Center,
                    fontSize = 15.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                val apiResponse = ImageProcessState("error", "loading", "success")

                ImageProcessStatus(apiResponse.objectRecognition, "Object Recognition")
                ImageProcessStatus(apiResponse.textRecognition, "Text Recognition")
                ImageProcessStatus(apiResponse.formGeneration, "Form Generation")

                ViewInterface(onClick = {})


            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun ProcessingScreenPreview() {
    LinkUpTheme {
        ProcessingScreen()
    }
}

