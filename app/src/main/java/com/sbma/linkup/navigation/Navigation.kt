package com.sbma.linkup.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sbma.linkup.application.AppViewModelProvider
import com.sbma.linkup.application.connectivity.InternetConnectionState
import com.sbma.linkup.card.CardViewModel
import com.sbma.linkup.connection.ConnectionViewModel
import com.sbma.linkup.presentation.screens.EditProfileScreenProvider
import com.sbma.linkup.presentation.screens.FormsScreen
import com.sbma.linkup.presentation.screens.HomeScreen
import com.sbma.linkup.presentation.screens.SettingsScreen
import com.sbma.linkup.presentation.screens.bluetooth.BluetoothShareAndReceiveResultScreen
import com.sbma.linkup.presentation.screens.bluetooth.ShareViaBluetoothScreenProvider
import com.sbma.linkup.presentation.screens.nfc.NfcScanScreen
import com.sbma.linkup.presentation.screens.qr.QrCodeScanViaCameraResultScreen
import com.sbma.linkup.presentation.screens.qr.ShareQrCodeScreen
import com.sbma.linkup.presentation.screenstates.UserConnectionsScreenState
import com.sbma.linkup.user.User
import com.sbma.linkup.user.UserViewModel
import kotlinx.coroutines.flow.StateFlow


@Composable
fun Navigation(
    navController: NavHostController,
    user: User,
    internetConnectionStateFlow: StateFlow<InternetConnectionState>,
    modifier: Modifier = Modifier
) {
    val userCardViewModel: CardViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val userConnectionViewModel: ConnectionViewModel =
        viewModel(factory = AppViewModelProvider.Factory)
    val userCards = userCardViewModel.allItemsStream(user.id).collectAsState(initial = listOf())
    val internetState = internetConnectionStateFlow.collectAsState()

    val userViewModel: UserViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val composableScope = rememberCoroutineScope()
    val userItems = userConnectionViewModel
        .allItemsStream(user.id)
        .collectAsState(initial = mapOf())

    val state = UserConnectionsScreenState(
        connections = userItems.value
    )

    NavHost(
        navController,
        modifier = modifier,
        startDestination = "home"
    ) {

        /**
         * Bluetooth method of sharing user profile.
         * at this point json string should be already saved to datastore and available.
         */
        composable(
            "share/{shareId}/bluetooth",
            arguments = listOf(navArgument("shareId") { type = NavType.StringType })
        ) { backStackEntry ->
            val shareId = backStackEntry.arguments?.getString("shareId")
            shareId?.let {
                ShareViaBluetoothScreenProvider(
                    shareId = it,
                    onSuccess = {
                        navController.navigate("scan-result/bluetooth/share/succeeded")
                    },
                    onFailure = {
                        navController.navigate("scan-result/bluetooth/share/failed")
                    }
                )
            }
        }

        composable(
            "share/{shareId}/nfc",
            arguments = listOf(navArgument("shareId") { type = NavType.StringType })
        ) { backStackEntry ->
            val shareId = backStackEntry.arguments?.getString("shareId")
            shareId?.let {
                NfcScanScreen(
                    shareId = it,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
        composable(
            "share/{shareId}/qr",
            arguments = listOf(navArgument("shareId") { type = NavType.StringType })
        ) { backStackEntry ->
            val shareId = backStackEntry.arguments?.getString("shareId")
            shareId?.let {
                ShareQrCodeScreen(
                    shareId = it,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

        }
        /**
         * tab of the bottom navigation bar
         */
        composable("forms") {
            FormsScreen(
                state) { connection ->
                navController.navigate("forms/${connection.id}")
            }
        }
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
                onBackClick = null
            )
        }
        /**
         *  Edit forms screen
         *  After user presses save button it will navigate back to forms route.
         */
        composable("forms/edit") {
            EditProfileScreenProvider(
                user,
                userCards.value,
                onBackClick = {
                    navController.popBackStack()
                },
                onSave = {
                    navController.navigate("forms")
                })
        }
        composable(
            route = "scan-result/qr/receive/{status}",
            arguments = listOf(navArgument("status") { type = NavType.BoolType }),
        ) {
            QrCodeScanViaCameraResultScreen(
                it.arguments?.getBoolean("status") ?: false
            )
        }
        composable(
            route = "scan-result/bluetooth/{mode}/{status}",
            arguments = listOf(
                navArgument("mode") { type = NavType.StringType },
                navArgument("status") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val succeeded = (backStackEntry.arguments?.getString("status") ?: "") == "succeeded"
            val isReceiveMode = (backStackEntry.arguments?.getString("mode") ?: "") == "receive"
            BluetoothShareAndReceiveResultScreen(isReceiveMode, succeeded)
        }
    }
}
