package com.draw2form.ai.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.draw2form.ai.presentation.screens.FormsListScreen
import com.draw2form.ai.presentation.screens.HomeScreen
import com.draw2form.ai.presentation.screens.InstructionsScreen
import com.draw2form.ai.presentation.screens.ProcessingScreen
import com.draw2form.ai.presentation.screens.SettingsScreen
import com.draw2form.ai.presentation.screens.ShareFormScreen
import com.draw2form.ai.presentation.screens.editform.FormEditScreen
import com.draw2form.ai.user.User
import com.draw2form.ai.user.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


@Composable
fun Navigation(
    navController: NavHostController,
    user: User,
    internetConnectionStateFlow: StateFlow<InternetConnectionState>,
    userViewModel: UserViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier,

    ) {
    val composableScope = rememberCoroutineScope()

    NavHost(
        navController,
        modifier = modifier,
        startDestination = "home"
    ) {

        /**
         * tab of the bottom navigation bar
         */
        composable("settings") {
            SettingsScreen(
                onInstButtonClicked = {
                    navController.navigate("instructions")
                }
            )
        }
        composable("instructions") {
            InstructionsScreen()
        }

        composable("forms") {
            FormsListScreen {
                navController.navigate("forms/${it.id}/edit")
            }
        }
        composable(
            "forms/{formId}/publish",
            arguments = listOf(navArgument("formId") { type = NavType.StringType }),
        ) { backStackEntry ->
            val formId = backStackEntry.arguments?.getString("formId")
            if (formId != null) {
                ShareFormScreen(formId, onBackClick = {
                    navController.popBackStack()
                })
            }
        }

        composable(
            "forms/{formId}/edit",
            arguments = listOf(navArgument("formId") { type = NavType.StringType }),
        ) { backStackEntry ->
            val formId = backStackEntry.arguments?.getString("formId")
            val apiUiElements = userViewModel.apiUiElements.collectAsState(initial = null)

            LaunchedEffect(true) {
                formId?.let {
                    userViewModel.getFormDetails(it)
                }
            }
            FormEditScreen(apiUiElements.value ?: emptyList(),
                onMove = { a, b ->
            },
                onPublish = {
                    composableScope.launch {
                        if (formId != null) {
                            userViewModel.publishForm(formId) {
                                navController.navigate("forms/${formId}/publish")

                            }
                        }
                    }

                },
                onUIComponentUpdate = {
                    it.label?.let { formLabel ->
                        userViewModel.updateFormLabel(formLabel = formLabel)
                    }
                }
            )
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
                    navController.navigate("forms/${it.id}/loading")
                },
                onBackClick = null,

                )
        }
        composable(
            "forms/{formId}/loading",
            arguments = listOf(navArgument("formId") { type = NavType.StringType }),

            ) { backStackEntry ->
            var counter by remember { mutableStateOf(0) }

            val formId = backStackEntry.arguments?.getString("formId")
            val apiUploadedFileState =
                userViewModel.apiUploadedFileState.collectAsState(initial = null)

            LaunchedEffect(true) {
                formId?.let {
                    while (true) {
                        // Update the counter every 1 second (1000 milliseconds)
                        delay(1000)
                        userViewModel.getFormStatus(it)

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

            ProcessingScreen(
                apiUploadedFileState.value ?: ApiUploadedFileState(
                    "loading",
                    "loading",
                    "loading"
                )
            ) {
                formId?.let {
                    navController.navigate("forms/${it}/edit")
                }
            }
        }


    }
}
