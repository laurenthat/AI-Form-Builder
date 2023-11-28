package com.draw2form.ai.presentation.screens.editform

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.draw2form.ai.R
import com.draw2form.ai.presentation.screens.UIElement
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormEditScreen(
    items: List<UIElement>,
    onMove: (Int, Int) -> Unit,
    onPublish: () -> Unit,
    modifier: Modifier = Modifier,
//    onClick: (text: String, picture: String) -> Unit
) {
    val scope = rememberCoroutineScope()
    var overScrollJob by remember { mutableStateOf<Job?>(null) }
    val dragDropListState = rememberDragDropListState(onMove = onMove)
    val sheetState = rememberModalBottomSheetState()
    var isSheetOpen by rememberSaveable {
        mutableStateOf(false)
    }
    Column(modifier = Modifier.padding(16.dp)) {

        Button(
            onClick = onPublish,
            modifier = Modifier
                .padding(end = 8.dp)
                .align(Alignment.End)
        ) {
            Text("Publish")
        }

        LazyColumn(
            modifier = modifier
                .pointerInput(Unit) {
                    detectDragGesturesAfterLongPress(
                        onDrag = { change, offset ->
                            change.consume()
                            dragDropListState.onDrag(offset = offset)

                            if (overScrollJob?.isActive == true)
                                return@detectDragGesturesAfterLongPress

                            dragDropListState
                                .checkForOverScroll()
                                .takeIf { it != 0f }
                                ?.let {
                                    overScrollJob = scope.launch {
                                        dragDropListState.lazyListState.scrollBy(it)
                                    }
                                } ?: kotlin.run { overScrollJob?.cancel() }
                        },
                        onDragStart = { offset -> dragDropListState.onDragStart(offset) },
                        onDragEnd = { dragDropListState.onDragInterrupted() },
                        onDragCancel = { dragDropListState.onDragInterrupted() }
                    )
                }
                .fillMaxSize()
                .padding(top = 5.dp, start = 5.dp, end = 5.dp),
            state = dragDropListState.lazyListState
        ) {
            itemsIndexed(items) { index, item ->
                Row(
                    modifier = Modifier
                        .composed {
                            val offsetOrNull = dragDropListState.elementDisplacement.takeIf {
                                index == dragDropListState.currentIndexOfDraggedItem
                            }
                            val scale = if (offsetOrNull != null) 1.1f else 1f
                            Modifier
                                .graphicsLayer {
                                    translationY = offsetOrNull ?: 0f
                                    scaleX = scale
                                    scaleY = scale
                                }
                        }
                        .background(Color.White, shape = RoundedCornerShape(8.dp))
                        .fillMaxWidth()
                        .padding(5.dp)
                ) {
                    Row {
                        Icon(
                            painter = painterResource(id = R.drawable.drag_indicator),
                            contentDescription = null,
                            tint = Color.LightGray,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(0.dp)
                        )
                        Column(modifier = Modifier.fillMaxHeight()) {
                            IconButton(
                                onClick = { isSheetOpen = true },
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Edit,
                                    contentDescription = "Edit",
                                    tint = Color.LightGray

                                )
                            }
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "Delete",
                                    tint = Color.Red
                                )
                            }
                        }
                        DynamicUI(element = item)
                    }
                }
                Spacer(modifier = Modifier.height(1.dp))
            }

        }

    }
    if (isSheetOpen) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { isSheetOpen = false }) {
        }
    }
}


@Preview(showBackground = true)
@Composable
fun FormEditScreenPreview() {
}
















