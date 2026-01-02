package no.nordicsemi.android.dfu.app.onboarding

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay
import no.nordicsemi.android.dfu.app.onboarding.screens.*

@Composable
fun OnboardingScreen(
    onComplete: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    fun hasScanPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            // On pre-Android 12, BLUETOOTH_SCAN does not exist as a runtime permission.
            true
        }
    }

    val scanPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions[Manifest.permission.BLUETOOTH_SCAN] == true
        } else {
            true
        }

        if (granted &&
            uiState.currentStep is OnboardingStep.Pairing &&
            !uiState.isScanning &&
            !uiState.isConnected
        ) {
            viewModel.startScanning()
        }
    }
    
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
                        viewModel.navigateToStep(OnboardingStep.Pairing)
                    }
                )
            }
            is OnboardingStep.Pairing -> {
                PairingScreen(
                    isScanning = uiState.isScanning,
                    foundDevice = uiState.foundDevice?.name,
                    isConnected = uiState.isConnected,
                    onStartScan = {
                        if (hasScanPermission()) {
                            viewModel.startScanning()
                        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            scanPermissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.BLUETOOTH_SCAN,
                                    Manifest.permission.BLUETOOTH_CONNECT
                                )
                            )
                        }
                    },
                    onContinue = {
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
            if (hasScanPermission()) {
                viewModel.startScanning()
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                scanPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_CONNECT
                    )
                )
            }
        }
    }
    
    // Auto-connect when device found
    LaunchedEffect(uiState.foundDevice) {
        if (uiState.foundDevice != null && !uiState.isConnected && uiState.currentStep is OnboardingStep.Pairing) {
            viewModel.connectToDevice()
        }
    }
    
}

