/*
 * Copyright (c) 2022, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list
 * of conditions and the following disclaimer in the documentation and/or other materials
 * provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be
 * used to endorse or promote products derived from this software without specific prior
 * written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package no.nordicsemi.android.dfu.profile.main.view

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import no.nordicsemi.android.common.analytics.view.AnalyticsPermissionRequestDialog
import no.nordicsemi.android.common.logger.view.LoggerAppBarIcon
import no.nordicsemi.android.dfu.profile.main.R
import no.nordicsemi.android.dfu.profile.main.viewmodel.DFUViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DFUScreen() {
    val viewModel: DFUViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onEvent: (DFUViewEvent) -> Unit = { viewModel.onEvent(it) }
    val context = LocalContext.current

    // Medium gray color for the header bar
    val mediumGray = Color(0xFF808080) // Medium gray

    Box(modifier = Modifier.fillMaxSize()) {
        // Background image at 20% opacity
        Image(
            painter = painterResource(id = R.drawable.ink_painting_9971068_1920),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.2f)
        )
        
        Column(modifier = Modifier.fillMaxSize()) {
            // Top App Bar with medium gray color and back arrow
            TopAppBar(
                title = { Text(text = stringResource(R.string.dfu_title)) },
                navigationIcon = {
                    IconButton(onClick = { onEvent(NavigateUp) }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = mediumGray,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                actions = {
                    LoggerAppBarIcon(onClick = { onEvent(OnLoggerButtonClick) })
                    IconButton(onClick = { onEvent(OnSettingsButtonClick) }) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = stringResource(id = R.string.dfu_settings_action)
                        )
                    }
                }
            )

            // Download firmware button at the top
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://daylightcomputer.com/firmware/downloads/"))
                        context.startActivity(intent)
                    },
                    modifier = Modifier.widthIn(max = 600.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF000000)
                        
                    )
                ) {
                    Text(text = stringResource(R.string.dfu_download_firmware))
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedCard(
                    modifier = Modifier
                        .widthIn(max = 600.dp)
                        .padding(16.dp)
                        .padding(bottom = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        val dfuInProgress = state.isRunning()

                        DFUSelectFileView(state.fileViewEntity, !dfuInProgress, onEvent)

                        DFUSelectedDeviceView(state.deviceViewEntity, !dfuInProgress, onEvent)

                        DFUProgressView(state.progressViewEntity, onEvent)
                    }
                }
            }
        }

        // Allow user to Opt-In to analytics collection.
        AnalyticsPermissionRequestDialog()
    }
}
