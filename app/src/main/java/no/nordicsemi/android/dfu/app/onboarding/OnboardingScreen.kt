package no.nordicsemi.android.dfu.app.onboarding

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import no.nordicsemi.android.dfu.app.bluetooth.BluetoothPermissions
import no.nordicsemi.android.dfu.app.onboarding.screens.*
import no.nordicsemi.android.dfu.app.permissions.*
import no.nordicsemi.android.dfu.app.onboarding.OnboardingAnimations
import no.nordicsemi.android.dfu.app.onboarding.OnboardingPageIndicator
import no.nordicsemi.android.dfu.app.onboarding.rememberOnboardingAudioManager

@Composable
fun OnboardingScreen(
    onComplete: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val activity = context as? Activity
    val audioManager = rememberOnboardingAudioManager()
    
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
    
    Box(modifier = Modifier.fillMaxSize()) {
        // Page indicator overlay
        OnboardingPageIndicator(
            currentStep = uiState.currentStep,
            modifier = Modifier.fillMaxSize()
        )
        
        // Animated content with luxury transitions
        AnimatedContent(
            targetState = uiState.currentStep,
            transitionSpec = {
                OnboardingAnimations.slideTransitionSpec()
            },
            modifier = Modifier.fillMaxSize(),
            label = "onboarding_transition"
        ) { step ->
        when (step) {
            is OnboardingStep.Welcome -> {
                WelcomeScreen(
                    onContinue = {
                        audioManager.playTransitionSound()
                        viewModel.navigateToStep(OnboardingStep.Intro)
                    }
                )
            }
            is OnboardingStep.Intro -> {
                IntroScreen(
                    onContinue = {
                        audioManager.playTransitionSound()
                        viewModel.navigateToStep(OnboardingStep.Pairing)
                    }
                )
            }
            is OnboardingStep.Pairing -> {
                PairingScreen(
                    isScanning = uiState.isScanning,
                    foundDevice = uiState.foundDevice?.name,
                    allDevices = uiState.allDiscoveredDevices,
                    isConnected = uiState.isConnected,
                    connectedDeviceAddress = uiState.connectedDeviceAddress,
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
                    onPairClick = { deviceAddress ->
                        viewModel.connectToDevice(deviceAddress)
                    },
                    onContinue = {
                        audioManager.playTransitionSound()
                        viewModel.navigateToStep(OnboardingStep.Guidance)
                    }
                )
            }
            is OnboardingStep.Guidance -> {
                GuidanceScreen(
                    onContinue = {
                        audioManager.playTransitionSound()
                        viewModel.navigateToStep(OnboardingStep.Preferences)
                    }
                )
            }
            is OnboardingStep.Preferences -> {
                PreferencesScreen(
                    preferences = uiState.preferences,
                    onPreferenceChanged = { prefs ->
                        audioManager.playClickSound()
                        viewModel.updatePreferences(prefs)
                    },
                    onContinue = {
                        audioManager.playTransitionSound()
                        viewModel.navigateToStep(OnboardingStep.Done)
                    }
                )
            }
            is OnboardingStep.Done -> {
                DoneScreen(
                    onBegin = {
                        audioManager.playSuccessSound()
                        viewModel.completeOnboarding()
                        onComplete()
                    }
                )
            }
        }
    }
    }
}

