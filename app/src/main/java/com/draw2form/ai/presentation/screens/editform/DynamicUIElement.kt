package com.draw2form.ai.presentation.screens.editform

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.draw2form.ai.presentation.screens.Checkbox
import com.draw2form.ai.presentation.screens.DynamicFormButton
import com.draw2form.ai.presentation.screens.FormAsyncImage
import com.draw2form.ai.presentation.screens.Label
import com.draw2form.ai.presentation.screens.TextField
import com.draw2form.ai.presentation.screens.ToggleSwitch
import com.draw2form.ai.presentation.screens.UIComponent

@Composable
fun DynamicUI(element: UIComponent, onImageClick: () -> Unit) {

    Row(verticalAlignment = Alignment.CenterVertically) {
        element.label?.let {
            Label(it.label)
        }
        element.image?.let {
            FormAsyncImage(
                url = "https://placekitten.com/1000/500?image=12",
                modifier = Modifier
                    .fillMaxSize(),
                onClick = onImageClick
            )
        }
        element.textField?.let {
            TextField(it.label, "", {})
        }
        element.checkbox?.let {
            Checkbox(it.label)
        }

        element.toggleSwitch?.let {
            ToggleSwitch(it.label)
        }

        element.button?.let {
            DynamicFormButton(it.label)
        }
    }
}