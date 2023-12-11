package com.draw2form.ai.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.draw2form.ai.api.NewFormSubmissionCheckboxResponse
import com.draw2form.ai.api.NewFormSubmissionRequestBody
import com.draw2form.ai.api.NewFormSubmissionTextFieldResponse
import com.draw2form.ai.api.NewFormSubmissionToggleSwitchResponse
import timber.log.Timber

@Composable
fun PublishedFormScreen(
    scannedFormState: List<UIComponent>,
    onInteraction: (List<UIComponent>) -> Unit,
    onSubmitClicked: (formSubmission: NewFormSubmissionRequestBody) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        itemsIndexed(scannedFormState) { index, uiComponent ->
            FormInteractionUI(
                element = uiComponent,
                onElementUpdated = { updatedComponent ->
                    val listCopy = scannedFormState.toMutableList()
                    listCopy[index] = updatedComponent
                    onInteraction(listCopy)
                },
                onSubmitClicked = {
                    val checkboxResponse = scannedFormState.mapNotNull { it.checkboxResponse }.map {
                        NewFormSubmissionCheckboxResponse(it.checkboxId, it.value)
                    }
                    val textFieldResponse =
                        scannedFormState.mapNotNull { it.textFieldResponse }.map {
                            NewFormSubmissionTextFieldResponse(it.textFieldId, it.value)
                        }
                    val toggleSwitchResponse =
                        scannedFormState.mapNotNull { it.toggleSwitchResponse }.map {
                            NewFormSubmissionToggleSwitchResponse(it.toggleSwitchId, it.value)
                        }

                    val newFormSubmissionRequestBody = NewFormSubmissionRequestBody(
                        textFieldResponse, checkboxResponse, toggleSwitchResponse
                    )

                    onSubmitClicked(
                        newFormSubmissionRequestBody
                    )
                }
            )
        }
    }
}


@Composable
fun FormInteractionUI(
    element: UIComponent,
    onElementUpdated: (UIComponent) -> Unit,
    onSubmitClicked: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardColors(Color.White, Color.Black, Color.Transparent, Color.Transparent),

        ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            element.label?.let {
                LabelComponent(it.label)
            }

            element.image?.let {
                FormAsyncImageComponent(
                    url = it.url ?: "https://picsum.photos/id/20/1000/500",
                    modifier = Modifier.fillMaxSize()
                )
            }

            element.textField?.let { apiField ->
                TextFieldComponent(
                    label = apiField.label,
                    value = element.textFieldResponse?.value ?: "",
                    readonly = false,
                    onValueChange = { text ->
                        onElementUpdated(
                            element.copy(
                                textFieldResponse = element.textFieldResponse?.copy(
                                    value = text
                                )
                            )
                        )
                    }
                )
            }

            element.checkbox?.let {
                CheckboxComponent(
                    label = it.label,
                    isChecked = element.checkboxResponse?.value ?: false,
                    enabled = true,
                    onCheckedChange = { isChecked ->

                        onElementUpdated(
                            element.copy(
                                checkboxResponse = element.checkboxResponse?.copy(
                                    value = isChecked
                                )
                            )
                        )
                    }
                )
            }

            element.toggleSwitch?.let {
                ToggleSwitchComponent(
                    label = it.label,
                    isChecked = element.toggleSwitchResponse?.value ?: false,
                    enabled = true,
                    onCheckedChange = { isChecked ->
                        onElementUpdated(
                            element.copy(
                                toggleSwitchResponse = element.toggleSwitchResponse?.copy(
                                    value = isChecked
                                )
                            )
                        )
                    }
                )
            }

            element.button?.let {
                DynamicFormButtonComponent(
                    label = it.label,
                    enabled = true,
                    onClick = {
                        onSubmitClicked()

                    }


                )
            }
        }
    }
}

@Composable
fun LabelComponent(label: String) {
    Text(
        text = label,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        textAlign = TextAlign.Center
    )
}

@Composable
fun FormAsyncImageComponent(url: String, modifier: Modifier) {
    AsyncImage(
        model = url,
        contentDescription = "profile photo",
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}

@Composable
fun TextFieldComponent(
    label: String,
    value: String,
    readonly: Boolean,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        placeholder = { Text(text = label, color = Color.Black) },
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        readOnly = readonly
    )
}

@Composable
fun CheckboxComponent(
    label: String,
    isChecked: Boolean,
    enabled: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label)
        androidx.compose.material3.Checkbox(
            checked = isChecked,
            onCheckedChange = {
                onCheckedChange(it)
            },
            modifier = Modifier.padding(8.dp),
            enabled = enabled
        )
    }
}

@Composable
fun ToggleSwitchComponent(
    label: String,
    isChecked: Boolean,
    enabled: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label)
        Switch(
            checked = isChecked,
            enabled = enabled,
            onCheckedChange = onCheckedChange,
        )
    }
}

@Composable
fun DynamicFormButtonComponent(label: String, enabled: Boolean, onClick: () -> Unit) {
    Button(
        onClick = {
            Timber.d("Button clicked")
            onClick()
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        enabled = enabled
    ) {
        Text(text = label, modifier = Modifier.padding(8.dp))
    }
}


