package no.nordicsemi.android.dfu.app.onboarding.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import no.nordicsemi.android.dfu.app.theme.DaylightColors
import no.nordicsemi.android.dfu.app.theme.DaylightTheme

@Composable
fun GuidanceScreen(
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(48.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Subtle line drawing animation placeholder
                val infiniteTransition = rememberInfiniteTransition(label = "drawing")
                val drawingAlpha by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(2000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    ),
                    label = "drawing"
                )
                
                // Placeholder for clamshell illustration
                Box(
                    modifier = Modifier
                        .size(300.dp)
                        .background(DaylightColors.Surface.copy(alpha = 0.3f))
                        .alpha(drawingAlpha)
                )
                
                Spacer(modifier = Modifier.height(64.dp))
                
                Text(
                    text = "Open.",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Medium,
                    color = DaylightColors.TextPrimary,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Type.",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Medium,
                    color = DaylightColors.TextPrimary,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Begin.",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Medium,
                    color = DaylightColors.TextPrimary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

