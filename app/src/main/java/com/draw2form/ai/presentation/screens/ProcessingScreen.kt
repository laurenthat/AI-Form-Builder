package com.draw2form.ai.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.draw2form.ai.R
import com.draw2form.ai.api.ApiUploadedFileState

@Composable
fun ImageProcessStatus(state: String, label: String) {

    ListItem(
        headlineContent = {
            Text(text = label)
        },
        leadingContent = {
            ProcessingLottieAnimation(state)
        }
    )
}

@Composable
fun ProcessingLottieAnimation(state: String) {
    val successComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_success))
    val loadingComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_loading))
    val errorComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_error))

    LottieAnimation(
        composition = when (state) {
            "success" -> successComposition
            "error" -> errorComposition
            "loading" -> loadingComposition
            else -> loadingComposition
        },
        iterations = when (state) {
            "success" -> 1
            "error" -> 1
            "loading" -> 100
            else -> 100
        },
        modifier = Modifier.size(30.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProcessingScreen(state: ApiUploadedFileState?, onEditForm: () -> Unit, onBackClick: () -> Unit) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ), title = {
                    Text(
                        text = "Processing your image",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 20.sp,
                    )
                },
                navigationIcon = {
                    IconButton(
                        modifier = Modifier,
                        onClick = { onBackClick() },
                        enabled = state?.failed() == true

                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        modifier = Modifier,
                        onClick = { onEditForm() },
                        enabled = state?.succeeded() == true
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Next"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            Text(
                "Image processing might take 10-30 seconds. Please wait patiently.",
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp),
            )
            LazyColumn(
                modifier = Modifier
                    .padding(start = 5.dp, end = 5.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(1.dp),
            ) {
                itemsIndexed(state?.items() ?: listOf()) { index, item ->
                    ImageProcessStatus(item.first, item.second)
                }
            }

        }
    }


    // val waitComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.please_wait))
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(20.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//
//        Text("Processing your image", fontSize = 20.sp)
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp)
//                .border(2.dp, Color.Gray, shape = RectangleShape)
//
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize(),
////                    .padding(top = 20.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                state?.let {
//                    ImageProcessStatus(state.ObjectDetectionResponseReceived, "Object Detection Response Received")
//                    ImageProcessStatus(state.TextDetectionResponseReceived, "Text Detection Response Received")
//                    ImageProcessStatus(state.PredictionsUnified, "Predictions Unified")
//                    ImageProcessStatus(state.UnifiedPredictionCoordinatesRounded, "Unified Prediction Coordinates Rounded")
//                    ImageProcessStatus(state.UnifiedPredictionsLeveledInYAxis, "Unified Predictions Leveled In Y Axis")
//                    ImageProcessStatus(state.ChatGPT4ImageDescribed, "ChatGPT 4 Image Described")
//                    ImageProcessStatus(state.ChatGPT3P5JsonGenerated, "ChatGPT 3.5 Json Generated")
//                    ImageProcessStatus(state.FormComponentsCreated, "Form Components Created")
//                    Spacer(modifier = Modifier.height(50.dp))
//                    Button(
//                        enabled = state.succeeded(),
//                        onClick = {
//                            onEditForm()
//                        }
//                    ) {
//                        Text(text = "View Form")
//                    }
//                }
//
//                Text(
//                    "Image processing might take 5-15 seconds. Please wait patiently.",
//                    modifier = Modifier.padding(top = 50.dp),
//                    textAlign = TextAlign.Center
//                )
//            }
//        }
//    }
}

