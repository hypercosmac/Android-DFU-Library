package no.nordicsemi.android.dfu.app.onboarding.screens

import androidx.compose.animation.core.*
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
                .background(DaylightColors.BackgroundPrimary),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(32.dp)
            ) {
                // Glass pill container
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 48.dp),
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
                    }
                }
            }
        }
    }
}

