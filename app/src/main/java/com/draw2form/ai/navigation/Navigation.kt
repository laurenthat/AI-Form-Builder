package com.draw2form.ai.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.draw2form.ai.api.ApiUploadedFileState
import com.draw2form.ai.application.AppViewModelProvider
import com.draw2form.ai.application.connectivity.InternetConnectionState
import com.draw2form.ai.presentation.screens.DynamicUI
import com.draw2form.ai.presentation.screens.HomeScreen
import com.draw2form.ai.presentation.screens.ProcessingScreen
import com.draw2form.ai.presentation.screens.SettingsScreen
import com.draw2form.ai.user.User
import com.draw2form.ai.user.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow


@Composable
fun Navigation(
    navController: NavHostController,
    user: User,
    internetConnectionStateFlow: StateFlow<InternetConnectionState>,
    userViewModel: UserViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier,

) {
    NavHost(
        navController,
        modifier = modifier,
        startDestination = "home"
    ) {

        /**
         * tab of the bottom navigation bar
         */
        composable("settings") {
            SettingsScreen()
        }

        /**
         * tab of the bottom navigation bar
         * Navigates to {UserShareScreenProvider}  when user clicks on Share button.
         */
        composable("home") {
            HomeScreen(
                user,
                canEdit = true,
                onEditClick = { navController.navigate("profile/edit") },
                canGoBack = false,
                onSuccessUpload = {
                    navController.navigate("upload/${it.id}/loading")
                },
                onBackClick = null,

                )
        }
        composable(
            "upload/{uploadId}/loading",
            arguments = listOf(navArgument("uploadId") { type = NavType.StringType }),

            ) { backStackEntry ->
            var counter by remember { mutableStateOf(0) }

            val uploadId = backStackEntry.arguments?.getString("uploadId")
            val apiUploadedFileState = userViewModel.apiUploadedFileState.collectAsState(initial = null)

            LaunchedEffect(true) {
                uploadId?.let {
                    while (true) {
                        // Update the counter every 1 second (1000 milliseconds)
                        delay(1000)
                        userViewModel.getUploadedFileState(it)

                        counter++
                        if (
                            apiUploadedFileState.value?.formGeneration == "success" &&
                            apiUploadedFileState.value?.objectRecognition == "success" &&
                            apiUploadedFileState.value?.textRecognition == "success"
                        ) {
                            break
                        }
                        if (counter > 100) {
                            break
                        }
                    }
                }
            }

            ProcessingScreen(apiUploadedFileState.value ?: ApiUploadedFileState("loading", "loading", "loading")) {
                uploadId?.let {
                    navController.navigate("upload/${it}/edit")
                }
            }
        }



        composable(
            "upload/{uploadId}/edit",
            arguments = listOf(navArgument("uploadId") { type = NavType.StringType }),

            ) { backStackEntry ->
            val uploadId = backStackEntry.arguments?.getString("uploadId")
            val apiUiElements = userViewModel.apiUiElements.collectAsState(initial = null)

            LaunchedEffect(true) {
                uploadId?.let {
                    userViewModel.getUploadedFileDetails(it)
                }
            }

            DynamicUI(apiUiElements.value ?: listOf())
        }
    }
}
