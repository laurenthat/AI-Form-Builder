package com.draw2form.ai.presentation.screens

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.draw2form.ai.api.ApiForm

@Composable
fun CategorizedLazyRow(
    forms: List<ApiForm>,
    modifier: Modifier = Modifier,
    onFormClick: (ApiForm) -> Unit,
    onShareButtonClick: (id: String) -> Unit
) {
    val sortedForms = forms.sortedBy { it.name }

    LazyRow(
        modifier = modifier
    ) {
        items(sortedForms) { form ->
            MyFormItem(form, onFormClick = {
                onFormClick(form)
            }) { onShareButtonClick(form.id) }
        }
    }
}