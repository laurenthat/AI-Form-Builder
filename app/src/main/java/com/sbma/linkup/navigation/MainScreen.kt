package com.sbma.linkup.navigation

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.sbma.linkup.api.apimodels.ApiUser
import com.sbma.linkup.api.apimodels.toUser
import com.sbma.linkup.application.AppViewModelProvider
import com.sbma.linkup.application.connectivity.InternetConnectionState
import com.sbma.linkup.intents.login.LoginResponseToken
import com.sbma.linkup.navigation.bottomnavigation.BottomNavigationBar
import com.sbma.linkup.presentation.components.NoInternetConnectionBarComponent
import com.sbma.linkup.presentation.screens.LoadingScreen
import com.sbma.linkup.presentation.screens.LoginScreen
import com.sbma.linkup.user.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
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
                }

                isLoading.value = false
            }
        }
    }

    if (loggedInUser.value == null || isLoading.value) {
        LoadingScreen()
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
