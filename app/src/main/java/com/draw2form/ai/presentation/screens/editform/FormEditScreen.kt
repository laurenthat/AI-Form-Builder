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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
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
import com.draw2form.ai.api.ApiFormButton
import com.draw2form.ai.api.ApiFormCheckbox
import com.draw2form.ai.api.ApiFormImage
import com.draw2form.ai.api.ApiFormLabel
import com.draw2form.ai.api.ApiFormTextField
import com.draw2form.ai.api.ApiFormToggleSwitch
import com.draw2form.ai.presentation.screens.Checkbox
import com.draw2form.ai.presentation.screens.DynamicFormButton
import com.draw2form.ai.presentation.screens.FormAsyncImage
import com.draw2form.ai.presentation.screens.Label
import com.draw2form.ai.presentation.screens.TextField
import com.draw2form.ai.presentation.screens.ToggleSwitch
import com.draw2form.ai.presentation.screens.UIElement
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormEditScreen(
    items: List<UIElement>,
    onMove: (Int, Int) -> Unit,
    modifier: Modifier = Modifier,
//    onClick: (text: String, picture: String) -> Unit
) {
    val scope = rememberCoroutineScope()
    var overScrollJob by remember { mutableStateOf<Job?>(null) }
    val dragDropListState = rememberDragDropListState(onMove = onMove)
    val sheetState = rememberModalBottomSheetState()
    var editSheetOpen by rememberSaveable {
        mutableStateOf<UIElement?>(null)
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
                            onClick = { editSheetOpen = item },
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
    editSheetOpen?.let {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { editSheetOpen = null }) {
            EditElementBottomSheet(it)
        }
    }
}

@Composable
fun EditElementBottomSheet(element: UIElement) {
    Column {
        when (element) {
            is ApiFormLabel -> Column {
                var newElementState by rememberSaveable(saver = stateSaver()) {
                    mutableStateOf<ApiFormLabel>(element.copy())
                }
                Label(newElementState.label)
                TextField(label = "Label", value = newElementState.label, onChange = {
                    var label = newElementState.copy(
                        label = it
                    )
                    newElementState = label

                })
//                label = label.copy(label = it)

            }

            is ApiFormImage -> FormAsyncImage(
                url = "https://placekitten.com/1000/500?image=12",
                modifier = Modifier
                    .fillMaxSize()
            )

            is ApiFormTextField -> Column {
//                TextField(label = "Label", value = element.label)

            }

            is ApiFormCheckbox -> Column {
                Checkbox(element.label)
//                TextField(label = "Label", value = element.label)

            }

            is ApiFormToggleSwitch -> Column {
                ToggleSwitch(element.label)
//                TextField(label = "Label", value = element.label)

            }

            is ApiFormButton -> Column {
                DynamicFormButton(element.label)
//                TextField(label = "Label", value = element.label)

            }
        }
    }
}

fun <T> stateSaver() = Saver<MutableState<T>, Any>(
    save = { state -> state.value ?: "null" },
    restore = { value ->
        @Suppress("UNCHECKED_CAST")
        mutableStateOf((if (value == "null") null else value) as T)
    }
)

@Preview(showBackground = true)
@Composable
fun FormEditScreenPreview() {
}
















