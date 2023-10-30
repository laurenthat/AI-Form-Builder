package com.sbma.linkup.presentation.icons

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

val Icons.Filled.Contactless: ImageVector
    get() {
        if (_contactless != null) {
            return _contactless!!
        }
        _contactless = Builder(name = "Contactless", defaultWidth = 24.0.dp, defaultHeight =
                24.0.dp, viewportWidth = 960.0f, viewportHeight = 960.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(336.0f, 586.0f)
                quadToRelative(9.0f, -24.0f, 14.5f, -50.5f)
                reflectiveQuadTo(356.0f, 480.0f)
                quadToRelative(0.0f, -29.0f, -5.5f, -55.5f)
                reflectiveQuadTo(336.0f, 374.0f)
                lineToRelative(-74.0f, 30.0f)
                quadToRelative(6.0f, 18.0f, 10.0f, 37.0f)
                reflectiveQuadToRelative(4.0f, 39.0f)
                quadToRelative(0.0f, 20.0f, -4.0f, 39.0f)
                reflectiveQuadToRelative(-10.0f, 37.0f)
                lineToRelative(74.0f, 30.0f)
                close()
                moveTo(464.0f, 640.0f)
                quadToRelative(17.0f, -38.0f, 24.5f, -78.0f)
                reflectiveQuadToRelative(7.5f, -82.0f)
                quadToRelative(0.0f, -42.0f, -7.5f, -82.0f)
                reflectiveQuadTo(464.0f, 320.0f)
                lineToRelative(-74.0f, 30.0f)
                quadToRelative(14.0f, 30.0f, 20.0f, 62.5f)
                reflectiveQuadToRelative(6.0f, 67.5f)
                quadToRelative(0.0f, 35.0f, -6.0f, 67.5f)
                reflectiveQuadTo(390.0f, 610.0f)
                lineToRelative(74.0f, 30.0f)
                close()
                moveTo(594.0f, 694.0f)
                quadToRelative(21.0f, -50.0f, 31.5f, -103.5f)
                reflectiveQuadTo(636.0f, 480.0f)
                quadToRelative(0.0f, -57.0f, -10.5f, -110.5f)
                reflectiveQuadTo(594.0f, 266.0f)
                lineToRelative(-74.0f, 32.0f)
                quadToRelative(18.0f, 42.0f, 27.0f, 88.0f)
                reflectiveQuadToRelative(9.0f, 94.0f)
                quadToRelative(0.0f, 48.0f, -9.0f, 94.0f)
                reflectiveQuadToRelative(-27.0f, 88.0f)
                lineToRelative(74.0f, 32.0f)
                close()
                moveTo(480.0f, 880.0f)
                quadToRelative(-83.0f, 0.0f, -156.0f, -31.5f)
                reflectiveQuadTo(197.0f, 763.0f)
                quadToRelative(-54.0f, -54.0f, -85.5f, -127.0f)
                reflectiveQuadTo(80.0f, 480.0f)
                quadToRelative(0.0f, -83.0f, 31.5f, -156.0f)
                reflectiveQuadTo(197.0f, 197.0f)
                quadToRelative(54.0f, -54.0f, 127.0f, -85.5f)
                reflectiveQuadTo(480.0f, 80.0f)
                quadToRelative(83.0f, 0.0f, 156.0f, 31.5f)
                reflectiveQuadTo(763.0f, 197.0f)
                quadToRelative(54.0f, 54.0f, 85.5f, 127.0f)
                reflectiveQuadTo(880.0f, 480.0f)
                quadToRelative(0.0f, 83.0f, -31.5f, 156.0f)
                reflectiveQuadTo(763.0f, 763.0f)
                quadToRelative(-54.0f, 54.0f, -127.0f, 85.5f)
                reflectiveQuadTo(480.0f, 880.0f)
                close()
                moveTo(480.0f, 800.0f)
                quadToRelative(134.0f, 0.0f, 227.0f, -93.0f)
                reflectiveQuadToRelative(93.0f, -227.0f)
                quadToRelative(0.0f, -134.0f, -93.0f, -227.0f)
                reflectiveQuadToRelative(-227.0f, -93.0f)
                quadToRelative(-134.0f, 0.0f, -227.0f, 93.0f)
                reflectiveQuadToRelative(-93.0f, 227.0f)
                quadToRelative(0.0f, 134.0f, 93.0f, 227.0f)
                reflectiveQuadToRelative(227.0f, 93.0f)
                close()
                moveTo(480.0f, 480.0f)
                close()
            }
        }
        .build()
        return _contactless!!
    }

private var _contactless: ImageVector? = null
