package no.nordicsemi.android.dfu.app.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import no.nordicsemi.android.dfu.app.bluetooth.BluetoothManager
import no.nordicsemi.android.dfu.app.bluetooth.BluetoothPermissions
import no.nordicsemi.android.dfu.app.bluetooth.ConnectionState
import no.nordicsemi.android.dfu.app.bluetooth.DiscoveredDevice
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import no.nordicsemi.android.dfu.app.onboarding.OnboardingRepository
import javax.inject.Inject

data class OnboardingUiState(
    val currentStep: OnboardingStep = OnboardingStep.Welcome,
    val isScanning: Boolean = false,
    val foundDevice: BluetoothDevice? = null,
    val isConnected: Boolean = false,
    val connectionError: String? = null,
    val preferences: OnboardingPreferences = OnboardingPreferences()
)

data class BluetoothDevice(
    val name: String?,
    val address: String
)

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val onboardingRepository: OnboardingRepository,
    private val bluetoothManager: BluetoothManager,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    init {
        // Observe Bluetooth manager states
        observeBluetoothStates()
    }

    /**
     * Observes Bluetooth manager states and updates UI state accordingly
     */
    private fun observeBluetoothStates() {
        // Combine scan state and discovered devices
        combine(
            bluetoothManager.scanState,
            bluetoothManager.discoveredDevices,
            bluetoothManager.connectionState
        ) { scanState, devices, connectionState ->
            val isScanning = scanState is BluetoothManager.ScanState.Scanning
            val foundDevice = devices.firstOrNull()?.let { device ->
                BluetoothDevice(name = device.name, address = device.address)
            }
            val isConnected = connectionState is ConnectionState.Connected
            val connectionError = when (connectionState) {
                is ConnectionState.Error -> connectionState.message
                else -> null
            }

            _uiState.value = _uiState.value.copy(
                isScanning = isScanning,
                foundDevice = foundDevice,
                isConnected = isConnected,
                connectionError = connectionError
            )
        }
            .onEach { }
            .launchIn(viewModelScope)
    }

    fun navigateToStep(step: OnboardingStep) {
        _uiState.value = _uiState.value.copy(currentStep = step)
    }

    /**
     * Starts scanning for Bluetooth devices
     * Filters for keyboard-related devices
     */
    fun startScanning() {
        if (!bluetoothManager.hasRequiredPermissions()) {
            val errorMessage = BluetoothPermissions.getPermissionErrorMessage(context)
            _uiState.value = _uiState.value.copy(
                connectionError = errorMessage.ifEmpty { "Bluetooth permissions are required." }
            )
            return
        }

        if (!bluetoothManager.isBluetoothEnabled()) {
            _uiState.value = _uiState.value.copy(
                connectionError = "Bluetooth is not enabled. Please enable Bluetooth in settings."
            )
            return
        }

        // Clear previous errors
        _uiState.value = _uiState.value.copy(connectionError = null)
        
        // Start scanning with filter for keyboard devices
        bluetoothManager.startScanning(
            filterByName = "keyboard", // Filter for keyboard-related devices (can be "DAYLIGHT", "keyboard", etc.)
            timeoutMillis = 15000L // 15 second timeout
        )
    }

    /**
     * Stops scanning for devices
     */
    fun stopScanning() {
        bluetoothManager.stopScanning()
    }

    /**
     * Connects to the currently found device
     */
    fun connectToDevice() {
        val devices = bluetoothManager.discoveredDevices.value
        val device = devices.firstOrNull()
        
        if (device == null) {
            _uiState.value = _uiState.value.copy(
                connectionError = "No device available to connect"
            )
            return
        }

        // Clear previous errors
        _uiState.value = _uiState.value.copy(connectionError = null)
        
        bluetoothManager.connectToDevice(device)
    }

    /**
     * Disconnects from the current device
     */
    fun disconnect() {
        bluetoothManager.disconnect()
    }

    fun updatePreferences(preferences: OnboardingPreferences) {
        _uiState.value = _uiState.value.copy(preferences = preferences)
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            onboardingRepository.completeOnboarding()
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Cleanup Bluetooth resources when ViewModel is cleared
        bluetoothManager.cleanup()
    }
}
