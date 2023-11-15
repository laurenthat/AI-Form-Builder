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

val Icons.Filled.QrCodeScan: ImageVector
    get() {
        if (_qrcodescan != null) {
            return _qrcodescan!!
        }
        _qrcodescan = Builder(
            name = "Qrcodescan", defaultWidth = 24.0.dp, defaultHeight = 24.0.dp,
            viewportWidth = 960.0f, viewportHeight = 960.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(80.0f, 280.0f)
                verticalLineToRelative(-200.0f)
                horizontalLineToRelative(200.0f)
                verticalLineToRelative(80.0f)
                lineTo(160.0f, 160.0f)
                verticalLineToRelative(120.0f)
                lineTo(80.0f, 280.0f)
                close()
                moveTo(80.0f, 880.0f)
                verticalLineToRelative(-200.0f)
                horizontalLineToRelative(80.0f)
                verticalLineToRelative(120.0f)
                horizontalLineToRelative(120.0f)
                verticalLineToRelative(80.0f)
                lineTo(80.0f, 880.0f)
                close()
                moveTo(680.0f, 880.0f)
                verticalLineToRelative(-80.0f)
                horizontalLineToRelative(120.0f)
                verticalLineToRelative(-120.0f)
                horizontalLineToRelative(80.0f)
                verticalLineToRelative(200.0f)
                lineTo(680.0f, 880.0f)
                close()
                moveTo(800.0f, 280.0f)
                verticalLineToRelative(-120.0f)
                lineTo(680.0f, 160.0f)
                verticalLineToRelative(-80.0f)
                horizontalLineToRelative(200.0f)
                verticalLineToRelative(200.0f)
                horizontalLineToRelative(-80.0f)
                close()
                moveTo(700.0f, 700.0f)
                horizontalLineToRelative(60.0f)
                verticalLineToRelative(60.0f)
                horizontalLineToRelative(-60.0f)
                verticalLineToRelative(-60.0f)
                close()
                moveTo(700.0f, 580.0f)
                horizontalLineToRelative(60.0f)
                verticalLineToRelative(60.0f)
                horizontalLineToRelative(-60.0f)
                verticalLineToRelative(-60.0f)
                close()
                moveTo(640.0f, 640.0f)
                horizontalLineToRelative(60.0f)
                verticalLineToRelative(60.0f)
                horizontalLineToRelative(-60.0f)
                verticalLineToRelative(-60.0f)
                close()
                moveTo(580.0f, 700.0f)
                horizontalLineToRelative(60.0f)
                verticalLineToRelative(60.0f)
                horizontalLineToRelative(-60.0f)
                verticalLineToRelative(-60.0f)
                close()
                moveTo(520.0f, 640.0f)
                horizontalLineToRelative(60.0f)
                verticalLineToRelative(60.0f)
                horizontalLineToRelative(-60.0f)
                verticalLineToRelative(-60.0f)
                close()
                moveTo(640.0f, 520.0f)
                horizontalLineToRelative(60.0f)
                verticalLineToRelative(60.0f)
                horizontalLineToRelative(-60.0f)
                verticalLineToRelative(-60.0f)
                close()
                moveTo(580.0f, 580.0f)
                horizontalLineToRelative(60.0f)
                verticalLineToRelative(60.0f)
                horizontalLineToRelative(-60.0f)
                verticalLineToRelative(-60.0f)
                close()
                moveTo(520.0f, 520.0f)
                horizontalLineToRelative(60.0f)
                verticalLineToRelative(60.0f)
                horizontalLineToRelative(-60.0f)
                verticalLineToRelative(-60.0f)
                close()
                moveTo(760.0f, 200.0f)
                verticalLineToRelative(240.0f)
                lineTo(520.0f, 440.0f)
                verticalLineToRelative(-240.0f)
                horizontalLineToRelative(240.0f)
                close()
                moveTo(440.0f, 520.0f)
                verticalLineToRelative(240.0f)
                lineTo(200.0f, 760.0f)
                verticalLineToRelative(-240.0f)
                horizontalLineToRelative(240.0f)
                close()
                moveTo(440.0f, 200.0f)
                verticalLineToRelative(240.0f)
                lineTo(200.0f, 440.0f)
                verticalLineToRelative(-240.0f)
                horizontalLineToRelative(240.0f)
                close()
                moveTo(380.0f, 700.0f)
                verticalLineToRelative(-120.0f)
                lineTo(260.0f, 580.0f)
                verticalLineToRelative(120.0f)
                horizontalLineToRelative(120.0f)
                close()
                moveTo(380.0f, 380.0f)
                verticalLineToRelative(-120.0f)
                lineTo(260.0f, 260.0f)
                verticalLineToRelative(120.0f)
                horizontalLineToRelative(120.0f)
                close()
                moveTo(700.0f, 380.0f)
                verticalLineToRelative(-120.0f)
                lineTo(580.0f, 260.0f)
                verticalLineToRelative(120.0f)
                horizontalLineToRelative(120.0f)
                close()
            }
        }
            .build()
        return _qrcodescan!!
    }

private var _qrcodescan: ImageVector? = null
