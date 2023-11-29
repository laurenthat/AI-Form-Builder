package com.draw2form.ai.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.draw2form.ai.api.ApiForm
import com.draw2form.ai.presentation.screens.editform.DynamicUI
import com.draw2form.ai.user.UserViewModel
import timber.log.Timber


@Composable
fun PublishedFormScreen(
    scannedFormState: ApiForm,
    //userViewModel: UserViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            scannedFormState.let { scannedForm ->
                val uiComponents = convertApiFormToUIComponents(scannedForm)

                items(uiComponents) { uiComponent ->
                    DynamicUI(element = uiComponent)
                    //FillFormUI(uiComponent, userViewModel)
                }
            }
        }
    }
}

fun convertApiFormToUIComponents(apiForm: ApiForm): List<UIComponent> {
    val checkboxes = apiForm.checkboxes ?: listOf()
    val textFields = apiForm.textFields ?: listOf()
    val toggleSwitches = apiForm.toggleSwitches ?: listOf()
    val buttons = apiForm.buttons ?: listOf()
    val labels = apiForm.labels ?: listOf()
    val images = apiForm.images ?: listOf()

    val uiElements = mutableListOf<UIComponent>()

    uiElements.addAll(checkboxes.map { UIComponent(checkbox = it) })
    uiElements.addAll(textFields.map { UIComponent(textField = it) })
    uiElements.addAll(toggleSwitches.map { UIComponent(toggleSwitch = it) })
    uiElements.addAll(buttons.map { UIComponent(button = it) })
    uiElements.addAll(labels.map { UIComponent(label = it) })
    uiElements.addAll(images.map { UIComponent(image = it) })
    uiElements.sortBy { it.order() }

    return uiElements
}

@Composable
fun FillFormUI(
    element: UIComponent,
    userViewModel: UserViewModel
) {
    Timber.d("Fill form UI: $element")
    /* val uiComponentState = remember {
         when {
             element.textField != null -> {
                 val response =
                     formSubmission.textFieldResponses?.find { it.textFieldId == element.textField.id }
                 UiComponentState.TextFieldState(response?.value ?: "")
             }

             element.checkbox != null -> {
                 val response =
                     formSubmission.checkboxResponse?.find { it.checkboxId == element.checkbox.id }
                 UiComponentState.CheckboxState(response?.value?.toBoolean() ?: false)
             }

             element.button != null -> {
                 UiComponentState.ButtonState(false)
             }

             element.toggleSwitch != null -> {
                 val response =
                     formSubmission.toggleSwitchResponses?.find { it.toggleSwitchId == element.toggleSwitch.id }
                 UiComponentState.ToggleSwitchState(response?.value?.toBoolean() ?: false)
             }

             else -> throw IllegalArgumentException("Unsupported UIComponent type")
         }


     }*/

    val uiComponentState = when {
        element.label != null -> {
            val value = userViewModel.getFormFieldValue(element.label.id) as? String ?: ""
            UiComponentState.TextFieldState(value)

        }

        element.textField != null -> {
            val value = userViewModel.getFormFieldValue(element.textField.id) as? String ?: ""
            UiComponentState.TextFieldState(value)
        }

        element.checkbox != null -> {
            val value = userViewModel.getFormFieldValue(element.checkbox.id) as? Boolean ?: false
            UiComponentState.CheckboxState(value)
        }

        element.button != null -> {
            UiComponentState.ButtonState(false)
        }

        element.toggleSwitch != null -> {
            val value =
                userViewModel.getFormFieldValue(element.toggleSwitch.id) as? Boolean ?: false
            UiComponentState.ToggleSwitchState(value)
        }

        else -> {
            Timber.e("Unsupported UIComponent type: ${element.javaClass.simpleName}")
            throw IllegalArgumentException("Unsupported UIComponent type: ${element.javaClass.simpleName}")
                .apply { printStackTrace() }
        }
    }

    Box {
        Row(verticalAlignment = Alignment.CenterVertically) {
            when {
                element.image != null -> {
                    val imageUrl = element.image.url ?: "https://placekitten.com/1000/500?image=12"
                    FormAsyncImage(
                        url = imageUrl,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                element.textField != null -> {
                    TextField(
                        value = (uiComponentState as UiComponentState.TextFieldState).text,
                        onValueChange = { newText ->
                            userViewModel.updateFormFieldValue(
                                element.textField.id,
                                newText
                            )
                        },
                        label = { Text(element.textField.label) }
                    )
                }

                element.checkbox != null -> {
                    Checkbox(
                        checked = (uiComponentState as UiComponentState.CheckboxState).checked,
                        onCheckedChange = { isChecked ->
                            userViewModel.updateFormFieldValue(
                                element.checkbox.id,
                                isChecked
                            )
                        },
                    )
                }

                element.toggleSwitch != null -> {
                    Switch(
                        checked = (uiComponentState as UiComponentState.ToggleSwitchState).switched,
                        onCheckedChange = { isChecked ->
                            userViewModel.updateFormFieldValue(
                                element.toggleSwitch.id,
                                isChecked
                            )
                        },
                    )
                }

                element.button != null -> {
                    Button(
                        onClick = {
                            (uiComponentState as UiComponentState.ButtonState).clicked = true
                            userViewModel.handleButtonClick()
                        },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(text = element.button.label)
                    }
                }

                else -> throw IllegalArgumentException("Unsupported UIComponent type")
            }
        }
    }


}

sealed class UiComponentState {
    data class TextFieldState(val text: String) : UiComponentState()
    data class CheckboxState(val checked: Boolean) : UiComponentState()
    data class ButtonState(var clicked: Boolean) : UiComponentState()
    data class ToggleSwitchState(val switched: Boolean) : UiComponentState()


}