package com.draw2form.ai.navigation.bottomnavigation

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Stable
import com.draw2form.ai.R

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
        title = "Forms List",
        icon = R.drawable.forms,
        screenRoute = "forms",
    )

    object Settings : BottomNavigationItemsData(
        title = "Settings",
        icon = R.drawable.settings,
        screenRoute = "settings",
    )

}


