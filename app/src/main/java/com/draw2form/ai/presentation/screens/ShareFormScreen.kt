package com.draw2form.ai.presentation.screens

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.draw2form.ai.R
import com.draw2form.ai.presentation.components.rememberQrBitmapPainter

const val MYAPI = "https://draw2form.ericaskari.com/android/forms/publish?id="

@Composable
fun ShareFormScreen(shareId: String, onBackClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        GenerateQRCode(MYAPI + shareId, onBackClick)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenerateQRCode(
    data: String, onBackClick: () -> Unit
) {
    val painter = rememberQrBitmapPainter(content = data)
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie_qr))
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
    }


    Scaffold(
        topBar = {
            GenerateQRCodeTopBar(
                scrollBehavior = scrollBehavior

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
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),

            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            //Spacer(modifier = Modifier.height((-2).dp))

            Text(
                text = "Scan QR to fill the Form",
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )
            Spacer(modifier = Modifier.height(22.dp))

            Button(onClick = {
                shareViaLink(data, context, launcher)
            }) {
                Text(text = "Share your form link")
            }


        }
    }
}

fun shareViaLink(
    id: String,
    context: android.content.Context,
    launcher: ActivityResultLauncher<Intent>
) {
    val sendIntent = Intent(Intent.ACTION_SEND)
        .setType("text/plain")
        .putExtra(Intent.EXTRA_TEXT, id)

    val shareIntent = Intent.createChooser(sendIntent, null)
    launcher.launch(shareIntent)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenerateQRCodeTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onBackClick: () -> Unit
) {
    MediumTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(
                text = "Share Form",
                style = MaterialTheme.typography.labelLarge,
                fontSize = 20.sp
            )
        },
        navigationIcon = {
            IconButton(
                modifier = Modifier,
                onClick = { onBackClick() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}
