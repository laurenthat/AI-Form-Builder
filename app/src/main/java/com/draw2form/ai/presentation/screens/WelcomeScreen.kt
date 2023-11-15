package com.draw2form.ai.presentation.screens

import android.content.ClipData.Item
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.draw2form.ai.R
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(
    onClick: (() -> Unit),
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_welcome))
    LazyColumn() {
        item {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Divider(thickness = 1.dp, color = Color.Gray)
                Spacer(modifier = Modifier.height(50.dp))
                Text(
                    text = "AI FORM BUILDER",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(50.dp))
                Divider(thickness = 1.dp, color = Color.Gray)

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .size(width = 300.dp, height = 300.dp)
                ) {
                    LottieAnimation(
                        composition = composition,
                        iterations = LottieConstants.IterateForever,
                    )

                }
                Text(
                    text = "Transforming Hand Drawn WireFrames Into Forms and Interfaces",
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(50.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedButton(
                    onClick = onClick,
                    modifier = Modifier
                        .padding(20.dp),
                    shape = RoundedCornerShape(30.dp),
                ) {
                    Text("Get Started", style = MaterialTheme.typography.labelMedium)
                }


            }
        }
    }

    LaunchedEffect(Unit) {
        delay(2000)
    }
}


@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen(onClick = {})
}