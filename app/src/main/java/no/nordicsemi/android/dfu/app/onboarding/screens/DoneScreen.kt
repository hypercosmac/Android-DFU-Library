package no.nordicsemi.android.dfu.app.onboarding.screens

import androidx.compose.animation.core.*
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
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
import no.nordicsemi.android.dfu.app.theme.DaylightColors
import no.nordicsemi.android.dfu.app.theme.DaylightTheme

@Composable
fun DoneScreen(
    onBegin: () -> Unit
) {
    // Celebration animations
    var visible by remember { mutableStateOf(false) }
    var iconScaleState by remember { mutableStateOf(1.2f) }
    
    LaunchedEffect(Unit) {
        visible = true
        // Bounce animation for icon
        kotlinx.coroutines.delay(200)
        iconScaleState = 1f
    }
    
    val iconAnimatedScale by animateFloatAsState(
        targetValue = iconScaleState,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "icon_scale"
    )
    
    val iconAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(600, easing = FastOutSlowInEasing),
        label = "icon_alpha"
    )
    
    val titleAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(800, delayMillis = 200, easing = FastOutSlowInEasing),
        label = "title_alpha"
    )
    
    val subtitleAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(800, delayMillis = 400, easing = FastOutSlowInEasing),
        label = "subtitle_alpha"
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
                        Brush.radialGradient(
                            colors = listOf(
                                DaylightColors.PrimaryAccent.copy(alpha = 0.05f),
                                DaylightColors.BackgroundPrimary
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
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    // Animated success icon with celebration effect
                    Surface(
                        modifier = Modifier
                            .size(120.dp)
                            .scale(iconAnimatedScale)
                            .alpha(iconAlpha),
                        shape = RoundedCornerShape(999.dp),
                        color = DaylightColors.PrimaryAccent.copy(alpha = 0.1f),
                        tonalElevation = 0.dp
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Complete",
                                modifier = Modifier.size(80.dp),
                                tint = DaylightColors.PrimaryAccent
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // Animated title
                    Text(
                        text = "All Set!",
                        style = MaterialTheme.typography.headlineMedium,
                        color = DaylightColors.TextPrimary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.alpha(titleAlpha)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    // Animated subtitle
                    Text(
                        text = "Your keyboard case is ready to use. You can start using it right away!",
                        style = MaterialTheme.typography.bodyLarge,
                        color = DaylightColors.TextSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.alpha(subtitleAlpha)
                    )
                }

                // Animated button
                Button(
                    onClick = {
                        buttonPressed = true
                        onBegin()
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
                        text = "Begin",
                        fontSize = 17.sp,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
            }
        }
    }
}

