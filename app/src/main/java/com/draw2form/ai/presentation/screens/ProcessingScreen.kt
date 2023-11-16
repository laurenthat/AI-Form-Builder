package com.draw2form.ai.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.draw2form.ai.presentation.ui.theme.LinkUpTheme
import com.draw2form.ai.R
import com.draw2form.ai.api.ApiUploadedFileState

@Composable
fun ImageProcessStatus(state: String, label: String) {

    val successComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_success))
    val loadingComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_loading))
    val errorComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_error))


    when (state) {
        "loading" ->
            Row(
                modifier = Modifier
                    .padding(20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LottieAnimation(
                    composition = loadingComposition,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier.size(50.dp)

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
                    composition = successComposition,
                    iterations =  1,
                    modifier = Modifier.size(50.dp)
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
                    composition = errorComposition,
                    iterations = 1,
                    modifier = Modifier.size(50.dp)
                )
                Text(text = label, textAlign = TextAlign.Center, fontSize = 16.sp)

            }

    }

}

@Composable
fun ViewInterface(onClick: () -> Unit) {

}

@Composable
fun ProcessingScreen(state: ApiUploadedFileState, onEditForm: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ImageProcessStatus(state.objectRecognition, "Object Recognition")
        ImageProcessStatus(state.textRecognition, "Text Recognition")
        ImageProcessStatus(state.formGeneration, "Form Generation")

        Button(
            enabled = state.formGeneration == "success"
                    && state.textRecognition == "success"
                    && state.objectRecognition == "success",
            onClick = {
                onEditForm()
            }

        ) {
            Text(text = "View Form")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ProcessingScreenPreview() {
    LinkUpTheme {
        ProcessingScreen(ApiUploadedFileState("success", "success", "success")) {

        }
    }
}

