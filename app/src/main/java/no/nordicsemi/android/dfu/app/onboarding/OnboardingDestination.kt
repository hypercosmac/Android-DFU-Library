package no.nordicsemi.android.dfu.app.onboarding

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import no.nordicsemi.android.common.navigation.defineDestination
import no.nordicsemi.android.dfu.app.home.Onboarding as OnboardingDest

val OnboardingDestination = defineDestination(OnboardingDest) {
    val navigationViewModel: OnboardingNavigationViewModel = hiltViewModel()
    
    OnboardingScreen(
        onComplete = {
            navigationViewModel.navigateUp()
        }
    )
}

