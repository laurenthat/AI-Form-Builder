package com.draw2form.ai.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.draw2form.ai.api.ApiForm


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormSubmissionDetailsScreenTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    label: String,
    onBackClick: () -> Unit,
) {
    MediumTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ), title = {
            Text(
                text = label,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 20.sp,
            )
        }, navigationIcon = {
            IconButton(
                modifier = Modifier,
                onClick = { onBackClick() }
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormSubmissionDetailsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    label: String,
    form: ApiForm,
    submissionId: String?,
) {
    val submission = form.formSubmissions?.find { it.id === submissionId }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            FormSubmissionDetailsScreenTopBar(
                scrollBehavior = scrollBehavior,
                label = label,
                onBackClick = onBackClick,
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            val images = form.images?.map { UIComponent(image = it) } ?: listOf()
            val buttons = form.buttons?.map { UIComponent(button = it) } ?: listOf()
            val checkboxes = form.checkboxes?.map {
                val response = submission?.checkboxResponses?.find { res -> res.checkboxId == it.id }
                UIComponent(checkbox = it, checkboxResponse = response)
            } ?: listOf()
            val textFields = form.textFields?.map {
                val response = submission?.textFieldResponses?.find { res -> res.textFieldId == it.id }
                UIComponent(textField = it, textFieldResponse = response)
            } ?: listOf()

            val toggleSwitch = form.toggleSwitches?.map {
                val response = submission?.toggleSwitchResponses?.find { res -> res.toggleSwitchId == it.id }
                UIComponent(toggleSwitch = it, toggleSwitchResponse = response)
            } ?: listOf()

            val uiComponents = mutableListOf<UIComponent>()
            uiComponents.addAll(images)
            uiComponents.addAll(buttons)
            uiComponents.addAll(checkboxes)
            uiComponents.addAll(textFields)
            uiComponents.addAll(toggleSwitch)
            LazyColumn(
                modifier
                    .padding(start = 5.dp, end = 5.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(1.dp),
            ) {
                items(uiComponents.sortedBy { it.order() }) { item ->
                    FormSubmissionDetailsScreenPreviewFormComponent(item)
                }
            }
        }
    }
}