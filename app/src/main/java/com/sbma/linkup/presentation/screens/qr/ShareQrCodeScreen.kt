package com.sbma.linkup.presentation.screens.qr

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sbma.linkup.presentation.screens.qr.components.GenerateQRCode


const val MYAPI = "https://draw2form.ericaskari.com/android/qr/scan?id="


@Composable
fun ShareQrCodeScreen(shareId: String, onBackClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        GenerateQRCode(MYAPI + shareId, onBackClick)
    }
}

