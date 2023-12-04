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

val Icons.Filled.TextField: ImageVector
    get() {
        if (_variableAddFill0Wght400Grad0Opsz24 != null) {
            return _variableAddFill0Wght400Grad0Opsz24!!
        }
        _variableAddFill0Wght400Grad0Opsz24 = Builder(
            name = "VariableAddFill0Wght400Grad0Opsz24",
            defaultWidth = 24.0.dp, defaultHeight = 24.0.dp, viewportWidth = 960.0f,
            viewportHeight = 960.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(560.0f, 680.0f)
                lineTo(120.0f, 680.0f)
                verticalLineToRelative(-400.0f)
                horizontalLineToRelative(720.0f)
                verticalLineToRelative(120.0f)
                horizontalLineToRelative(-80.0f)
                verticalLineToRelative(-40.0f)
                lineTo(200.0f, 360.0f)
                verticalLineToRelative(240.0f)
                horizontalLineToRelative(360.0f)
                verticalLineToRelative(80.0f)
                close()
                moveTo(200.0f, 600.0f)
                verticalLineToRelative(-240.0f)
                verticalLineToRelative(240.0f)
                close()
                moveTo(760.0f, 800.0f)
                verticalLineToRelative(-120.0f)
                lineTo(640.0f, 680.0f)
                verticalLineToRelative(-80.0f)
                horizontalLineToRelative(120.0f)
                verticalLineToRelative(-120.0f)
                horizontalLineToRelative(80.0f)
                verticalLineToRelative(120.0f)
                horizontalLineToRelative(120.0f)
                verticalLineToRelative(80.0f)
                lineTo(840.0f, 680.0f)
                verticalLineToRelative(120.0f)
                horizontalLineToRelative(-80.0f)
                close()
            }
        }
            .build()
        return _variableAddFill0Wght400Grad0Opsz24!!
    }

private var _variableAddFill0Wght400Grad0Opsz24: ImageVector? = null
