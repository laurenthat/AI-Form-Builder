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

val Icons.Filled.Qrcode: ImageVector
    get() {
        if (_qrcode != null) {
            return _qrcode!!
        }
        _qrcode = Builder(name = "Qrcode", defaultWidth = 24.0.dp, defaultHeight = 24.0.dp,
                viewportWidth = 960.0f, viewportHeight = 960.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(120.0f, 440.0f)
                verticalLineToRelative(-320.0f)
                horizontalLineToRelative(320.0f)
                verticalLineToRelative(320.0f)
                lineTo(120.0f, 440.0f)
                close()
                moveTo(200.0f, 360.0f)
                horizontalLineToRelative(160.0f)
                verticalLineToRelative(-160.0f)
                lineTo(200.0f, 200.0f)
                verticalLineToRelative(160.0f)
                close()
                moveTo(120.0f, 840.0f)
                verticalLineToRelative(-320.0f)
                horizontalLineToRelative(320.0f)
                verticalLineToRelative(320.0f)
                lineTo(120.0f, 840.0f)
                close()
                moveTo(200.0f, 760.0f)
                horizontalLineToRelative(160.0f)
                verticalLineToRelative(-160.0f)
                lineTo(200.0f, 600.0f)
                verticalLineToRelative(160.0f)
                close()
                moveTo(520.0f, 440.0f)
                verticalLineToRelative(-320.0f)
                horizontalLineToRelative(320.0f)
                verticalLineToRelative(320.0f)
                lineTo(520.0f, 440.0f)
                close()
                moveTo(600.0f, 360.0f)
                horizontalLineToRelative(160.0f)
                verticalLineToRelative(-160.0f)
                lineTo(600.0f, 200.0f)
                verticalLineToRelative(160.0f)
                close()
                moveTo(760.0f, 840.0f)
                verticalLineToRelative(-80.0f)
                horizontalLineToRelative(80.0f)
                verticalLineToRelative(80.0f)
                horizontalLineToRelative(-80.0f)
                close()
                moveTo(520.0f, 600.0f)
                verticalLineToRelative(-80.0f)
                horizontalLineToRelative(80.0f)
                verticalLineToRelative(80.0f)
                horizontalLineToRelative(-80.0f)
                close()
                moveTo(600.0f, 680.0f)
                verticalLineToRelative(-80.0f)
                horizontalLineToRelative(80.0f)
                verticalLineToRelative(80.0f)
                horizontalLineToRelative(-80.0f)
                close()
                moveTo(520.0f, 760.0f)
                verticalLineToRelative(-80.0f)
                horizontalLineToRelative(80.0f)
                verticalLineToRelative(80.0f)
                horizontalLineToRelative(-80.0f)
                close()
                moveTo(600.0f, 840.0f)
                verticalLineToRelative(-80.0f)
                horizontalLineToRelative(80.0f)
                verticalLineToRelative(80.0f)
                horizontalLineToRelative(-80.0f)
                close()
                moveTo(680.0f, 760.0f)
                verticalLineToRelative(-80.0f)
                horizontalLineToRelative(80.0f)
                verticalLineToRelative(80.0f)
                horizontalLineToRelative(-80.0f)
                close()
                moveTo(680.0f, 600.0f)
                verticalLineToRelative(-80.0f)
                horizontalLineToRelative(80.0f)
                verticalLineToRelative(80.0f)
                horizontalLineToRelative(-80.0f)
                close()
                moveTo(760.0f, 680.0f)
                verticalLineToRelative(-80.0f)
                horizontalLineToRelative(80.0f)
                verticalLineToRelative(80.0f)
                horizontalLineToRelative(-80.0f)
                close()
            }
        }
        .build()
        return _qrcode!!
    }

private var _qrcode: ImageVector? = null
