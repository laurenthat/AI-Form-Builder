package com.draw2form.ai.presentation.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.draw2form.ai.api.ApiForm
import com.draw2form.ai.presentation.screens.editform.DynamicUI


@Composable
fun PublishedFormScreen(scannedFormState: ApiForm) {

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