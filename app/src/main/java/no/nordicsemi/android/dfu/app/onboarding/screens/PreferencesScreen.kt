package no.nordicsemi.android.dfu.app.onboarding.screens

import androidx.compose.animation.core.*
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.nordicsemi.android.dfu.app.R
import no.nordicsemi.android.dfu.app.onboarding.OnboardingPreferences
import no.nordicsemi.android.dfu.app.theme.DaylightColors
import no.nordicsemi.android.dfu.app.theme.DaylightTheme

@Composable
fun PreferencesScreen(
    preferences: OnboardingPreferences,
    onPreferenceChanged: (OnboardingPreferences) -> Unit,
    onContinue: () -> Unit
) {
    var keyboardBacklight by remember { mutableStateOf(preferences.keyboardBacklight) }
    var keySound by remember { mutableStateOf(preferences.keySound) }
    var autoSleep by remember { mutableStateOf(preferences.autoSleep) }
    
    // Entrance animations
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        visible = true
    }
    
    val titleAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(600, easing = FastOutSlowInEasing),
        label = "title_alpha"
    )
    
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
        animationSpec = tween(800, delayMillis = 200, easing = FastOutSlowInEasing),
        label = "card_alpha"
    )
    
    val buttonScale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.9f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "button_scale"
    )
    
    var buttonPressed by remember { mutableStateOf(false) }

    DaylightTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
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
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                DaylightColors.BackgroundPrimary,
                                DaylightColors.Surface.copy(alpha = 0.3f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp, vertical = 40.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Preferences",
                        style = MaterialTheme.typography.headlineMedium,
                        color = DaylightColors.TextPrimary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .alpha(titleAlpha)
                    )
                    Spacer(modifier = Modifier.height(32.dp))

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .scale(cardScale)
                            .alpha(cardAlpha),
                        shape = RoundedCornerShape(24.dp),
                        color = DaylightColors.Surface,
                        tonalElevation = 12.dp,
                        shadowElevation = 8.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Keyboard Backlight",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = DaylightColors.TextPrimary
                                    )
                                    Text(
                                        text = "Enable keyboard backlight",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = DaylightColors.TextSecondary
                                    )
                                }
                                Switch(
                                    checked = keyboardBacklight,
                                    onCheckedChange = {
                                        keyboardBacklight = it
                                        onPreferenceChanged(
                                            preferences.copy(keyboardBacklight = it)
                                        )
                                    }
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Key Sound",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = DaylightColors.TextPrimary
                                    )
                                    Text(
                                        text = "Enable key press sounds",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = DaylightColors.TextSecondary
                                    )
                                }
                                Switch(
                                    checked = keySound,
                                    onCheckedChange = {
                                        keySound = it
                                        onPreferenceChanged(
                                            preferences.copy(keySound = it)
                                        )
                                    }
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Auto Sleep",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = DaylightColors.TextPrimary
                                    )
                                    Text(
                                        text = "Automatically sleep when idle",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = DaylightColors.TextSecondary
                                    )
                                }
                                Switch(
                                    checked = autoSleep,
                                    onCheckedChange = {
                                        autoSleep = it
                                        onPreferenceChanged(
                                            preferences.copy(autoSleep = it)
                                        )
                                    }
                                )
                            }
                        }
                    }
                }

                Button(
                    onClick = {
                        buttonPressed = true
                        onContinue()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .scale(if (buttonPressed) 0.95f else buttonScale),
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
                        text = "Continue",
                        fontSize = 17.sp,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
            }
        }
    }
}

