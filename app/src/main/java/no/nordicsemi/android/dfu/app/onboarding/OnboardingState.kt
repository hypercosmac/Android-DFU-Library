package no.nordicsemi.android.dfu.app.onboarding

sealed class OnboardingStep {
    object Welcome : OnboardingStep()
    object Intro : OnboardingStep()
    object Pairing : OnboardingStep()
    object Guidance : OnboardingStep()
    object Preferences : OnboardingStep()
    object Done : OnboardingStep()
}

data class OnboardingPreferences(
    val keyboardBacklight: Boolean = true,
    val keySound: Boolean = true,
    val autoSleep: Boolean = true
)

