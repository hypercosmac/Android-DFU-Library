package no.nordicsemi.android.dfu.app.onboarding.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import no.nordicsemi.android.dfu.app.theme.DaylightColors
import no.nordicsemi.android.dfu.app.theme.DaylightTheme

@Composable
fun IntroScreen(
    onContinue: () -> Unit
) {
    DaylightTheme {
        val configuration = LocalConfiguration.current
        val isLandscape = configuration.screenWidthDp > configuration.screenHeightDp
        
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DaylightColors.BackgroundPrimary)
        ) {
            
            if (isLandscape) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalArrangement = Arrangement.spacedBy(48.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Product render placeholder
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(DaylightColors.Surface.copy(alpha = 0.3f))
                    )
                    
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Keyboard Case 1",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Medium,
                            color = DaylightColors.TextPrimary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Made for Daylight DC-1",
                            style = MaterialTheme.typography.bodyLarge,
                            color = DaylightColors.TextSecondary
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    
                    // Product render placeholder
                    Box(
                        modifier = Modifier
                            .size(240.dp)
                            .background(DaylightColors.Surface.copy(alpha = 0.3f))
                    )
                    
                    Spacer(modifier = Modifier.height(48.dp))
                    
                    Text(
                        text = "Keyboard Case 1",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Medium,
                        color = DaylightColors.TextPrimary,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Made for Daylight DC-1",
                        style = MaterialTheme.typography.bodyLarge,
                        color = DaylightColors.TextSecondary,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

