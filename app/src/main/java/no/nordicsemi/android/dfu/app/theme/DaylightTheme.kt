package no.nordicsemi.android.dfu.app.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Grayscale Color Palette
object DaylightColors {
    // Background (Primary): Pure White
    val BackgroundPrimary = Color(0xFFFFFFFF)
    
    // Surface (Cards): Light Gray
    val Surface = Color(0xFFF5F5F5)
    
    // Overlay (Glass): Light Gray with transparency
    val Overlay = Color(0xFFE0E0E0).copy(alpha = 0.1f)
    
    // Primary Accent: Dark Gray (for buttons/accents)
    val PrimaryAccent = Color(0xFF424242)
    
    // Text Primary: Black
    val TextPrimary = Color(0xFF000000)
    
    // Text Secondary: Medium Gray
    val TextSecondary = Color(0xFF757575)
}

private val DaylightLightColorScheme = lightColorScheme(
    primary = DaylightColors.PrimaryAccent,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0E0E0),
    onPrimaryContainer = DaylightColors.TextPrimary,
    
    secondary = DaylightColors.TextSecondary,
    onSecondary = Color.White,
    secondaryContainer = DaylightColors.Surface,
    onSecondaryContainer = DaylightColors.TextPrimary,
    
    tertiary = Color(0xFF616161),
    onTertiary = Color.White,
    
    error = Color(0xFF424242),
    onError = Color.White,
    errorContainer = Color(0xFFE0E0E0),
    onErrorContainer = Color(0xFF000000),
    
    background = Color.White,
    onBackground = DaylightColors.TextPrimary,
    
    surface = Color.White,
    onSurface = DaylightColors.TextPrimary,
    surfaceVariant = DaylightColors.Surface,
    onSurfaceVariant = DaylightColors.TextSecondary,
    
    outline = Color(0xFFBDBDBD),
    outlineVariant = Color(0xFFE0E0E0),
    
    scrim = Color.Black.copy(alpha = 0.32f),
    inverseSurface = DaylightColors.TextPrimary,
    inverseOnSurface = Color.White,
    inversePrimary = Color.White,
    
    surfaceDim = DaylightColors.Surface,
    surfaceBright = Color.White,
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color.White,
    surfaceContainer = Color.White,
    surfaceContainerHigh = Color.White,
    surfaceContainerHighest = Color.White,
)

@Composable
fun DaylightTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = DaylightLightColorScheme
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = DaylightTypography,
        content = content
    )
}

