package no.nordicsemi.android.dfu.app.home

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import no.nordicsemi.android.common.navigation.createSimpleDestination
import no.nordicsemi.android.common.navigation.defineDestination

val Home = createSimpleDestination("home")

val HomeDestination = defineDestination(Home) {
    HomeContent()
}

@Composable
private fun HomeContent(viewModel: HomeViewModel = hiltViewModel()) {
    HomeScreen(
        onNavigateToDFU = {
            viewModel.navigateToDFU()
        },
        onNavigateToKeyboardConfig = {
            viewModel.navigateToKeyboardConfig()
        },
        onNavigateToOnboarding = {
            viewModel.navigateToOnboarding()
        }
    )
}

// Destinations defined in their respective modules
val KeyboardConfig = createSimpleDestination("keyboard-config")
val Onboarding = createSimpleDestination("onboarding")

