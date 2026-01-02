package no.nordicsemi.android.dfu.app.onboarding.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.nordicsemi.android.dfu.app.theme.DaylightColors
import no.nordicsemi.android.dfu.app.theme.DaylightTheme
import no.nordicsemi.android.dfu.app.R

@Composable
fun WelcomeScreen(
    onContinue: () -> Unit
) {
    DaylightTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
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

                // Logo placeholder with white background
                androidx.compose.material3.Surface(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .heightIn(max = 220.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    tonalElevation = 0.dp
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                        painter = painterResource(id = R.drawable.keyboard_case_guidance),
                        contentDescription = "Daylight",
                        // Preserve the logo's intrinsic aspect ratio without stretching
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )
                    }
                }
                
                Text(
                    text = "Get ready for your Daylight Keyboard Case!",
                    style = MaterialTheme.typography.headlineMedium,
                    color = DaylightColors.TextPrimary,
                    textAlign = TextAlign.Center
                )

                Button(
                    onClick = onContinue,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DaylightColors.PrimaryAccent,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(999.dp)
                ) {
                    Text(text = "Get started", fontSize = 16.sp)
                }
            }
        }
    }
}

