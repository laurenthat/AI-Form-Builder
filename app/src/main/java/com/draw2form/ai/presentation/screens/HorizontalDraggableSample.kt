package com.draw2form.ai.presentation.screens

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.draw2form.ai.R
import kotlin.math.roundToInt

enum class DragAnchors(val fraction: Float) {
    Start(0f),
    OneQuarter(.25f),
    Half(.5f),
    ThreeQuarters(.75f),
    End(1f),
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalDraggableSample(
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val state = remember {
        AnchoredDraggableState(
            initialValue = DragAnchors.Start,
            positionalThreshold = { distance: Float -> distance * 0.5f },
            velocityThreshold = { with(density) { 100.dp.toPx() } },
            animationSpec = tween(),
        )
            .apply {
                updateAnchors(
                    DraggableAnchors {
                        DragAnchors.Start at 0f
                        DragAnchors.OneQuarter at 200f
                        DragAnchors.Half at 400f
                        DragAnchors.ThreeQuarters at 600f
                        DragAnchors.End at 800f
                    }
                )
            }
    }
    val statey = remember {
        AnchoredDraggableState(
            initialValue = DragAnchors.Start,
            positionalThreshold = { distance: Float -> distance * 0.5f },
            velocityThreshold = { with(density) { 100.dp.toPx() } },
            animationSpec = tween(),
        )
            .apply {
                updateAnchors(
                    DraggableAnchors {
                        DragAnchors.Start at 0f
                        DragAnchors.OneQuarter at 200f
                        DragAnchors.Half at 400f
                        DragAnchors.ThreeQuarters at 600f
                        DragAnchors.End at 800f
                    }
                )
            }
    }
    val contentSize = 80.dp
    val contentSizePx = with(density) { contentSize.toPx() }
    Box(
        modifier
    ) {
        Image(
            painter = painterResource(id = R.drawable.instagram),
            modifier = Modifier
                .size(contentSize)
                .offset {
                    IntOffset(
                        x = state
                            .requireOffset()
                            .roundToInt(),
                        y = statey
                            .requireOffset()
                            .roundToInt(),
                    )

                }
                .anchoredDraggable(state, Orientation.Horizontal)
                .anchoredDraggable(statey, Orientation.Vertical),
            contentDescription = null,
        )
    }
}




