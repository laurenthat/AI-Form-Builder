/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sbma.linkup.application

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.sbma.linkup.bluetooth.AppBluetoothViewModel
import com.sbma.linkup.card.CardViewModel
import com.sbma.linkup.connection.ConnectionViewModel
import com.sbma.linkup.nfc.NFCViewModel
import com.sbma.linkup.user.UserViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            UserViewModel(
                userRepository = MyApplication().container.userRepository,
                cardRepository = MyApplication().container.cardRepository,
                dataStore = MyApplication().dataStore,
                apiService = MyApplication().container.apiService,
                userConnectionRepository = MyApplication().container.userConnectionRepository
            )
        }
        addInitializer(NFCViewModel::class) {
            NFCViewModel(
                MyApplication().appNfcManager
            )
        }
        addInitializer(CardViewModel::class) {
            CardViewModel(
                MyApplication().container.cardRepository,
                MyApplication().dataStore,
                MyApplication().container.apiService
            )
        }
        addInitializer(ConnectionViewModel::class) {
            ConnectionViewModel(
                MyApplication().container.userConnectionRepository
            )
        }
        addInitializer(AppBluetoothViewModel::class) {
            AppBluetoothViewModel(
                appBluetoothManager = MyApplication().appBluetoothManager,
                appBroadcastReceiver = MyApplication().appBroadcastReceiver

            )
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of [MyApplication].
 */
fun CreationExtras.MyApplication(): MyApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)
