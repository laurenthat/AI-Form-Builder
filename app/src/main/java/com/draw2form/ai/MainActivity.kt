package com.draw2form.ai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.draw2form.ai.application.MyApplication
import com.draw2form.ai.navigation.MainScreen
import com.draw2form.ai.presentation.ui.theme.LinkUpTheme
import kotlinx.coroutines.flow.asStateFlow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LinkUpTheme {
                MainScreen(
                    intent = intent,
                    internetConnectionStateFlow = (application as MyApplication).internetConnectionState.asStateFlow()
                )
            }
        }
    }
}

