package com.sbma.linkup.navigation.bottomnavigation

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.exyte.animatednavbar.items.dropletbutton.DropletButton
import com.sbma.linkup.navigation.bottomnavigation.balltrajectory.Parabolic
import com.sbma.linkup.navigation.bottomnavigation.indentshape.Height
import com.sbma.linkup.navigation.bottomnavigation.indentshape.shapeCornerRadius
import com.sbma.linkup.presentation.ui.theme.YellowApp


@Composable
fun BottomNavigationBar(navController: NavController) {
    var selectedItem by rememberSaveable { mutableIntStateOf(0) }

    val items = listOf(
        BottomNavigationItemsData.Home,
        BottomNavigationItemsData.Forms,
        BottomNavigationItemsData.Settings,

    )

    AnimatedNavigationBar(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .height(85.dp),
        selectedIndex = selectedItem,
        ballColor = YellowApp,
        cornerRadius = shapeCornerRadius(25.dp),
        ballAnimation = Parabolic(tween(Duration, easing = LinearOutSlowInEasing)),
        indentAnimation = Height(
            indentWidth = 56.dp,
            indentHeight = 15.dp,
            animationSpec = tween(
                DoubleDuration,
                easing = { OvershootInterpolator().getInterpolation(it) })
        )
    ) {
        items.forEachIndexed { index, item ->
            DropletButton(
                modifier = Modifier
                    .fillMaxSize(),
                isSelected = selectedItem == index,
                onClick = {
                    val currentRoute = navController.currentBackStackEntry?.destination?.route
                    currentRoute?.let {
                        if (item.screenRoute != it) {
                            selectedItem = index
                            navController.navigate(item.screenRoute)
                        }
                    }
                },
                icon = item.icon,
                dropletColor = YellowApp,
                animationSpec = tween(durationMillis = Duration, easing = LinearEasing)
            )
        }
    }
}

const val Duration = 500
const val DoubleDuration = 1000

