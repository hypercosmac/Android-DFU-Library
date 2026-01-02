package no.nordicsemi.android.dfu.app.onboarding.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    DaylightTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DaylightColors.BackgroundPrimary),
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
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(32.dp))

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = DaylightColors.Surface,
                        tonalElevation = 4.dp
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
                    onClick = onContinue,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DaylightColors.PrimaryAccent,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(999.dp)
                ) {
                    Text(text = "Continue", fontSize = 16.sp)
                }
            }
        }
    }
}

