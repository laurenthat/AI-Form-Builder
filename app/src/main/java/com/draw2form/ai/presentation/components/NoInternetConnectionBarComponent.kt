package com.draw2form.ai.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.draw2form.ai.application.connectivity.InternetConnectionState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun NoInternetConnectionBarComponent(internetConnectionState: StateFlow<InternetConnectionState>) {
    var initialState by rememberSaveable { mutableStateOf(internetConnectionState.value.isConnected()) }
    var showNoInternet by rememberSaveable { mutableStateOf(false) }
    var showWarning by rememberSaveable { mutableStateOf(false) }
    var showBackOnline by rememberSaveable { mutableStateOf(false) }

    val animationTime = 300

    LaunchedEffect(key1 = Unit) {
        internetConnectionState.collectLatest { state ->
            //  Giving user some time to land.
            delay(2000)
            //  If  user entered the application in offline mode and internet is back then let's show "back online".
            if (!initialState && state.isConnected()) {
                showBackOnline = true
            }
            // If App is offline let's reset the initialState for the next change so we can show the back online when user is back online.
            if (!state.isConnected()) {
                initialState = false
            }
            showNoInternet = !state.isConnected()
            delay(3000)
            showBackOnline = false
            showNoInternet = false
            showWarning = !state.isConnected()
            delay(4000)
            showWarning = false

        }
    }

    AnimatedVisibility(
        visible = showNoInternet,
        enter = slideInVertically(
            initialOffsetY = {
                -100
            },
            animationSpec = tween(
                durationMillis = animationTime,
                easing = LinearEasing // interpolator
            )
        ),
        exit = slideOutVertically(
            targetOffsetY = {
                -100
            },
            animationSpec = tween(
                durationMillis = animationTime,
                easing = LinearEasing // interpolator
            )
        ),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                .fillMaxWidth()
                .background(Color.Red)
        ) {
            Text(text = "No internet connection", color = Color.White)
        }
    }


    AnimatedVisibility(
        visible = showWarning,
        enter = slideInVertically(
            initialOffsetY = {
                -100
            },
            animationSpec = tween(
                durationMillis = animationTime,
                easing = LinearEasing // interpolator
            )
        ),
        exit = slideOutVertically(
            targetOffsetY = {
                -100
            },
            animationSpec = tween(
                durationMillis = animationTime,
                easing = LinearEasing // interpolator
            )
        ),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                .fillMaxWidth()
                .background(Color.Red)
        ) {
            Text(text = "Some services might not be available", color = Color.White)
        }
    }

    AnimatedVisibility(
        visible = showBackOnline,
        enter = slideInVertically(
            initialOffsetY = {
                -100
            },
            animationSpec = tween(
                durationMillis = animationTime,
                easing = LinearEasing // interpolator
            )
        ),
        exit = slideOutVertically(
            targetOffsetY = {
                -100
            },
            animationSpec = tween(
                durationMillis = animationTime,
                easing = LinearEasing // interpolator
            )
        ),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                .fillMaxWidth()
                .background(Color.Green)
        ) {
            Text(text = "Back Online", color = Color.White)
        }
    }

}