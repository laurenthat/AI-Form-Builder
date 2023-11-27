package com.draw2form.ai.presentation.screens.editform

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.draw2form.ai.api.ApiFormButton
import com.draw2form.ai.api.ApiFormCheckbox
import com.draw2form.ai.api.ApiFormImage
import com.draw2form.ai.api.ApiFormLabel
import com.draw2form.ai.api.ApiFormTextField
import com.draw2form.ai.api.ApiFormToggleSwitch
import com.draw2form.ai.presentation.screens.Checkbox
import com.draw2form.ai.presentation.screens.DynamicFormButton
import com.draw2form.ai.presentation.screens.FormAsyncImage
import com.draw2form.ai.presentation.screens.Label
import com.draw2form.ai.presentation.screens.TextField
import com.draw2form.ai.presentation.screens.ToggleSwitch
import com.draw2form.ai.presentation.screens.UIElement

@Composable
fun DynamicUI(element: UIElement) {

    Row(verticalAlignment = Alignment.CenterVertically) {
        when (element) {
            is ApiFormLabel -> Label(element.label)
            is ApiFormImage -> FormAsyncImage(
                url = "https://placekitten.com/1000/500?image=12",
                modifier = Modifier
                    .fillMaxSize()
            )

            is ApiFormTextField -> TextField(element.label)
            is ApiFormCheckbox -> Checkbox(element.label)
            is ApiFormToggleSwitch -> ToggleSwitch(element.label)
            is ApiFormButton -> DynamicFormButton(element.label)
        }
    }
}