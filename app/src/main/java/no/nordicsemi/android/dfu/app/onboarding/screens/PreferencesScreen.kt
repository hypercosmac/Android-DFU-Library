package no.nordicsemi.android.dfu.app.onboarding.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import no.nordicsemi.android.dfu.app.onboarding.OnboardingPreferences
import no.nordicsemi.android.dfu.app.theme.DaylightColors
import no.nordicsemi.android.dfu.app.theme.DaylightTheme

@Composable
fun PreferencesScreen(
    preferences: OnboardingPreferences,
    onPreferenceChanged: (OnboardingPreferences) -> Unit,
    onContinue: () -> Unit
) {
    DaylightTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DaylightColors.BackgroundPrimary)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(48.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Preferences",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Medium,
                    color = DaylightColors.TextPrimary,
                    modifier = Modifier.padding(bottom = 48.dp)
                )
                
                // Only 3 preferences max
                PreferenceItem(
                    title = "Keyboard Backlight",
                    checked = preferences.keyboardBacklight,
                    onCheckedChange = {
                        onPreferenceChanged(preferences.copy(keyboardBacklight = it))
                    }
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                PreferenceItem(
                    title = "Key Sound",
                    checked = preferences.keySound,
                    onCheckedChange = {
                        onPreferenceChanged(preferences.copy(keySound = it))
                    }
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                PreferenceItem(
                    title = "Auto Sleep",
                    checked = preferences.autoSleep,
                    onCheckedChange = {
                        onPreferenceChanged(preferences.copy(autoSleep = it))
                    }
                )
                
                Spacer(modifier = Modifier.height(48.dp))
                
                Button(
                    onClick = onContinue,
                    modifier = Modifier.fillMaxWidth(0.7f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DaylightColors.PrimaryAccent,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Continue")
                }
            }
        }
    }
}

@Composable
private fun PreferenceItem(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = DaylightColors.TextPrimary
        )
        
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = DaylightColors.PrimaryAccent,
                checkedTrackColor = DaylightColors.PrimaryAccent.copy(alpha = 0.5f)
            )
        )
    }
}

