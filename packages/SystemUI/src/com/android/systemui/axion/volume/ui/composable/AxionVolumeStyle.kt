/*
 * Copyright (C) 2024-2026 Lunaris AOSP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.systemui.axion.volume.ui.composable

import android.content.Context
import android.database.ContentObserver
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.android.systemui.axion.volume.ui.composable.lunaris.LunarisVolumeDialogContent
import com.android.systemui.axion.volume.ui.viewmodel.AxionVolumeDialogViewModel

enum class AxionVolumeStyle(val value: Int) {
    AXION(0),
    ONE_UI(1);

    companion object {
        const val SETTINGS_KEY = "axion_volume_style"

        fun from(value: Int): AxionVolumeStyle =
            entries.firstOrNull { it.value == value } ?: AXION
    }
}

@Composable
fun rememberAxionVolumeStyle(): State<AxionVolumeStyle> {
    val context = LocalContext.current
    val state = remember { mutableStateOf(readStyle(context)) }

    DisposableEffect(Unit) {
        val handler = Handler(Looper.getMainLooper())
        val observer = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                state.value = readStyle(context)
            }
        }
        context.contentResolver.registerContentObserver(
            Settings.System.getUriFor(AxionVolumeStyle.SETTINGS_KEY),
            /* notifyForDescendants = */ false,
            observer
        )
        onDispose {
            context.contentResolver.unregisterContentObserver(observer)
        }
    }

    return state
}

private fun readStyle(context: Context): AxionVolumeStyle =
    AxionVolumeStyle.from(
        Settings.System.getInt(
            context.contentResolver,
            AxionVolumeStyle.SETTINGS_KEY,
            AxionVolumeStyle.AXION.value
        )
    )

@Composable
fun AxionVolumeStyleDispatcher(viewModel: AxionVolumeDialogViewModel) {
    val style by rememberAxionVolumeStyle()
    when (style) {
        AxionVolumeStyle.AXION  -> AxionVolumeDialogContent(viewModel)
        AxionVolumeStyle.ONE_UI -> LunarisVolumeDialogContent(viewModel)
    }
}
