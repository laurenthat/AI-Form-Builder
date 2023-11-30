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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import timber.log.Timber

@Composable
fun PublishedFormScreen(
    scannedFormState: List<UIComponent>,
    onInteraction: (List<UIComponent>) -> Unit
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
                element = uiComponent
            ) { updatedComponent ->
                val listCopy = scannedFormState.toMutableList()
                listCopy[index] = updatedComponent
                onInteraction(listCopy)
                Timber.d("updated Component: $updatedComponent")
            }
        }
    }
}


@Composable
fun FormInteractionUI(
    element: UIComponent,
    onInteraction: (UIComponent) -> Unit
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
                isChecked = false,
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
                isChecked = false,
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

        element.button?.let {
            DynamicFormButtonComponent(
                label = it.label,
                onClick = {
                    onInteraction(
                        element.copy(

                        )
                    )
                }
            )
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
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = label, modifier = Modifier.padding(8.dp))
    }
}


sealed class FormInteractions {
    data class TextFieldInteraction(val newText: String) : FormInteractions()
    data class CheckboxInteraction(val isChecked: Boolean) : FormInteractions()
    data class ToggleSwitchInteraction(val isSwitched: Boolean) : FormInteractions()
    object ButtonClickInteraction : FormInteractions()
}

data class FormState(
    val textFieldState: String = "",
    val checkboxState: Boolean = false,
    val toggleSwitchState: Boolean = false,
    val buttonClicked: Boolean = false
)

