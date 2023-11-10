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
    object Home : BottomNavigationItemsData(
        title = "Home",
        icon = R.drawable.home,
        screenRoute = "home",
    )
    object Forms : BottomNavigationItemsData(
        title = "Forms",
        icon = R.drawable.forms,
        screenRoute = "forms",
    )
    object Settings : BottomNavigationItemsData(
        title = "Settings",
        icon = R.drawable.settings,
        screenRoute = "settings",
    )

}


