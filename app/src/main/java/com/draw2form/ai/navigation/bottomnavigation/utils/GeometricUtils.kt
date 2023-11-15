package com.draw2form.ai.navigation.bottomnavigation.utils

fun lerp(start: Float, stop: Float, fraction: Float) =
    (start * (1 - fraction) + stop * fraction)