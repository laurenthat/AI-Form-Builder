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

val Icons.Filled.Label: ImageVector
    get() {
        if (_labelFill0Wght400Grad0Opsz24 != null) {
            return _labelFill0Wght400Grad0Opsz24!!
        }
        _labelFill0Wght400Grad0Opsz24 = Builder(
            name = "LabelFill0Wght400Grad0Opsz24", defaultWidth
            = 24.0.dp, defaultHeight = 24.0.dp, viewportWidth = 960.0f, viewportHeight =
            960.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(840.0f, 480.0f)
                lineTo(666.0f, 726.0f)
                quadToRelative(-11.0f, 16.0f, -28.5f, 25.0f)
                reflectiveQuadToRelative(-37.5f, 9.0f)
                lineTo(200.0f, 760.0f)
                quadToRelative(-33.0f, 0.0f, -56.5f, -23.5f)
                reflectiveQuadTo(120.0f, 680.0f)
                verticalLineToRelative(-400.0f)
                quadToRelative(0.0f, -33.0f, 23.5f, -56.5f)
                reflectiveQuadTo(200.0f, 200.0f)
                horizontalLineToRelative(400.0f)
                quadToRelative(20.0f, 0.0f, 37.5f, 9.0f)
                reflectiveQuadToRelative(28.5f, 25.0f)
                lineToRelative(174.0f, 246.0f)
                close()
                moveTo(742.0f, 480.0f)
                lineTo(600.0f, 280.0f)
                lineTo(200.0f, 280.0f)
                verticalLineToRelative(400.0f)
                horizontalLineToRelative(400.0f)
                lineToRelative(142.0f, -200.0f)
                close()
                moveTo(200.0f, 480.0f)
                verticalLineToRelative(200.0f)
                verticalLineToRelative(-400.0f)
                verticalLineToRelative(200.0f)
                close()
            }
        }
            .build()
        return _labelFill0Wght400Grad0Opsz24!!
    }

private var _labelFill0Wght400Grad0Opsz24: ImageVector? = null
