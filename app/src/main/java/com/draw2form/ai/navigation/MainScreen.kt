package com.draw2form.ai.navigation

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.draw2form.ai.api.ApiUser
import com.draw2form.ai.api.LoginResponseToken
import com.draw2form.ai.api.toUser
import com.draw2form.ai.application.AppViewModelProvider
import com.draw2form.ai.application.connectivity.InternetConnectionState
import com.draw2form.ai.navigation.bottomnavigation.BottomNavigationBar
import com.draw2form.ai.presentation.components.NoInternetConnectionBarComponent
import com.draw2form.ai.presentation.screens.LoadingScreen
import com.draw2form.ai.presentation.screens.LoginScreen
import com.draw2form.ai.presentation.screens.WelcomeScreen
import com.draw2form.ai.user.UserViewModel
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    intent: Intent,
    internetConnectionStateFlow: StateFlow<InternetConnectionState>,
    userViewModel: UserViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val navController = rememberNavController()
    val loggedInUser = userViewModel.getLoggedInUserProfile.collectAsState(initial = null)
    val scannedForm = userViewModel.scannedForm.collectAsState(initial = null)
    val welcomeScreenSeen = userViewModel.welcomeScreenSeen.collectAsState(initial = null)
    val composableScope = rememberCoroutineScope()

    // show loading screen at first if there is an intent
    val isLoading =
        rememberSaveable { mutableStateOf(intent.action != null && intent.data != null) }

    LaunchedEffect(true) {
        // Check if there is an intent action
        intent.action?.let { action ->
            // Check if there is an intent data
            intent.data?.let { data ->

                val path = data.path ?: ""

                Timber.d("intent.action: $action")
                Timber.d("intent.data: $data")
                Timber.d("intent.path: $path")

                // Check if it is a login intent
                if (path == "/android/auth/login") {
                    val tokenQuery = data.getQueryParameter("token")
                    val userQuery = data.getQueryParameter("user")
                    val token = Gson().fromJson(tokenQuery, LoginResponseToken::class.java)
                    val apiUser = Gson().fromJson(userQuery, ApiUser::class.java)
                    val user = apiUser.toUser()
                    userViewModel.insertItem(user)
                    userViewModel.saveLoginData(
                        accessToken = token.accessToken,
                        expiresAt = token.expiresAt,
                        userId = user.id
                    )
                    delay(1000)
                } else if (path == "/android/forms/publish") {
                    val formTokenQuery = data.getQueryParameter("id")
                    Timber.d(" Form Id from the url: $formTokenQuery")
                    if (formTokenQuery != null) {
                        userViewModel.getForm(formTokenQuery)

                    }

                }

                isLoading.value = false
            }
        }
    }

    if (loggedInUser.value == null || isLoading.value) {
        LoadingScreen()
        
    } else if (scannedForm.value != null) {
        Text("Form filling component: ${scannedForm.value}")

    } else if (welcomeScreenSeen.value == false) {
        WelcomeScreen(onClick = {
            run {
                composableScope.launch {
                    userViewModel.setWelcomeScreenSeen()
                }
            }
        })
    } else if (loggedInUser.value!!.isEmpty()) {
        LoginScreen()
    } else {
        LaunchedEffect(true) {
            userViewModel.syncRoomDatabase()
        }
        Scaffold(
            bottomBar = { BottomNavigationBar(navController = navController) },
            topBar = { NoInternetConnectionBarComponent(internetConnectionStateFlow) }
        ) {
            Navigation(
                navController,
                loggedInUser.value!!.first(),
                internetConnectionStateFlow = internetConnectionStateFlow,
                modifier = Modifier.padding(it)
            )
        }
    }
}
