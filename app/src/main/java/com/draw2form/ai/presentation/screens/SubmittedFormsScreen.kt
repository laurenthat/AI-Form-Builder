package com.draw2form.ai.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.draw2form.ai.api.ApiForm
import com.draw2form.ai.api.ApiFormSubmission
import com.draw2form.ai.application.AppViewModelProvider
import com.draw2form.ai.presentation.icons.Eye
import com.draw2form.ai.user.UserViewModel
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubmittedFormsScreenTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    canGoBack: Boolean,
    canShare: Boolean,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    onPreviewClick: () -> Unit,
    form: ApiForm

) {
    MediumTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ), title = {
            Text(
                text = form.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 20.sp,
            )
        }, navigationIcon = {
            if (canGoBack) {
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

            }
        },

        actions = {
            Row {
                IconButton(
                    modifier = Modifier,
                    onClick = { onPreviewClick() }
                ) {
                    Icon(
                        Icons.Filled.Eye,
                        contentDescription = "Preview",
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                    )
                }

                if (canShare) {
                    IconButton(
                        modifier = Modifier,
                        onClick = { onShareClick() }
                    ) {
                        Icon(
                            Icons.Filled.Share,
                            contentDescription = "Share",
                            modifier = Modifier
                                .padding(horizontal = 3.dp)
                        )
                    }

                }

            }
        },

        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubmittedFormsScreen(
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel = viewModel(factory = AppViewModelProvider.Factory),
    canShare: Boolean,
    canGoBack: Boolean,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    onPreviewClick: () -> Unit,
    form: ApiForm,
    onClick: (id: String) -> Unit
) {

    val submittedFormList = userViewModel.apiSubmittedForms.collectAsState(emptyList())
    val submittedForms = submittedFormList.value

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val responses = submittedForms.count().toString()


    Scaffold(
        topBar = {
            SubmittedFormsScreenTopBar(
                scrollBehavior = scrollBehavior,
                canShare = canShare,
                canGoBack = canGoBack,
                onBackClick = onBackClick,
                onShareClick = onShareClick,
                onPreviewClick = onPreviewClick,
                form = form

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
            Column {
                Text(
                    text = "Responses: $responses",
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.labelLarge,
                    fontSize = 15.sp,
                    modifier = Modifier
                        .padding(20.dp)

                )
            }
            LazyColumn(
                modifier
                    .padding(start = 5.dp, end = 5.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(1.dp),
            ) {
                itemsIndexed(submittedForms) { index, item ->
                    FormSubmittedListItem(formSubmission = item) {
                        onClick(item.id)
                    }
                }
            }
        }
    }
}

@Composable
fun FormSubmittedListItem(
    formSubmission: ApiFormSubmission,
    onClick: () -> Unit
) {
    ListItem(
        modifier = Modifier.clickable(onClick = {onClick()}),
        headlineContent = {
            Text(
                text = formSubmission.owner?.name ?: "Anonymous",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .padding(bottom = 8.dp)
            )
        },
        supportingContent = {
            Text(
                text = run {
                    val date =
                        Instant.parse(formSubmission.createdAt)
                            .toLocalDateTime(TimeZone.currentSystemDefault())
                    "${date.date} ${date.time.hour}:${date.time.minute}"

                },
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium
            )


        },
        leadingContent = {
            if (formSubmission.owner?.picture == null)
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(50.dp)
                )
            else
                AsyncImage(
                    model = formSubmission.owner.picture,
                    contentScale = ContentScale.Crop,
                    contentDescription = "User picture",
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .size(48.dp),
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

