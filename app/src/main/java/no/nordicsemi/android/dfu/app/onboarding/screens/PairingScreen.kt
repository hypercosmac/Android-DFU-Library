package no.nordicsemi.android.dfu.app.onboarding.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import no.nordicsemi.android.dfu.app.theme.DaylightColors
import no.nordicsemi.android.dfu.app.theme.DaylightTheme

@Composable
fun PairingScreen(
    isScanning: Boolean,
    foundDevice: String?,
    isConnected: Boolean,
    onStartScan: () -> Unit,
    onContinue: () -> Unit
) {
    DaylightTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(32.dp)
            ) {
<<<<<<< Updated upstream
                // Glass pill container
=======
                // Top "dashboard" card
                Surface(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(32.dp),
                    color = Color.White,
                    tonalElevation = 0.dp,
                    border = BorderStroke(1.dp, Color(0xFFE0E0E0))
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
                                    text = "Bluetooth Device",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = DaylightColors.TextPrimary
                                )
                                Text(
                                    text = "Select a device to update",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = DaylightColors.TextSecondary
                                )
                            }

                            // Simple icon placeholder
                            Surface(
                                shape = RoundedCornerShape(24.dp),
                                color = Color.White,
                                tonalElevation = 0.dp,
                                border = BorderStroke(1.dp, Color(0xFFE0E0E0))
                            ) {
                                Box(
                                    modifier = Modifier.size(72.dp),
                                    contentAlignment = Alignment.Center
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
                        }

                        // Status strip
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            color = Color(0xFFF5F5F5),
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
                                        text = foundDevice ?: "No device found",
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

                // Middle section with animated bluetooth
>>>>>>> Stashed changes
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 48.dp),
                    shape = RoundedCornerShape(32.dp),
                    color = Color.White,
                    tonalElevation = 0.dp,
                    border = BorderStroke(1.dp, Color(0xFFE0E0E0))
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
<<<<<<< Updated upstream
=======
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Turn on your keyboard case. When you’re ready, discover devices and pick your keyboard to pair.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = DaylightColors.TextSecondary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 4.dp)
                        )
>>>>>>> Stashed changes
                    }
                }
                
                Spacer(modifier = Modifier.height(48.dp))
                
                Text(
                    text = "Bring your keyboard close.\nWe'll take care of the rest.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = DaylightColors.TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
                
                if (!isScanning && !isConnected && foundDevice == null) {
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = onStartScan,
                        modifier = Modifier.fillMaxWidth(0.6f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DaylightColors.PrimaryAccent,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text("Start Scanning")
                    }
<<<<<<< Updated upstream
                }
                
                if (isConnected) {
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = onContinue,
                        modifier = Modifier.fillMaxWidth(0.6f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DaylightColors.PrimaryAccent,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text("Continue")
=======

                    // Pair button appears when a device is found but not yet connected
                    if (foundDevice != null && !isConnected) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(999.dp),
                            color = Color.White,
                            tonalElevation = 0.dp,
                            border = BorderStroke(1.dp, Color(0xFFE0E0E0))
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

                    // Always allow the user to move forward, even if pairing hasn't completed.
                    Button(
                        onClick = onContinue,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = DaylightColors.TextPrimary
                        ),
                        shape = RoundedCornerShape(999.dp)
                    ) {
                        Text(if (isConnected) "Continue" else "Skip for now")
>>>>>>> Stashed changes
                    }
                }
            }
        }
    }
}

