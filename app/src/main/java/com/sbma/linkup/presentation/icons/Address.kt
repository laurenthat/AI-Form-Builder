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

val Icons.Filled.Address: ImageVector
    get() {
        if (_address != null) {
            return _address!!
        }
        _address = Builder(name = "Address", defaultWidth = 24.0.dp, defaultHeight = 24.0.dp,
                viewportWidth = 960.0f, viewportHeight = 960.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(240.0f, 760.0f)
                horizontalLineToRelative(120.0f)
                verticalLineToRelative(-240.0f)
                horizontalLineToRelative(240.0f)
                verticalLineToRelative(240.0f)
                horizontalLineToRelative(120.0f)
                verticalLineToRelative(-360.0f)
                lineTo(480.0f, 220.0f)
                lineTo(240.0f, 400.0f)
                verticalLineToRelative(360.0f)
                close()
                moveTo(160.0f, 840.0f)
                verticalLineToRelative(-480.0f)
                lineToRelative(320.0f, -240.0f)
                lineToRelative(320.0f, 240.0f)
                verticalLineToRelative(480.0f)
                lineTo(520.0f, 840.0f)
                verticalLineToRelative(-240.0f)
                horizontalLineToRelative(-80.0f)
                verticalLineToRelative(240.0f)
                lineTo(160.0f, 840.0f)
                close()
                moveTo(480.0f, 490.0f)
                close()
            }
        }
        .build()
        return _address!!
    }

private var _address: ImageVector? = null
