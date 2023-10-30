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

val Icons.Filled.Title: ImageVector
    get() {
        if (_title != null) {
            return _title!!
        }
        _title = Builder(name = "Title", defaultWidth = 24.0.dp, defaultHeight = 24.0.dp,
                viewportWidth = 960.0f, viewportHeight = 960.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(200.0f, 714.0f)
                quadToRelative(54.0f, -53.0f, 125.5f, -83.5f)
                reflectiveQuadTo(480.0f, 600.0f)
                quadToRelative(83.0f, 0.0f, 154.5f, 30.5f)
                reflectiveQuadTo(760.0f, 714.0f)
                verticalLineToRelative(-514.0f)
                lineTo(200.0f, 200.0f)
                verticalLineToRelative(514.0f)
                close()
                moveTo(480.0f, 520.0f)
                quadToRelative(58.0f, 0.0f, 99.0f, -41.0f)
                reflectiveQuadToRelative(41.0f, -99.0f)
                quadToRelative(0.0f, -58.0f, -41.0f, -99.0f)
                reflectiveQuadToRelative(-99.0f, -41.0f)
                quadToRelative(-58.0f, 0.0f, -99.0f, 41.0f)
                reflectiveQuadToRelative(-41.0f, 99.0f)
                quadToRelative(0.0f, 58.0f, 41.0f, 99.0f)
                reflectiveQuadToRelative(99.0f, 41.0f)
                close()
                moveTo(200.0f, 840.0f)
                quadToRelative(-33.0f, 0.0f, -56.5f, -23.5f)
                reflectiveQuadTo(120.0f, 760.0f)
                verticalLineToRelative(-560.0f)
                quadToRelative(0.0f, -33.0f, 23.5f, -56.5f)
                reflectiveQuadTo(200.0f, 120.0f)
                horizontalLineToRelative(560.0f)
                quadToRelative(33.0f, 0.0f, 56.5f, 23.5f)
                reflectiveQuadTo(840.0f, 200.0f)
                verticalLineToRelative(560.0f)
                quadToRelative(0.0f, 33.0f, -23.5f, 56.5f)
                reflectiveQuadTo(760.0f, 840.0f)
                lineTo(200.0f, 840.0f)
                close()
                moveTo(280.0f, 760.0f)
                horizontalLineToRelative(400.0f)
                verticalLineToRelative(-10.0f)
                quadToRelative(-42.0f, -35.0f, -93.0f, -52.5f)
                reflectiveQuadTo(480.0f, 680.0f)
                quadToRelative(-56.0f, 0.0f, -107.0f, 17.5f)
                reflectiveQuadTo(280.0f, 750.0f)
                verticalLineToRelative(10.0f)
                close()
                moveTo(480.0f, 440.0f)
                quadToRelative(-25.0f, 0.0f, -42.5f, -17.5f)
                reflectiveQuadTo(420.0f, 380.0f)
                quadToRelative(0.0f, -25.0f, 17.5f, -42.5f)
                reflectiveQuadTo(480.0f, 320.0f)
                quadToRelative(25.0f, 0.0f, 42.5f, 17.5f)
                reflectiveQuadTo(540.0f, 380.0f)
                quadToRelative(0.0f, 25.0f, -17.5f, 42.5f)
                reflectiveQuadTo(480.0f, 440.0f)
                close()
                moveTo(480.0f, 457.0f)
                close()
            }
        }
        .build()
        return _title!!
    }

private var _title: ImageVector? = null
