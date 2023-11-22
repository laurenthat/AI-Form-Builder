package com.draw2form.ai.presentation.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.draw2form.ai.R


//TODO Enhance it
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstructionScreenTopBar() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    MediumTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(
                stringResource(R.string.instructions),
                style = MaterialTheme.typography.labelLarge,
                fontSize = 20.sp
            )
        },
        scrollBehavior = scrollBehavior
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun InstructionsScreen() {
    Scaffold(
        topBar = {
            SettingsScreenTopBar()
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

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
    }
}
