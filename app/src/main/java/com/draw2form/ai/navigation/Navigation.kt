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
import com.draw2form.ai.application.AppViewModelProvider
import com.draw2form.ai.application.connectivity.InternetConnectionState
import com.draw2form.ai.presentation.screens.FormsListScreen
import com.draw2form.ai.presentation.screens.HomeScreen
import com.draw2form.ai.presentation.screens.InstructionsScreen
import com.draw2form.ai.presentation.screens.ProcessingScreen
import com.draw2form.ai.presentation.screens.SettingsScreen
import com.draw2form.ai.presentation.screens.ShareFormScreen
import com.draw2form.ai.presentation.screens.SubmitResultScreen
import com.draw2form.ai.presentation.screens.SubmittedFormsScreen
import com.draw2form.ai.presentation.screens.UIComponent
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

    val scope = rememberCoroutineScope()

    val selectedForm by userViewModel.selectedForm.collectAsState()


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
            InstructionsScreen(onBackClick = {
                navController.popBackStack()
            })
        }

        composable("forms") {
            FormsListScreen {
                if (it.status == "DRAFT")
                    navController.navigate("forms/${it.id}/edit")
                else {
                    navController.navigate("forms/${it.id}")
                }

            }
        }

        composable(
            "forms/{formId}",
            arguments = listOf(navArgument("formId") { type = NavType.StringType }),
        ) { backStackEntry ->
            val formId = backStackEntry.arguments?.getString("formId")

            LaunchedEffect(true) {
                formId?.let {
                    userViewModel.getSubmittedForms(it)
                }
            }


            selectedForm?.let {
                SubmittedFormsScreen(
                    userViewModel = userViewModel,
                    canShare = true,
                    canGoBack = true,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onShareClick = {
                        composableScope.launch {
                            if (formId != null) {
                                userViewModel.publishForm(formId) {
                                    navController.navigate("forms/${formId}/publish")
                                }
                            }
                        }

                    },
                    form = it
                )
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
            route = "scanResult/{submitResult}",
            arguments = listOf(navArgument("submitResult") { type = NavType.BoolType }),
        ) {
            SubmitResultScreen(
                it.arguments?.getBoolean("submitResult") ?: false,
                onForwardClick = {
                    navController.navigate("home")
                }
            )
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
                    scope.launch {
                        userViewModel.swapUIComponents(apiUiElements.value ?: emptyList(), a, b)
                    }
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
                    it.textField?.let { textField ->
                        userViewModel.updateFormTextField(textField = textField)
                    }
                    it.checkbox?.let { checkbox ->
                        userViewModel.updateFormCheckbox(checkbox = checkbox)
                    }
                    it.toggleSwitch?.let { toggleSwitch ->
                        userViewModel.updateFormToggleSwitch(toggleSwitch = toggleSwitch)
                    }
                    it.button?.let { button ->
                        userViewModel.updateFormButton(button = button)
                    }
                },
                onUIComponentDelete = { deletedItem ->
                    when (deletedItem) {
                        is UIComponent -> {
                            when {
                                deletedItem.label != null -> userViewModel.deleteFormLabel(
                                    deletedItem.label
                                )

                                deletedItem.textField != null -> userViewModel.deleteFormTextField(
                                    deletedItem.textField
                                )

                                deletedItem.checkbox != null -> userViewModel.deleteFormCheckbox(
                                    deletedItem.checkbox
                                )

                                deletedItem.toggleSwitch != null -> userViewModel.deleteFormToggleSwitch(
                                    deletedItem.toggleSwitch
                                )

                                deletedItem.button != null -> userViewModel.deleteFormButton(
                                    deletedItem.button
                                )

                                deletedItem.image != null -> userViewModel.deleteFormImage(
                                    deletedItem.image
                                )
                            }
                        }
                    }
                },
                onAddUIComponent = { uiComponent ->
                    uiComponent.label?.let { formLabel ->
                        userViewModel.postFormLabel(
                            label = formLabel.copy(
                                formId = formId ?: ""
                            )
                        )
                    }

                    uiComponent.textField?.let { formTextField ->
                        userViewModel.postFormTextField(
                            textField = formTextField.copy(
                                formId = formId ?: ""
                            )
                        )
                    }

                    uiComponent.checkbox?.let { formCheckbox ->
                        userViewModel.postFormCheckbox(
                            checkbox = formCheckbox.copy(
                                formId = formId ?: ""
                            )
                        )
                    }

                    uiComponent.toggleSwitch?.let { formToggleSwitch ->
                        userViewModel.postFormToggleSwitch(
                            toggleSwitch = formToggleSwitch.copy(
                                formId = formId ?: ""
                            )
                        )
                    }

                    uiComponent.button?.let { formButton ->
                        userViewModel.postFormButton(
                            button = formButton.copy(
                                formId = formId ?: ""
                            )
                        )

                    }

                    uiComponent.image?.let { formImage ->
                        userViewModel.postFormImage(
                            image = formImage.copy(
                                formId = formId ?: ""
                            )
                        )
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                },
                onImageUIComponentUpdate = { component, file ->
                    component.image?.let { image ->
                        userViewModel.updateFormImage(image, file)
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
                canGoBack = false,
                onSuccessUpload = {
                    navController.navigate("forms/${it.id}/loading")
                },
                onBackClick = null,
                onFormClick = {
                    navController.navigate("forms/${it.id}/edit")
                },
                onShareClick = {
                    navController.navigate("forms/${it}/publish")
                }


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
                            apiUploadedFileState.value?.failed() == true
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
                state = apiUploadedFileState.value,
                onBackClick = {
                    navController.popBackStack()
                },
                onEditForm = {
                    formId?.let {
                        navController.navigate("forms/${it}/edit")
                    }
                })
        }


    }
}
