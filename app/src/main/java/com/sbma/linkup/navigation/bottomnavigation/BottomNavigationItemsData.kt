package com.sbma.linkup.navigation.bottomnavigation

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Stable
import com.sbma.linkup.R

@Stable
sealed class BottomNavigationItemsData(
    var title: String,
    @DrawableRes val icon: Int,
    var screenRoute: String,

    ) {
    object Share : BottomNavigationItemsData(
        title = "Share",
        icon = R.drawable.card_send,
        screenRoute = "share",
    )

    object MyContacts : BottomNavigationItemsData(
        title = "My Network",
        icon = R.drawable.my_network,
        screenRoute = "connections",
    )

    object Profile : BottomNavigationItemsData(
        title = "Profile",
        icon = R.drawable.person,
        screenRoute = "profile",
    )

    object Receive : BottomNavigationItemsData(
        title = "Receive",
        icon = R.drawable.card_receive,
        screenRoute = "receive",
    )

    object Settings : BottomNavigationItemsData(
        title = "Settings",
        icon = R.drawable.settings,
        screenRoute = "settings",
    )
}

