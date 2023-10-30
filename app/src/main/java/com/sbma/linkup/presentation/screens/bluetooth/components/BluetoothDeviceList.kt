package com.sbma.linkup.presentation.screens.bluetooth.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sbma.linkup.bluetooth.models.BluetoothDeviceDomain
import com.sbma.linkup.presentation.icons.Bluetooth


@SuppressLint("MissingPermission")
@Composable
fun BluetoothDeviceList(
    data: List<BluetoothDeviceDomain>,
    modifier: Modifier = Modifier,
    onClick: (device: BluetoothDeviceDomain) -> Unit
) {
    var deviceName = rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            Column {
                BluetoothListScreenTopBar()
            }
        },
        modifier = Modifier
            .fillMaxSize()
    ) {padding ->

    Surface(
        modifier = modifier,
        shadowElevation = 4.dp,
        tonalElevation = 4.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            DeviceSearch(onValueChange = {
                deviceName.value = it
            })
            LazyColumn(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(1.dp),
            ) {
                items(data.filter { (it.name ?: "").contains(deviceName.value, ignoreCase = true) }) { scanResult ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onClick(scanResult) }
                            .padding(6.dp),
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(){

                                Icon(
                                    Icons.Filled.Bluetooth,
                                    contentDescription = "AccountCircle",
                                    modifier = Modifier
                                        .size(46.dp)
                                        .padding(8.dp, top = 14.dp)

                                )
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        text = scanResult.address,
                                        fontWeight = FontWeight.Bold
                                    )

                                    scanResult.name?.let {
                                        Text(
                                            text = it,
                                            fontWeight = FontWeight.Normal
                                        )
                                    }
                                }
                            }


                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceSearch(onValueChange: (String) -> Unit) {
    var searchText by rememberSaveable { mutableStateOf("") }
    var showClearIcon by rememberSaveable { mutableStateOf(false) }

    if (searchText.isEmpty()) {
        showClearIcon = false
    } else if (searchText.isNotEmpty()) {
        showClearIcon = true
    }

    OutlinedTextField(
        value = searchText,
        onValueChange = {
            searchText = it
            onValueChange(it)
        },
        placeholder = { Text("Search Devices") },
        singleLine = true,
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "searchIcon") },
        trailingIcon = {
            if (showClearIcon) {
                IconButton(onClick = { searchText = "" }) {
                    Icon(
                        imageVector = Icons.Rounded.Clear,
                        tint = MaterialTheme.colorScheme.onBackground,
                        contentDescription = "Clear Icon"
                    )
                }
            }
        },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(16.dp)
            )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BluetoothListScreenTopBar() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    MediumTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(
                text = "Bluetooth Lists",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 20.sp
            )
        },
        scrollBehavior = scrollBehavior
    )
}


