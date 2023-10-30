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

val Icons.Filled.Bluetooth: ImageVector
    get() {
        if (_bluetooth != null) {
            return _bluetooth!!
        }
        _bluetooth = Builder(name = "Bluetooth", defaultWidth = 24.0.dp, defaultHeight = 24.0.dp,
                viewportWidth = 960.0f, viewportHeight = 960.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(440.0f, 880.0f)
                verticalLineToRelative(-304.0f)
                lineTo(256.0f, 760.0f)
                lineToRelative(-56.0f, -56.0f)
                lineToRelative(224.0f, -224.0f)
                lineToRelative(-224.0f, -224.0f)
                lineToRelative(56.0f, -56.0f)
                lineToRelative(184.0f, 184.0f)
                verticalLineToRelative(-304.0f)
                horizontalLineToRelative(40.0f)
                lineToRelative(228.0f, 228.0f)
                lineToRelative(-172.0f, 172.0f)
                lineToRelative(172.0f, 172.0f)
                lineTo(480.0f, 880.0f)
                horizontalLineToRelative(-40.0f)
                close()
                moveTo(520.0f, 384.0f)
                lineTo(596.0f, 308.0f)
                lineTo(520.0f, 234.0f)
                verticalLineToRelative(150.0f)
                close()
                moveTo(520.0f, 726.0f)
                lineTo(596.0f, 652.0f)
                lineTo(520.0f, 576.0f)
                verticalLineToRelative(150.0f)
                close()
            }
        }
        .build()
        return _bluetooth!!
    }

private var _bluetooth: ImageVector? = null
