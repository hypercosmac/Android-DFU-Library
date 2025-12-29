package no.nordicsemi.android.dfu.app.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Daylight Color Palette
object DaylightColors {
    // Background (Primary): Warm Off-White
    val BackgroundPrimary = Color(0xFFF5F4F1)
    
    // Surface (Cards): Soft Gray
    val Surface = Color(0xFFE8E6E1)
    
    // Overlay (Glass): Frosted Gray (8-12% alpha)
    val Overlay = Color(0xFFDAD7D0).copy(alpha = 0.1f)
    
    // Primary Accent: Daylight Amber
    val PrimaryAccent = Color(0xFFF2A33A)
    
    // Text Primary: Charcoal
    val TextPrimary = Color(0xFF2B2B2B)
    
    // Text Secondary: Muted Gray
    val TextSecondary = Color(0xFF6F6F6F)
}

private val DaylightLightColorScheme = lightColorScheme(
    primary = DaylightColors.PrimaryAccent,
    onPrimary = Color.White,
    primaryContainer = DaylightColors.PrimaryAccent.copy(alpha = 0.1f),
    onPrimaryContainer = DaylightColors.TextPrimary,
    
    secondary = DaylightColors.TextSecondary,
    onSecondary = Color.White,
    secondaryContainer = DaylightColors.Surface,
    onSecondaryContainer = DaylightColors.TextPrimary,
    
    tertiary = DaylightColors.PrimaryAccent.copy(alpha = 0.8f),
    onTertiary = Color.White,
    
    error = Color(0xFFBA1A1A),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    
    background = DaylightColors.BackgroundPrimary,
    onBackground = DaylightColors.TextPrimary,
    
    surface = DaylightColors.Surface,
    onSurface = DaylightColors.TextPrimary,
    surfaceVariant = DaylightColors.Surface.copy(alpha = 0.5f),
    onSurfaceVariant = DaylightColors.TextSecondary,
    
    outline = DaylightColors.TextSecondary.copy(alpha = 0.3f),
    outlineVariant = DaylightColors.TextSecondary.copy(alpha = 0.1f),
    
    scrim = Color.Black.copy(alpha = 0.32f),
    inverseSurface = DaylightColors.TextPrimary,
    inverseOnSurface = DaylightColors.BackgroundPrimary,
    inversePrimary = DaylightColors.PrimaryAccent,
    
    surfaceDim = DaylightColors.Surface,
    surfaceBright = DaylightColors.BackgroundPrimary,
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = DaylightColors.BackgroundPrimary,
    surfaceContainer = DaylightColors.Surface,
    surfaceContainerHigh = DaylightColors.Surface.copy(alpha = 0.8f),
    surfaceContainerHighest = DaylightColors.Surface.copy(alpha = 0.6f),
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

