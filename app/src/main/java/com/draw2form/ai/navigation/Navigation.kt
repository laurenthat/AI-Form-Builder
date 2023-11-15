package com.draw2form.ai.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.draw2form.ai.application.connectivity.InternetConnectionState
import com.draw2form.ai.presentation.screens.HomeScreen
import com.draw2form.ai.presentation.screens.SettingsScreen
import com.draw2form.ai.user.User
import kotlinx.coroutines.flow.StateFlow


@Composable
fun Navigation(
    navController: NavHostController,
    user: User,
    internetConnectionStateFlow: StateFlow<InternetConnectionState>,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController,
        modifier = modifier,
        startDestination = "home"
    ) {

        /**
         * tab of the bottom navigation bar
         */
        composable("settings") {
            SettingsScreen()
        }

        /**
         * tab of the bottom navigation bar
         * Navigates to {UserShareScreenProvider}  when user clicks on Share button.
         */
        composable("home") {
            HomeScreen(
                user,
                canEdit = true,
                onEditClick = { navController.navigate("profile/edit") },
                canGoBack = false,
                onBackClick = null,

                )
        }
    }
}
