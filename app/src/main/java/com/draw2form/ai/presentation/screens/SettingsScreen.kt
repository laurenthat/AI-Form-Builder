package com.draw2form.ai.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.draw2form.ai.R
import com.draw2form.ai.application.AppViewModelProvider
import com.draw2form.ai.user.UserViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(userViewModel: UserViewModel = viewModel(factory = AppViewModelProvider.Factory)) {
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var isInstructionModalVisible by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val composableScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()


    Scaffold(
        topBar = {
            SettingsScreenTopBar()
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Text(stringResource(R.string.are_you_sure))
            Button(onClick = { isInstructionModalVisible = true }) {
                Text("Instructions")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { showConfirmationDialog = true }) {
                Text(stringResource(R.string.logout))
            }
        }
    }

    if (isInstructionModalVisible) {
        InstructionModal(onDismiss = { isInstructionModalVisible = false })
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
fun InstructionModal(onDismiss: () -> Unit) {
    // You can use BottomSheetScaffold or BottomSheetContent for more complex modals
    BottomSheetScaffold(
        sheetContent = {
            InstructionsScreen()
        },
        sheetContainerColor = MaterialTheme.colorScheme.background,
        sheetContentColor = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.fillMaxSize()
    ) {

    }
}

@Composable
fun InstructionsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {


        // Wireframe Upload Section
        Text(
            text = "Wireframe Upload Instructions",
        )

        Text(
            text = "Follow these instructions to ensure accurate wireframe recognition:",
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "1. Capture the entire wireframe or hand-drawn sketch within the frame.",
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "2. Ensure good lighting to minimize shadows and enhance visibility.",
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "3. Use a clear camera or scanner for better image quality.",
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "4. Useful websites.",
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "5. Some image samples",
            modifier = Modifier.padding(bottom = 8.dp)
        )



        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(5) { index ->
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground), // Placeholder image
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp, 200.dp)
                        .clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(16.dp)
                )
            }
        }

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
            Text(stringResource(R.string.confirm_logout))
        },
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
