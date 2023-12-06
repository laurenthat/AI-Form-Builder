package com.draw2form.ai.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDismissState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.draw2form.ai.R
import com.draw2form.ai.api.ApiForm
import com.draw2form.ai.application.AppViewModelProvider
import com.draw2form.ai.presentation.icons.Forms
import com.draw2form.ai.presentation.ui.theme.LinkUpTheme
import com.draw2form.ai.user.UserViewModel
import kotlinx.datetime.Instant.Companion.parse
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormsListScreenTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
) {
    MediumTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(
                stringResource(R.string.my_forms),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 20.sp,
                style = MaterialTheme.typography.labelLarge,
            )
        },
        scrollBehavior = scrollBehavior
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormsListScreen(
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onFormClick: (ApiForm) -> Unit,
) {

    val userForms = userViewModel.apiUserForms.collectAsState(emptyList())
    var forms = userForms.value


    LaunchedEffect(true) {
        userViewModel.getUserForms()
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    var searchQuery by remember { mutableStateOf("") }

    var showClearIcon by rememberSaveable { mutableStateOf(false) }

    if (searchQuery.isEmpty()) {
        showClearIcon = false
    } else if (searchQuery.isNotEmpty()) {
        showClearIcon = true
    }


    val filteredForms = if (searchQuery.isEmpty()) {
        forms
    } else {
        forms.filter { form ->
            form.name.contains(searchQuery, ignoreCase = true)
        }
    }


    Scaffold(
        topBar = {
            FormsListScreenTopBar(scrollBehavior = scrollBehavior)
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
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { newValue ->
                    searchQuery = newValue
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(16.dp)
                    ),
                placeholder = {
                    Text(
                        text = "Search Forms",
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                trailingIcon = {
                    if (showClearIcon) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Rounded.Clear,
                                tint = MaterialTheme.colorScheme.onBackground,
                                contentDescription = "Clear Icon"
                            )
                        }
                    }
                },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )


            LazyColumn(
                modifier
                    .padding(start = 5.dp, end = 5.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(1.dp),
            ) {
                itemsIndexed(
                    items = filteredForms.sortedBy {
                        parse(it.createdAt).epochSeconds
                    }.reversed(),
                    key = { _, item -> item.id }) { index, form ->

                    val state = rememberDismissState(
                        confirmValueChange = {
                            if (it == DismissValue.DismissedToStart) {
                                userViewModel.deleteFormItem(form)

                            }
                            true
                        }
                    )
                    SwipeToDismiss(
                        state = state,
                        background = {
                            val color = when (state.dismissDirection) {
                                DismissDirection.StartToEnd -> Color.Transparent
                                DismissDirection.EndToStart -> Color.Red
                                null -> Color.Transparent
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(color = color)
                                    .padding(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .align(
                                            Alignment.CenterEnd
                                        )
                                )
                            }
                        },
                        dismissContent = {
                            FormListItem(
                                form,
                                onFormClick = {
                                    if (form.status == "DRAFT") onFormClick(form)
                                    else onFormClick(form)
                                })
                        },
                        directions = setOf(DismissDirection.EndToStart)
                    )
                    Divider()
                }
            }
        }
    }
}


@Composable
fun FormListItem(
    form: ApiForm,
    onFormClick: () -> Unit,
) {


    ListItem(
        modifier = Modifier.clickable(
            onClick = {
                onFormClick()
            }
        ),
        headlineContent = {
            Text(
                text = form.name,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(bottom = 8.dp)
            )
        },
        supportingContent = {
            Text(
                text = run {
                    val date =
                        parse(form.createdAt).toLocalDateTime(TimeZone.currentSystemDefault())
                    "${date.date} ${date.time.hour}:${date.time.minute}"

                },
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium
            )
        },
        leadingContent = {
            Icon(
                imageVector = Icons.Filled.Forms,
                contentDescription = "Form",
                modifier = Modifier
                    .size(50.dp),
            )
        },
        trailingContent = {
            SuggestionChip(
                onClick = { },
                shape = RoundedCornerShape(15.dp),
                border = BorderStroke(0.dp, Color.Transparent),
                label = { Text(text = form.status, fontSize = 12.sp, color = Color.Black) },
                colors = SuggestionChipDefaults.suggestionChipColors(
                    containerColor =
                    if (form.status == "DRAFT") Color(
                        255, 247, 147
                    ) else Color(182, 253, 192)
                ),
                modifier = Modifier


            )
        }

    )
    HorizontalDivider(
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .width(1.dp),
    )


}

@Preview(showBackground = true)
@Composable
fun FormsListScreenPreview() {
    LinkUpTheme {
        FormsListScreen() {}
    }
}



