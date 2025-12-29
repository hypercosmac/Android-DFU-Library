package no.nordicsemi.android.dfu.app.onboarding.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import no.nordicsemi.android.dfu.app.theme.DaylightColors
import no.nordicsemi.android.dfu.app.theme.DaylightTheme

@Composable
fun DoneScreen(
    onBegin: () -> Unit
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
                Text(
                    text = "Your keyboard is ready.",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Medium,
                    color = DaylightColors.TextPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 48.dp)
                )
                
                Button(
                    onClick = onBegin,
                    modifier = Modifier.fillMaxWidth(0.6f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DaylightColors.PrimaryAccent,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Begin")
                }
            }
        }
    }
}

