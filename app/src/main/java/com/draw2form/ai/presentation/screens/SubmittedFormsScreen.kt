package com.draw2form.ai.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.draw2form.ai.api.ApiForm
import com.draw2form.ai.api.ApiUser
import com.draw2form.ai.application.AppViewModelProvider
import com.draw2form.ai.presentation.ui.theme.LinkUpTheme
import com.draw2form.ai.user.UserViewModel
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubmittedFormsScreenTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
) {
    MediumTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(
                text = "Form Name",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 20.sp
            )
        },
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubmittedFormsScreen(
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {

    val submittedFormList = userViewModel.apiSubmittedForms.collectAsState(emptyList())
    val submittedForms = submittedFormList.value

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            SubmittedFormsScreenTopBar(scrollBehavior = scrollBehavior)
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
            LazyColumn(
                modifier
                    .padding(start = 5.dp, end = 5.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(1.dp),
            ) {
                itemsIndexed(submittedForms) { index, item ->
                    Text(text = item.owner?.name ?: "")
                }


            }
        }
    }
}


@Composable
fun SubmittedFormListItem(
    user: ApiUser,
    form: ApiForm,
) {


    ListItem(
        modifier = Modifier.clickable(
            onClick = {}
        ),
        headlineContent = {
            Text(
                text = user.name,
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
                        Instant.parse(form.createdAt)
                            .toLocalDateTime(TimeZone.currentSystemDefault())
                    "${date.date} ${date.time.hour}:${date.time.minute}"

                },
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium
            )
        },
        leadingContent = {
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = "User",
                modifier = Modifier
                    .size(50.dp),
            )
        },

        )
    HorizontalDivider(
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .width(1.dp),
    )


}

@Composable
@Preview(showBackground = true)
fun PublishFormScreenPreview() {
    LinkUpTheme {
        SubmittedFormsScreen()
    }
}