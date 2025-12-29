package no.nordicsemi.android.dfu.app.onboarding.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import no.nordicsemi.android.dfu.app.theme.DaylightColors
import no.nordicsemi.android.dfu.app.theme.DaylightTheme
import no.nordicsemi.android.dfu.app.R

@Composable
fun PairingScreen(
    isScanning: Boolean,
    foundDevice: String?,
    isConnected: Boolean,
    onStartScan: () -> Unit,
    onPairClick: () -> Unit,
    onContinue: () -> Unit
) {
    DaylightTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DaylightColors.BackgroundPrimary),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top "dashboard" card inspired by neumorphic layout
                Surface(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(32.dp),
                    color = DaylightColors.Surface,
                    tonalElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Keyboard Case 1",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = DaylightColors.TextPrimary
                                )
                                Text(
                                    text = "Made for Daylight DC‑1",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = DaylightColors.TextSecondary
                                )
                            }

                            // Placeholder product image
                            Surface(
                                shape = RoundedCornerShape(24.dp),
                                color = DaylightColors.BackgroundPrimary,
                                tonalElevation = 4.dp
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.keyboard_case_hero),
                                    contentDescription = "Keyboard case",
                                    modifier = Modifier
                                        .size(72.dp),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }

                        // Subtle status strip
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            color = DaylightColors.Overlay.copy(alpha = 0.06f),
                            tonalElevation = 0.dp
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp, vertical = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = if (isConnected) "Connected" else if (foundDevice != null) "Keyboard nearby" else if (isScanning) "Scanning…" else "Not connected",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = DaylightColors.TextPrimary
                                    )
                                    Text(
                                        text = foundDevice ?: "DAYLIGHT_KB‑1",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = DaylightColors.TextSecondary
                                    )
                                }

                                Icon(
                                    imageVector = Icons.Default.Bluetooth,
                                    contentDescription = null,
                                    tint = if (isConnected) DaylightColors.PrimaryAccent else DaylightColors.TextSecondary
                                )
                            }
                        }
                    }
                }

                // Middle pill with animated bluetooth + copy
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp),
                    shape = RoundedCornerShape(32.dp),
                    color = DaylightColors.Overlay.copy(alpha = 0.12f),
                    tonalElevation = 0.dp
                ) {
                    Column(
                        modifier = Modifier.padding(48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Bluetooth icon with pulse animation
                        val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                        val alpha by infiniteTransition.animateFloat(
                            initialValue = 0.3f,
                                targetValue = 1f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(1000, easing = FastOutSlowInEasing),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "alpha"
                        )
                        
                        Icon(
                            imageVector = Icons.Default.Bluetooth,
                            contentDescription = null,
                            modifier = Modifier
                                .size(64.dp)
                                .alpha(if (isScanning && !isConnected) alpha else 1f),
                            tint = if (isConnected) DaylightColors.PrimaryAccent else DaylightColors.TextSecondary
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        when {
                            isConnected -> {
                                Text(
                                    text = "Connected",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = DaylightColors.TextPrimary
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = foundDevice ?: "Keyboard Case",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = DaylightColors.TextSecondary
                                )
                            }
                            foundDevice != null -> {
                                Text(
                                    text = "Found keyboard",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = DaylightColors.TextPrimary
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = foundDevice,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = DaylightColors.TextSecondary
                                )
                            }
                            isScanning -> {
                                Text(
                                    text = "Searching…",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = DaylightColors.TextPrimary
                                )
                            }
                            else -> {
                                Text(
                                    text = "Ready to pair",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = DaylightColors.TextPrimary
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Bring your keyboard close. When you’re ready, discover devices and pick your keyboard to pair.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = DaylightColors.TextSecondary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                // Bottom action bar – discover + pair / continue
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Discover button
                    Button(
                        onClick = onStartScan,
                        enabled = !isScanning,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DaylightColors.PrimaryAccent,
                            contentColor = Color.White,
                            disabledContainerColor = DaylightColors.PrimaryAccent.copy(alpha = 0.6f)
                        ),
                        shape = RoundedCornerShape(999.dp)
                    ) {
                        if (isScanning) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(20.dp)
                                    .padding(end = 8.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Text("Scanning…", fontSize = 15.sp)
                        } else {
                            Text("Discover keyboards", fontSize = 15.sp)
                        }
                    }

                    // Pair button appears when a device is found but not yet connected
                    if (foundDevice != null && !isConnected) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(999.dp),
                            color = DaylightColors.Surface,
                            tonalElevation = 4.dp
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = foundDevice,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = DaylightColors.TextPrimary
                                    )
                                    Text(
                                        text = "Tap to pair and continue",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = DaylightColors.TextSecondary
                                    )
                                }
                                TextButton(onClick = onPairClick) {
                                    Text("Pair")
                                }
                            }
                        }
                    }

                    if (isConnected) {
                        Button(
                            onClick = onContinue,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DaylightColors.Surface,
                                contentColor = DaylightColors.TextPrimary
                            ),
                            shape = RoundedCornerShape(999.dp)
                        ) {
                            Text("Continue")
                        }
                    }
                }
            }
        }
    }
}

