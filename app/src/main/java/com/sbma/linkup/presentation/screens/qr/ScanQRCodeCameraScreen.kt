package com.sbma.linkup.presentation.screens.qr

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sbma.linkup.R
import com.sbma.linkup.application.AppViewModelProvider
import com.sbma.linkup.presentation.screens.qr.components.QrCodeReader
import com.sbma.linkup.presentation.screens.qr.components.ScanQRCodeCameraScreenTopBar
import com.sbma.linkup.presentation.ui.theme.YellowApp
import com.sbma.linkup.user.UserViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun ScanQRCodeCameraScreen(
    userViewModel: UserViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onBackClick: () -> Unit,
    onResultScan: (Boolean) -> Unit
) {

    val scope = rememberCoroutineScope()
    var code by remember {
        mutableStateOf("")
    }
    Scaffold(
        topBar = {
            ScanQRCodeCameraScreenTopBar {
                onBackClick()
            }
        },
        modifier = Modifier
            .fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
                .padding(top = 20.dp),
        ) {
            Text(
                textAlign = TextAlign.Center,
                text = stringResource(R.string.scan_qr_code),
                style = MaterialTheme.typography.labelLarge,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(50.dp))
            QrCodeReader(
                { result ->
                    scope.launch {
                        code = result
                        Timber.d("Result: $result")
                        val id = code.split(MYAPI).last()
                        userViewModel.scanQRCode(id) {
                            onResultScan(it)
                        }

                    }

                },
                modifier = Modifier
                    .size(width = 265.dp, height = 360.dp)
                    .border(10.dp, YellowApp, RoundedCornerShape(70.dp))
                    .clip(RoundedCornerShape(70.dp))
            )
            Spacer(modifier = Modifier.height(50.dp))
            Text(text = code)
        }
    }
}



