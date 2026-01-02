package no.nordicsemi.android.dfu.app.onboarding

sealed class OnboardingStep {
    object Welcome : OnboardingStep()
    object Pairing : OnboardingStep()
}

data class OnboardingPreferences(
    val keyboardBacklight: Boolean = true,
    val keySound: Boolean = true,
    val autoSleep: Boolean = true
)

