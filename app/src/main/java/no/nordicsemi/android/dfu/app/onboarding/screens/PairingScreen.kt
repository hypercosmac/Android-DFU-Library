package no.nordicsemi.android.dfu.app.onboarding.screens

import androidx.compose.animation.core.*
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.zIndex
import no.nordicsemi.android.dfu.app.theme.DaylightColors
import no.nordicsemi.android.dfu.app.theme.DaylightTheme
import no.nordicsemi.android.dfu.app.R
import no.nordicsemi.android.dfu.app.onboarding.BluetoothDevice

@Composable
fun PairingScreen(
    isScanning: Boolean,
    foundDevice: String?,
    allDevices: List<BluetoothDevice>,
    isConnected: Boolean,
    connectedDeviceAddress: String? = null,
    connectionError: String? = null,
    onStartScan: () -> Unit,
    onPairClick: (String?) -> Unit,
    onContinue: () -> Unit
) {
    // Entrance animations
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        visible = true
    }
    
    val cardScale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.95f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "card_scale"
    )
    
    val cardAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(500, easing = FastOutSlowInEasing),
        label = "card_alpha"
    )
    
    var buttonPressed by remember { mutableStateOf(false) }
    var showConfetti by remember { mutableStateOf(false) }
    
    // Show confetti when connected
    LaunchedEffect(isConnected) {
        if (isConnected) {
            showConfetti = true
            kotlinx.coroutines.delay(2000) // Show for 2 seconds
            showConfetti = false
        }
    }
    
    DaylightTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Confetti animation overlay
            ConfettiAnimation(
                visible = showConfetti,
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(10f)
            )
            
            // Background image at 20% opacity
            Image(
                painter = painterResource(id = R.drawable.ink_painting_9971068_1920),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.2f)
            )
            
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                DaylightColors.BackgroundPrimary,
                                DaylightColors.Surface.copy(alpha = 0.5f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top "dashboard" card with luxury animations
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(cardScale)
                        .alpha(cardAlpha),
                    shape = RoundedCornerShape(32.dp),
                    color = DaylightColors.Surface,
                    tonalElevation = 12.dp,
                    shadowElevation = 8.dp
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
                                    painter = painterResource(id = R.drawable.keyboard_case_hero1),
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

                // Middle section with device list or status
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(top = 32.dp),
                    shape = RoundedCornerShape(32.dp),
                    color = DaylightColors.Overlay.copy(alpha = 0.12f),
                    tonalElevation = 0.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
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
                                .size(48.dp)
                                .alpha(if (isScanning && !isConnected) alpha else 1f),
                            tint = if (isConnected) DaylightColors.PrimaryAccent else DaylightColors.TextSecondary
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        when {
                            isConnected -> {
                                Text(
                                    text = "Connected",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = DaylightColors.TextPrimary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = foundDevice ?: "Keyboard Case",
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
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Device list
                        if (allDevices.isNotEmpty()) {
                            Text(
                                text = "Available devices:",
                                style = MaterialTheme.typography.bodyMedium,
                                color = DaylightColors.TextSecondary,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(allDevices) { device ->
                                    DeviceListItem(
                                        device = device,
                                        isHighlighted = device.name?.contains("DAYLIGHT_KB-1", ignoreCase = true) == true,
                                        onClick = {
                                            if (!isConnected) {
                                                onPairClick(device.address)
                                            }
                                        },
                                        isConnected = isConnected && connectedDeviceAddress == device.address
                                    )
                                }
                            }
                        } else if (isScanning) {
                            Text(
                                text = "Scanning for devices...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = DaylightColors.TextSecondary,
                                textAlign = TextAlign.Center
                            )
                        } else {
                            Text(
                                text = "Bring your keyboard close. When you're ready, discover devices and pick your keyboard to pair.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = DaylightColors.TextSecondary,
                                textAlign = TextAlign.Center
                            )
                        }
                        
                        // Show error message if present
                        if (connectionError != null) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                            ) {
                                Text(
                                    text = connectionError,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.error,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
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
                    // Discover button with animation
                    Button(
                        onClick = {
                            buttonPressed = true
                            onStartScan()
                        },
                        enabled = !isScanning,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .scale(if (buttonPressed && !isScanning) 0.95f else 1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DaylightColors.PrimaryAccent,
                            contentColor = Color.White,
                            disabledContainerColor = DaylightColors.PrimaryAccent.copy(alpha = 0.6f)
                        ),
                        shape = RoundedCornerShape(999.dp),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp,
                            pressedElevation = 2.dp
                        )
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


                    // Continue button appears when connected
                    AnimatedVisibility(
                        visible = isConnected,
                        enter = slideInVertically() + fadeIn(),
                        exit = slideOutVertically() + fadeOut()
                    ) {
                        Button(
                            onClick = {
                                buttonPressed = true
                                onContinue()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .scale(if (buttonPressed) 0.95f else 1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DaylightColors.PrimaryAccent,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(999.dp),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 4.dp,
                                pressedElevation = 2.dp
                            )
                        ) {
                            Text(
                                "Continue",
                                fontSize = 17.sp,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            }
            }
        }
    }
}

@Composable
private fun DeviceListItem(
    device: BluetoothDevice,
    isHighlighted: Boolean,
    onClick: () -> Unit,
    isConnected: Boolean
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !isConnected, onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = Color.Transparent,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        border = if (isHighlighted) {
            BorderStroke(
                2.dp,
                DaylightColors.PrimaryAccent.copy(alpha = 0.5f)
            )
        } else null
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = if (isHighlighted) {
                        DaylightColors.PrimaryAccent.copy(alpha = 0.15f)
                    } else {
                        DaylightColors.Overlay.copy(alpha = 0.08f)
                    },
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = device.name ?: "Unknown Device",
                        style = MaterialTheme.typography.bodyLarge,
                        color = DaylightColors.TextPrimary,
                        fontWeight = if (isHighlighted) FontWeight.Bold else FontWeight.Normal
                    )
                    if (isHighlighted) {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = DaylightColors.PrimaryAccent.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Recommended",
                                style = MaterialTheme.typography.labelSmall,
                                color = DaylightColors.PrimaryAccent,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
                if (isConnected) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Connected",
                        style = MaterialTheme.typography.bodySmall,
                        color = DaylightColors.PrimaryAccent,
                        fontSize = 12.sp
                    )
                }
            }
            Icon(
                imageVector = Icons.Default.Bluetooth,
                contentDescription = null,
                tint = if (isConnected) DaylightColors.PrimaryAccent else if (isHighlighted) DaylightColors.PrimaryAccent else DaylightColors.TextSecondary,
                modifier = Modifier.size(24.dp)
            )
        }
        }
    }
}

