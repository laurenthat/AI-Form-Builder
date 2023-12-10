package com.draw2form.ai.presentation.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FormSubmissionDetailsScreenPreviewFormComponent(
    element: UIComponent,
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
                    readonly = true,
                    onValueChange = {}
                )
            }

            element.checkbox?.let {
                CheckboxComponent(
                    label = it.label,
                    isChecked = element.checkboxResponse?.value ?: false,
                    enabled = true,
                    onCheckedChange = {}
                )
            }

            element.toggleSwitch?.let {
                ToggleSwitchComponent(
                    label = it.label,
                    isChecked = element.toggleSwitchResponse?.value ?: false,
                    enabled = true,
                    onCheckedChange = {}
                )
            }

            element.button?.let {
                DynamicFormButtonComponent(
                    label = it.label,
                    enabled = false,
                    onClick = {}
                )
            }
        }
    }
}