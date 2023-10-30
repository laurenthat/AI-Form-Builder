package com.sbma.linkup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.sbma.linkup.application.MyApplication
import com.sbma.linkup.navigation.MainScreen
import com.sbma.linkup.presentation.ui.theme.LinkUpTheme
import kotlinx.coroutines.flow.asStateFlow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.lifecycle.addObserver((application as MyApplication).initAppBroadcastReceiver(this))
        this.lifecycle.addObserver((application as MyApplication).initAppNfcManager(this))

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

