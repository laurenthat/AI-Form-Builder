package com.sbma.linkup.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sbma.linkup.R
import com.sbma.linkup.application.AppViewModelProvider
import com.sbma.linkup.user.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen( userViewModel: UserViewModel = viewModel(factory = AppViewModelProvider.Factory)) {
    var showConfirmationDialog by remember { mutableStateOf(false) }
    val composableScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            SettingsScreenTopBar()
        }
    ) {padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(R.string.are_you_sure))
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { showConfirmationDialog = true }) {
                Text(stringResource(R.string.logout))
            }
        }
    }

    if (showConfirmationDialog) {
        LogoutConfirmationDialog(
            onConfirm = {
                run {
                    composableScope.launch {
                        userViewModel.deleteLoginData()
                    }
                }
                showConfirmationDialog = false
            },
            onDismiss = { showConfirmationDialog = false }
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenTopBar() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    MediumTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(
                stringResource(R.string.setting_screen),
                style = MaterialTheme.typography.labelLarge,
                fontSize = 20.sp
            )
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
fun LogoutConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(stringResource(R.string.confirm_logout))},
        text = { Text(stringResource(R.string.are_you_sure)) },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm()
                    onDismiss()
                }
            ) {
                Text(stringResource(R.string.yes))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(R.string.no))
            }
        }
    )
}
