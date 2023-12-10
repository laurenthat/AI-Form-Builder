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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.draw2form.ai.R
import com.draw2form.ai.presentation.screens.FormLabel
import com.draw2form.ai.presentation.screens.FormTextField
import com.draw2form.ai.presentation.screens.UIComponent
import com.draw2form.ai.presentation.screens.UploadCard
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.MultipartBody


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormEditScreenTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onBackClick: () -> Unit,
    onPublish: () -> Unit,
) {
    MediumTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(
                text = "Edit Form",
                style = MaterialTheme.typography.labelLarge,
                fontSize = 20.sp
            )
        },
        navigationIcon = {
            IconButton(
                modifier = Modifier,
                onClick = { onBackClick() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            IconButton(
                modifier = Modifier,
                onClick = { onPublish() }
            ) {
                Icon(
                    Icons.Filled.Check,
                    contentDescription = "Publish",
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                )
            }
        },
        scrollBehavior = scrollBehavior

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormEditScreen(
    items: List<UIComponent>,
    onMove: (Int, Int) -> Unit,
    onPublish: () -> Unit,
    modifier: Modifier = Modifier,
    onUIComponentUpdate: (UIComponent) -> Unit,
    onImageUIComponentUpdate: (component: UIComponent, file: MultipartBody.Part) -> Unit,
    onUIComponentDelete: (UIComponent) -> Unit,
    onAddUIComponent: (UIComponent) -> Unit,
    onBackClick: () -> Unit,

    ) {
    val scope = rememberCoroutineScope()
    var overScrollJob by remember { mutableStateOf<Job?>(null) }
    val dragDropListState = rememberDragDropListState(onMove = onMove)
    val sheetState = rememberModalBottomSheetState()
    var editSheetOpen by remember {
        mutableStateOf<UIComponent?>(null)
    }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        topBar = {
            FormEditScreenTopBar(
                scrollBehavior = scrollBehavior,
                onBackClick = onBackClick,
                onPublish = { onPublish() }
            )
        },
        bottomBar = {
            editSheetOpen?.let {
                var bottomSheetUIComponent by remember() {
                    mutableStateOf<UIComponent>(it.copy())
                }
                ModalBottomSheet(
                    sheetState = sheetState,
                    onDismissRequest = {
                        onUIComponentUpdate(bottomSheetUIComponent)
                        editSheetOpen = null
                    }
                ) {
                    EditElementBottomSheet(bottomSheetUIComponent, onChange = {
                        bottomSheetUIComponent = it
                    }, onImageSelected = { file ->
                        onImageUIComponentUpdate(it, file)
                        editSheetOpen = null
                    })
                }
            }
            FormComponentsBottomSheet(
                onAdd = {
                    onAddUIComponent(it)
                },
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(top = padding.calculateTopPadding())
        ) {
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
                    .fillMaxSize(),
                state = dragDropListState.lazyListState
            ) {
                itemsIndexed(items, key = { index, item ->
                    println(item.toString())
                    item.toString()
                }) { index, item ->
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.drag_indicator),
                                contentDescription = null,
                                tint = Color.LightGray,
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
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
                                IconButton(onClick = {
                                    onUIComponentDelete(item)
                                }) {
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
    }
}

@Composable
fun EditElementBottomSheet(
    uiComponent: UIComponent,
    onChange: (updatedUIComponent: UIComponent) -> Unit,
    onImageSelected: (body: MultipartBody.Part) -> Unit
) {
    //
    Column {
        uiComponent.label?.let { labelComponent ->
            Column {
                FormLabel(labelComponent.label)
                Spacer(modifier = Modifier.height(8.dp))
                FormTextField(label = "Label", value = labelComponent.label, onChange = {
                    var updatedUIComponent = uiComponent.copy(
                        label = labelComponent.copy(label = it)
                    )
                    onChange(updatedUIComponent)

                })
                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        uiComponent.textField?.let { textFieldComponent ->
            Column {
                FormLabel(textFieldComponent.label)
                Spacer(modifier = Modifier.height(8.dp))
                FormTextField(label = "Label", value = textFieldComponent.label, onChange = {
                    var updatedUIComponent = uiComponent.copy(
                        textField = textFieldComponent.copy(label = it)
                    )
                    onChange(updatedUIComponent)
                })
                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        uiComponent.checkbox?.let { checkboxComponent ->
            Column {
                FormLabel(checkboxComponent.label)
                Spacer(modifier = Modifier.height(8.dp))
                FormTextField(label = "Label", value = checkboxComponent.label, onChange = {
                    var updatedUIComponent = uiComponent.copy(
                        checkbox = checkboxComponent.copy(label = it)
                    )
                    onChange(updatedUIComponent)
                })
                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        uiComponent.toggleSwitch?.let { toggleSwitchComponent ->
            Column {
                FormLabel(toggleSwitchComponent.label)
                Spacer(modifier = Modifier.height(8.dp))
                FormTextField(label = "Label", value = toggleSwitchComponent.label, onChange = {
                    var updatedUIComponent = uiComponent.copy(
                        toggleSwitch = toggleSwitchComponent.copy(label = it)
                    )
                    onChange(updatedUIComponent)
                })
                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        uiComponent.button?.let { buttonComponent ->
            Column {
                FormLabel(buttonComponent.label)
                Spacer(modifier = Modifier.height(8.dp))
                FormTextField(label = "Label", value = buttonComponent.label, onChange = {
                    var updatedUIComponent = uiComponent.copy(
                        button = buttonComponent.copy(label = it)
                    )
                    onChange(updatedUIComponent)
                })
                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        uiComponent.image?.let { buttonComponent ->
            Column {
                UploadCard(
                    cameraText = "Camera",
                    galleryText = "Gallery",
                    onBitmapReady = {},
                    onMultipartBodyReady = {
                        onImageSelected(it)
                    }
                )
                Spacer(modifier = Modifier.height(100.dp))
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun FormEditScreenPreview() {
}
















