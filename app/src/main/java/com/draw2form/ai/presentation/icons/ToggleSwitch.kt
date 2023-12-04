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

val Icons.Filled.ToggleSwitch: ImageVector
    get() {
        if (_toggleOnFill0Wght400Grad0Opsz24 != null) {
            return _toggleOnFill0Wght400Grad0Opsz24!!
        }
        _toggleOnFill0Wght400Grad0Opsz24 = Builder(
            name = "ToggleOnFill0Wght400Grad0Opsz24",
            defaultWidth = 24.0.dp, defaultHeight = 24.0.dp, viewportWidth = 960.0f,
            viewportHeight = 960.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(280.0f, 720.0f)
                quadToRelative(-100.0f, 0.0f, -170.0f, -70.0f)
                reflectiveQuadTo(40.0f, 480.0f)
                quadToRelative(0.0f, -100.0f, 70.0f, -170.0f)
                reflectiveQuadToRelative(170.0f, -70.0f)
                horizontalLineToRelative(400.0f)
                quadToRelative(100.0f, 0.0f, 170.0f, 70.0f)
                reflectiveQuadToRelative(70.0f, 170.0f)
                quadToRelative(0.0f, 100.0f, -70.0f, 170.0f)
                reflectiveQuadToRelative(-170.0f, 70.0f)
                lineTo(280.0f, 720.0f)
                close()
                moveTo(280.0f, 640.0f)
                horizontalLineToRelative(400.0f)
                quadToRelative(66.0f, 0.0f, 113.0f, -47.0f)
                reflectiveQuadToRelative(47.0f, -113.0f)
                quadToRelative(0.0f, -66.0f, -47.0f, -113.0f)
                reflectiveQuadToRelative(-113.0f, -47.0f)
                lineTo(280.0f, 320.0f)
                quadToRelative(-66.0f, 0.0f, -113.0f, 47.0f)
                reflectiveQuadToRelative(-47.0f, 113.0f)
                quadToRelative(0.0f, 66.0f, 47.0f, 113.0f)
                reflectiveQuadToRelative(113.0f, 47.0f)
                close()
                moveTo(680.0f, 600.0f)
                quadToRelative(50.0f, 0.0f, 85.0f, -35.0f)
                reflectiveQuadToRelative(35.0f, -85.0f)
                quadToRelative(0.0f, -50.0f, -35.0f, -85.0f)
                reflectiveQuadToRelative(-85.0f, -35.0f)
                quadToRelative(-50.0f, 0.0f, -85.0f, 35.0f)
                reflectiveQuadToRelative(-35.0f, 85.0f)
                quadToRelative(0.0f, 50.0f, 35.0f, 85.0f)
                reflectiveQuadToRelative(85.0f, 35.0f)
                close()
                moveTo(480.0f, 480.0f)
                close()
            }
        }
            .build()
        return _toggleOnFill0Wght400Grad0Opsz24!!
    }

private var _toggleOnFill0Wght400Grad0Opsz24: ImageVector? = null
