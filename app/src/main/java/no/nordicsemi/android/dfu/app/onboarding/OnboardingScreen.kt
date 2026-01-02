package no.nordicsemi.android.dfu.app.onboarding

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import no.nordicsemi.android.dfu.app.bluetooth.BluetoothPermissions
import no.nordicsemi.android.dfu.app.onboarding.screens.*
import no.nordicsemi.android.dfu.app.permissions.*

@Composable
fun OnboardingScreen(
    onComplete: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val activity = context as? Activity
    
    // Permission handling
    var showPermissionDialog by remember { mutableStateOf(false) }
    var showBluetoothDialog by remember { mutableStateOf(false) }
    
    val permissionHandler = remember {
        activity?.let { PermissionHandler(it) }
    }
    
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.all { it.value }
        if (!allGranted && activity != null) {
            // Check if permanently denied
            val permanentlyDenied = permissions.any { (permission, granted) ->
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    !granted && !activity.shouldShowRequestPermissionRationale(permission)
                } else {
                    !granted
                }
            }
            if (permanentlyDenied) {
                showPermissionDialog = true
            }
        }
    }
    
    // Check permissions when pairing screen appears
    LaunchedEffect(uiState.currentStep) {
        if (uiState.currentStep is OnboardingStep.Pairing && activity != null) {
            val handler = PermissionHandler(activity)
            if (!handler.hasRequiredPermissions()) {
                val missingPermissions = handler.getMissingPermissions()
                if (missingPermissions.isNotEmpty()) {
                    permissionLauncher.launch(missingPermissions)
                }
            } else if (!handler.isBluetoothEnabled()) {
                showBluetoothDialog = true
            } else if (!uiState.isScanning && !uiState.isConnected) {
                viewModel.startScanning()
            }
        }
    }
    
    // Permission dialogs
    if (showPermissionDialog && activity != null) {
        PermissionPermanentlyDeniedDialog(
            onDismiss = { showPermissionDialog = false },
            onOpenSettings = {
                PermissionHandler(activity).openAppSettings()
            }
        )
    }
    
    if (showBluetoothDialog && activity != null) {
        BluetoothDisabledDialog(
            onDismiss = { showBluetoothDialog = false },
            onOpenBluetoothSettings = {
                PermissionHandler(activity).openBluetoothSettings()
            }
        )
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
                        if (activity != null) {
                            val handler = PermissionHandler(activity)
                            if (!handler.hasRequiredPermissions()) {
                                val missingPermissions = handler.getMissingPermissions()
                                if (missingPermissions.isNotEmpty()) {
                                    permissionLauncher.launch(missingPermissions)
                                }
                            } else if (!handler.isBluetoothEnabled()) {
                                showBluetoothDialog = true
                            } else {
                                viewModel.startScanning()
                            }
                        }
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
}

