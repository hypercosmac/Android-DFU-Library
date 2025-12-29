package no.nordicsemi.android.dfu.app.onboarding.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import no.nordicsemi.android.dfu.app.onboarding.OnboardingStep
import no.nordicsemi.android.dfu.app.theme.DaylightColors
import no.nordicsemi.android.dfu.app.theme.DaylightTheme

@Composable
fun WelcomeScreen(
    onContinue: () -> Unit
) {
    DaylightTheme {
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
                )
                .clickable { onContinue() },
            contentAlignment = Alignment.Center
        ) {
            // Product illustration placeholder - would use actual Clamshell.png
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(32.dp)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                
                // Placeholder for product illustration
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .background(DaylightColors.Surface.copy(alpha = 0.3f))
                )
                
                Spacer(modifier = Modifier.height(48.dp))
                
                Text(
                    text = "Made for focus.",
                    style = MaterialTheme.typography.headlineMedium,
                    color = DaylightColors.TextPrimary,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

