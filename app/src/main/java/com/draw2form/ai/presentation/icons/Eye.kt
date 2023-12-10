package com.draw2form.ai.presentation.icons

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.Filled.Eye: ImageVector
    get() {
        if (_eye != null) {
            return _eye!!
        }
        _eye = Builder(
            name = "Eye", defaultWidth = 24.0.dp, defaultHeight = 24.0.dp, viewportWidth
            = 960.0f, viewportHeight = 960.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(480.0f, 640.0f)
                quadToRelative(75.0f, 0.0f, 127.5f, -52.5f)
                reflectiveQuadTo(660.0f, 460.0f)
                quadToRelative(0.0f, -75.0f, -52.5f, -127.5f)
                reflectiveQuadTo(480.0f, 280.0f)
                quadToRelative(-75.0f, 0.0f, -127.5f, 52.5f)
                reflectiveQuadTo(300.0f, 460.0f)
                quadToRelative(0.0f, 75.0f, 52.5f, 127.5f)
                reflectiveQuadTo(480.0f, 640.0f)
                close()
                moveTo(480.0f, 568.0f)
                quadToRelative(-45.0f, 0.0f, -76.5f, -31.5f)
                reflectiveQuadTo(372.0f, 460.0f)
                quadToRelative(0.0f, -45.0f, 31.5f, -76.5f)
                reflectiveQuadTo(480.0f, 352.0f)
                quadToRelative(45.0f, 0.0f, 76.5f, 31.5f)
                reflectiveQuadTo(588.0f, 460.0f)
                quadToRelative(0.0f, 45.0f, -31.5f, 76.5f)
                reflectiveQuadTo(480.0f, 568.0f)
                close()
                moveTo(480.0f, 760.0f)
                quadToRelative(-146.0f, 0.0f, -266.0f, -81.5f)
                reflectiveQuadTo(40.0f, 460.0f)
                quadToRelative(54.0f, -137.0f, 174.0f, -218.5f)
                reflectiveQuadTo(480.0f, 160.0f)
                quadToRelative(146.0f, 0.0f, 266.0f, 81.5f)
                reflectiveQuadTo(920.0f, 460.0f)
                quadToRelative(-54.0f, 137.0f, -174.0f, 218.5f)
                reflectiveQuadTo(480.0f, 760.0f)
                close()
                moveTo(480.0f, 460.0f)
                close()
                moveTo(480.0f, 680.0f)
                quadToRelative(113.0f, 0.0f, 207.5f, -59.5f)
                reflectiveQuadTo(832.0f, 460.0f)
                quadToRelative(-50.0f, -101.0f, -144.5f, -160.5f)
                reflectiveQuadTo(480.0f, 240.0f)
                quadToRelative(-113.0f, 0.0f, -207.5f, 59.5f)
                reflectiveQuadTo(128.0f, 460.0f)
                quadToRelative(50.0f, 101.0f, 144.5f, 160.5f)
                reflectiveQuadTo(480.0f, 680.0f)
                close()
            }
        }
            .build()
        return _eye!!
    }

private var _eye: ImageVector? = null
