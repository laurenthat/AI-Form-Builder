package com.sbma.linkup.presentation.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.sbma.linkup.R
import com.sbma.linkup.presentation.icons.AddCard
import com.sbma.linkup.presentation.icons.Bluetooth
import com.sbma.linkup.presentation.icons.Contactless
import com.sbma.linkup.presentation.icons.Nfc
import com.sbma.linkup.presentation.icons.QrCodeScan
import com.sbma.linkup.presentation.icons.Qrcode
import com.sbma.linkup.presentation.ui.theme.LinkUpTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseMethodScreenTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    isReceiving: Boolean,
    backButtonEnabled: Boolean,
    onBackClick: () -> Unit
) {
    MediumTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        navigationIcon = {
            if (backButtonEnabled) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        title = {
            Text(
                text = if (isReceiving) stringResource(R.string.receive_option) else stringResource(
                    R.string.share_option
                ),
                style = MaterialTheme.typography.labelLarge,
                fontSize = 20.sp
            )
        },
        actions = {},
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseMethodScreen(
    onQrCodeClick: () -> Unit,
    isReceiving: Boolean,
    onNfcClick: () -> Unit,
    onBluetoothClick: () -> Unit,
    onBackClick: () -> Unit,
    @SuppressLint("ModifierParameter")
    cardModifier: Modifier = Modifier
        .size(width = 150.dp, height = 150.dp)
        .padding(top = 12.dp)
) {

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        topBar = {
            ChooseMethodScreenTopBar(
                scrollBehavior,
                isReceiving,
                backButtonEnabled = !isReceiving
            ) {
                onBackClick()
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    onClick = { onQrCodeClick() },
                    modifier = cardModifier
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                    ) {
                        Icon(
                            imageVector = when (isReceiving) {
                                true -> Icons.Filled.QrCodeScan
                                false -> Icons.Filled.Qrcode
                            },
                            contentDescription = "Qr Code Method",
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            stringResource(R.string.qr_code),
                            modifier = Modifier
                        )
                    }
                }
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    onClick = { onBluetoothClick() },
                    modifier = cardModifier
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Bluetooth,
                            contentDescription = "Bluetooth Method",
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            stringResource(R.string.bluetooth), modifier = Modifier

                        )
                    }
                }
            }

            Text(
                text = if (isReceiving) stringResource(R.string.nfc_card_receive_option)
                else stringResource(R.string.nfc_card_assign_option),
                fontSize = 20.sp,
                lineHeight = 1.5.em,
                modifier = Modifier
                    .padding(vertical = 20.dp, horizontal = 10.dp)
            )

            ElevatedCard(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                onClick = { onNfcClick() },
                modifier = cardModifier
            ) {
                Icon(
                    imageVector = Icons.Filled.Nfc,
                    contentDescription = "NFC Method",
                    modifier = Modifier
                        .size(16.dp)
                        .absoluteOffset(x = 5.dp, y = 5.dp)
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    Icon(
                        imageVector = when (isReceiving) {
                            true -> Icons.Filled.Contactless
                            false -> Icons.Filled.AddCard
                        },
                        contentDescription = "NFC Method",
                        modifier = Modifier.size(48.dp)
                    )

                    Text(
                        when (isReceiving) {
                           true -> "Read Card"
                           false -> "Assign Card"
                        },
                        modifier = Modifier
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }

}

@Preview(showBackground = true)
@Composable
fun MainShareScreenPreview() {
    LinkUpTheme {
        ChooseMethodScreen(
            onQrCodeClick = {},
            onNfcClick = {},
            onBluetoothClick = {},
            isReceiving = true,
            onBackClick = {}
        )
    }
}
