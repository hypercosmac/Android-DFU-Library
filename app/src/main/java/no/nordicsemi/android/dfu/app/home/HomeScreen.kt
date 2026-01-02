package no.nordicsemi.android.dfu.app.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import no.nordicsemi.android.dfu.app.R
import no.nordicsemi.android.dfu.app.theme.DaylightColors
import no.nordicsemi.android.dfu.app.theme.DaylightTheme

@Composable
fun HomeScreen(
    onNavigateToDFU: () -> Unit,
    onNavigateToKeyboardConfig: () -> Unit,
    onNavigateToOnboarding: () -> Unit
) {
    DaylightTheme {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
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
                    .background(Color.White)
            ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                
                // Title
                Text(
                    text = "Device Manager",
                    style = MaterialTheme.typography.headlineLarge,
                    color = DaylightColors.TextPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Text(
                    text = "Manage your keyboard and firmware",
                    style = MaterialTheme.typography.bodyMedium,
                    color = DaylightColors.TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 32.dp)
                )
                
                // Navigation Cards
                NavigationCard(
                    title = "Firmware Updates",
                    description = "Update device firmware over-the-air",
                    icon = Icons.Default.CloudUpload,
                    onClick = onNavigateToDFU,
                    modifier = Modifier.fillMaxWidth()
                )
                
                NavigationCard(
                    title = "Keyboard Configuration",
                    description = "Configure keyboard settings and preferences",
                    icon = Icons.Default.Settings,
                    onClick = onNavigateToKeyboardConfig,
                    modifier = Modifier.fillMaxWidth()
                )
                
                NavigationCard(
                    title = "Start Onboarding",
                    description = "Pair a new device or reset connection",
                    icon = Icons.Default.Bluetooth,
                    onClick = onNavigateToOnboarding,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.weight(1f))
            }
            }
        }
    }
}

@Composable
private fun NavigationCard(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon container
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFF5F5F5),
                modifier = Modifier.size(56.dp),
                tonalElevation = 0.dp
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = DaylightColors.PrimaryAccent,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
            
            // Text content
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = DaylightColors.TextPrimary,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = DaylightColors.TextSecondary
                )
            }
            
            // Arrow icon
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = DaylightColors.TextSecondary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

