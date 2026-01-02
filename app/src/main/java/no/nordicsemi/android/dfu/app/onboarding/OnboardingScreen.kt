package no.nordicsemi.android.dfu.app.onboarding

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import no.nordicsemi.android.dfu.app.onboarding.screens.*

@Composable
fun OnboardingScreen(
    onComplete: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    AnimatedContent(
        targetState = uiState.currentStep,
        transitionSpec = {
            fadeIn(animationSpec = tween(250)) togetherWith
            fadeOut(animationSpec = tween(200))
        },
        modifier = Modifier.fillMaxSize(),
        label = "onboarding_transition"
    ) { step ->
        when (step) {
            is OnboardingStep.Welcome -> {
                WelcomeScreen(
                    onContinue = {
                        viewModel.navigateToStep(OnboardingStep.Intro)
                    }
                )
            }
            is OnboardingStep.Intro -> {
                IntroScreen(
                    onContinue = {
                        viewModel.navigateToStep(OnboardingStep.Pairing)
                    }
                )
            }
            is OnboardingStep.Pairing -> {
                PairingScreen(
                    isScanning = uiState.isScanning,
                    foundDevice = uiState.foundDevice?.name,
                    isConnected = uiState.isConnected,
                    connectionError = uiState.connectionError,
                    onStartScan = {
                        viewModel.startScanning()
                    },
                    onPairClick = {
                        viewModel.connectToDevice()
                    },
                    onContinue = {
                        viewModel.navigateToStep(OnboardingStep.Guidance)
                    }
                )
            }
            is OnboardingStep.Guidance -> {
                GuidanceScreen(
                    onContinue = {
                        viewModel.navigateToStep(OnboardingStep.Preferences)
                    }
                )
            }
            is OnboardingStep.Preferences -> {
                PreferencesScreen(
                    preferences = uiState.preferences,
                    onPreferenceChanged = { prefs ->
                        viewModel.updatePreferences(prefs)
                    },
                    onContinue = {
                        viewModel.navigateToStep(OnboardingStep.Done)
                    }
                )
            }
            is OnboardingStep.Done -> {
                DoneScreen(
                    onBegin = {
                        viewModel.completeOnboarding()
                        onComplete()
                    }
                )
            }
        }
    }
    
    // Auto-start scanning when pairing screen appears
    LaunchedEffect(uiState.currentStep) {
        if (uiState.currentStep is OnboardingStep.Pairing && !uiState.isScanning && !uiState.isConnected) {
            viewModel.startScanning()
        }
    }
}

