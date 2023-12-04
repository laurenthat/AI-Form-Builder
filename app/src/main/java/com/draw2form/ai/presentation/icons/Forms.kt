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

val Icons.Filled.Forms: ImageVector
    get() {
        if (_descriptionFill0Wght400Grad0Opsz24 != null) {
            return _descriptionFill0Wght400Grad0Opsz24!!
        }
        _descriptionFill0Wght400Grad0Opsz24 = Builder(
            name = "DescriptionFill0Wght400Grad0Opsz24",
            defaultWidth = 24.0.dp, defaultHeight = 24.0.dp, viewportWidth = 960.0f,
            viewportHeight = 960.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(320.0f, 720.0f)
                horizontalLineToRelative(320.0f)
                verticalLineToRelative(-80.0f)
                lineTo(320.0f, 640.0f)
                verticalLineToRelative(80.0f)
                close()
                moveTo(320.0f, 560.0f)
                horizontalLineToRelative(320.0f)
                verticalLineToRelative(-80.0f)
                lineTo(320.0f, 480.0f)
                verticalLineToRelative(80.0f)
                close()
                moveTo(240.0f, 880.0f)
                quadToRelative(-33.0f, 0.0f, -56.5f, -23.5f)
                reflectiveQuadTo(160.0f, 800.0f)
                verticalLineToRelative(-640.0f)
                quadToRelative(0.0f, -33.0f, 23.5f, -56.5f)
                reflectiveQuadTo(240.0f, 80.0f)
                horizontalLineToRelative(320.0f)
                lineToRelative(240.0f, 240.0f)
                verticalLineToRelative(480.0f)
                quadToRelative(0.0f, 33.0f, -23.5f, 56.5f)
                reflectiveQuadTo(720.0f, 880.0f)
                lineTo(240.0f, 880.0f)
                close()
                moveTo(520.0f, 360.0f)
                verticalLineToRelative(-200.0f)
                lineTo(240.0f, 160.0f)
                verticalLineToRelative(640.0f)
                horizontalLineToRelative(480.0f)
                verticalLineToRelative(-440.0f)
                lineTo(520.0f, 360.0f)
                close()
                moveTo(240.0f, 160.0f)
                verticalLineToRelative(200.0f)
                verticalLineToRelative(-200.0f)
                verticalLineToRelative(640.0f)
                verticalLineToRelative(-640.0f)
                close()
            }
        }
            .build()
        return _descriptionFill0Wght400Grad0Opsz24!!
    }

private var _descriptionFill0Wght400Grad0Opsz24: ImageVector? = null
