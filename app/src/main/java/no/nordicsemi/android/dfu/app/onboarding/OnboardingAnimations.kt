package no.nordicsemi.android.dfu.app.onboarding

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Animation specifications for luxury onboarding experience
 */
object OnboardingAnimations {
    // Transition durations
    const val TRANSITION_DURATION = 400
    const val FAST_TRANSITION_DURATION = 250
    const val SLOW_TRANSITION_DURATION = 600
    
    // Easing curves for smooth luxury feel
    val LuxuryEasing = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)
    val SmoothEasing = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f)
    val BounceEasing = CubicBezierEasing(0.68f, -0.55f, 0.265f, 1.55f)
    
    /**
     * Slide and fade transition for screen changes
     */
    fun slideTransitionSpec(): ContentTransform {
        return (slideInHorizontally(
            animationSpec = tween(TRANSITION_DURATION, easing = LuxuryEasing),
            initialOffsetX = { fullWidth -> fullWidth }
        ) + fadeIn(animationSpec = tween(TRANSITION_DURATION, easing = LuxuryEasing))) togetherWith
        (slideOutHorizontally(
            animationSpec = tween(TRANSITION_DURATION, easing = LuxuryEasing),
            targetOffsetX = { fullWidth -> -fullWidth }
        ) + fadeOut(animationSpec = tween(TRANSITION_DURATION, easing = LuxuryEasing)))
    }
    
    /**
     * Scale and fade transition for content appearance
     */
    fun scaleTransitionSpec(): ContentTransform {
        return (scaleIn(
            animationSpec = tween(TRANSITION_DURATION, easing = SmoothEasing),
            initialScale = 0.9f
        ) + fadeIn(animationSpec = tween(TRANSITION_DURATION, easing = SmoothEasing))) togetherWith
        (scaleOut(
            animationSpec = tween(TRANSITION_DURATION, easing = SmoothEasing),
            targetScale = 0.9f
        ) + fadeOut(animationSpec = tween(TRANSITION_DURATION, easing = SmoothEasing)))
    }
}

/**
 * Page indicator for onboarding progress
 */
@Composable
fun OnboardingPageIndicator(
    currentStep: OnboardingStep,
    modifier: Modifier = Modifier
) {
    val steps = listOf(
        OnboardingStep.Welcome,
        OnboardingStep.Intro,
        OnboardingStep.Pairing,
        OnboardingStep.Guidance,
        OnboardingStep.Preferences,
        OnboardingStep.Done
    )
    
    val currentIndex = steps.indexOf(currentStep).coerceAtLeast(0)
    
        Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            steps.forEachIndexed { index, _ ->
                val isActive = index == currentIndex
                val animatedProgress by animateFloatAsState(
                    targetValue = if (isActive) 1f else 0.3f,
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = OnboardingAnimations.SmoothEasing
                    ),
                    label = "indicator_alpha"
                )
                
                val animatedWidth by animateDpAsState(
                    targetValue = if (isActive) 32.dp else 8.dp,
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = OnboardingAnimations.SmoothEasing
                    ),
                    label = "indicator_width"
                )
                
                Surface(
                    modifier = Modifier
                        .width(animatedWidth)
                        .height(8.dp),
                    shape = RoundedCornerShape(4.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = animatedProgress),
                    tonalElevation = if (isActive) 2.dp else 0.dp
                ) {}
            }
        }
    }
}

