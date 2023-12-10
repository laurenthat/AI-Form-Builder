package com.draw2form.ai.presentation.screens.editform

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.draw2form.ai.presentation.screens.FormAsyncImage
import com.draw2form.ai.presentation.screens.FormButton
import com.draw2form.ai.presentation.screens.FormCheckbox
import com.draw2form.ai.presentation.screens.FormLabel
import com.draw2form.ai.presentation.screens.FormTextField
import com.draw2form.ai.presentation.screens.FormToggleSwitch
import com.draw2form.ai.presentation.screens.UIComponent

@Composable
fun DynamicUI(element: UIComponent) {

    Row(verticalAlignment = Alignment.CenterVertically) {
        element.label?.let {
            FormLabel(it.label)
        }
        element.image?.let {
            FormAsyncImage(
                url = it.url ?: "https://picsum.photos/id/20/1000/500",
                modifier = Modifier
                    .fillMaxSize(),
            )
        }
        element.textField?.let {
            FormTextField(it.label, "", {})
        }
        element.checkbox?.let {
            FormCheckbox(it.label)
        }

        element.toggleSwitch?.let {
            FormToggleSwitch(it.label)
        }

        element.button?.let {
            FormButton(it.label)
        }
    }
}