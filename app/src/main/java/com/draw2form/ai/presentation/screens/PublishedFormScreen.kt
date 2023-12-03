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
import com.draw2form.ai.api.ApiFormTextFieldResponse
import com.draw2form.ai.api.NewFormSubmissionCheckboxResponse
import com.draw2form.ai.api.NewFormSubmissionRequestBody
import com.draw2form.ai.api.NewFormSubmissionTextFieldResponse
import com.draw2form.ai.api.NewFormSubmissionToggleSwitchResponse
import timber.log.Timber

@Composable
fun PublishedFormScreen(
    scannedFormState: List<UIComponent>,
    onInteraction: (List<UIComponent>) -> Unit,
    onSubmitClicked: (id: String, formSubmission: NewFormSubmissionRequestBody) -> Unit
) {
    Timber.d("Scanned form state: $scannedFormState")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        itemsIndexed(scannedFormState) { index, uiComponent ->
            Timber.d("Scanned form state: $scannedFormState")

            FormInteractionUI(
                element = uiComponent,
                onInteraction = { updatedComponent ->
                    val listCopy = scannedFormState.toMutableList()
                    listCopy[index] = updatedComponent
                    onInteraction(listCopy)
                    Timber.d("updated Component: $updatedComponent")
                },
                scannedFormState,
                onSubmitClicked = { formId, formSubmission ->
                    onSubmitClicked(formId, formSubmission)
                }
            )
        }
    }
}


@Composable
fun FormInteractionUI(
    element: UIComponent,
    onInteraction: (UIComponent) -> Unit,
    storedComponents: List<UIComponent>,
    onSubmitClicked: (id: String, formSubmission: NewFormSubmissionRequestBody) -> Unit
) {
    Timber.d("Stored Components:$storedComponents")

    val textFieldRes: ApiFormTextFieldResponse? = storedComponents
        .mapNotNull { it.textFieldResponse }
        .firstOrNull()
    val tfId = storedComponents.firstNotNullOfOrNull { it.textFieldResponse?.id ?: "" }
    val tfVal = storedComponents.firstNotNullOfOrNull { it.textFieldResponse?.value ?: "" }

    val cbId = storedComponents.firstNotNullOfOrNull { it.checkboxResponse?.id ?: "" }
    val cbVal = storedComponents.firstNotNullOfOrNull { it.checkboxResponse?.value ?: true }

    val tsId = storedComponents.firstNotNullOfOrNull { it.toggleSwitchResponse?.id ?: "" }
    val tsVal = storedComponents.firstNotNullOfOrNull { it.toggleSwitchResponse?.value ?: true }



    Timber.d("Values: $tfId: $tfVal, for checkbox: $cbVal, for toggleSwitch: $tsVal")

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
                    url = "https://placekitten.com/1000/500?image=12",
                    modifier = Modifier.fillMaxSize()
                )
            }

            element.textField?.let { apiField ->
                TextFieldComponent(
                    label = apiField.label,
                    value = element.textFieldResponse?.value ?: "",
                    onValueChange = { text ->
                        Timber.d("Form interaction textField: $text")
                        onInteraction(
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
                    isChecked = element.checkboxResponse?.value ?: true,
                    onCheckedChange = { isChecked ->
                        Timber.d("FormInteractionUI Checkbox Interaction: $isChecked")

                        onInteraction(
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
                    isChecked = element.toggleSwitchResponse?.value ?: true,
                    onCheckedChange = { isChecked ->
                        Timber.d("FormInteractionUI ToggleSwitch Interaction: $isChecked")

                        onInteraction(
                            element.copy(
                                toggleSwitchResponse = element.toggleSwitchResponse?.copy(
                                    value = isChecked
                                )
                            )
                        )
                    }
                )
            }

            element.button?.let { it ->
                Timber.d("Inside the button block ${element.button}")

                DynamicFormButtonComponent(
                    label = it.label,
                    onClick = {
                        Timber.d("Inside the button block in DynamicButtonComp")
                        Timber.d("Inside the button block in DynamicButtonComp $element")
                        element.label.let {
                            Timber.d("label inside Dynamic Button: ${element.label} ")
                            val formSubmission = NewFormSubmissionRequestBody(
                                textFieldResponses = listOf(
                                    NewFormSubmissionTextFieldResponse(
                                        id = textFieldRes?.id ?: "",
                                        value = textFieldRes?.value ?: ""
                                    )
                                ),
                                checkboxResponse = listOf(
                                    NewFormSubmissionCheckboxResponse(
                                        id = cbId ?: "",
                                        value = cbVal ?: true
                                    )
                                ),
                                toggleSwitchResponses = listOf(
                                    NewFormSubmissionToggleSwitchResponse(
                                        id = tsId ?: "",
                                        value = tsVal ?: true

                                    )
                                )

                            )

                            //Timber.d("${it.label} button clicked")
                            Timber.d("Submitted Form id: ${element.label?.formId}")
                            Timber.d("Submitted form response: $formSubmission")
                            it?.formId?.let { it1 -> onSubmitClicked(it1, formSubmission) }


                        }

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
fun TextFieldComponent(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
}

@Composable
fun CheckboxComponent(label: String, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {

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
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun ToggleSwitchComponent(label: String, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
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
            onCheckedChange = onCheckedChange,
        )
    }
}

@Composable
fun DynamicFormButtonComponent(label: String, onClick: () -> Unit) {
    Button(
        onClick = {
            Timber.d("Button clicked")
            onClick()
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = label, modifier = Modifier.padding(8.dp))
    }
}


