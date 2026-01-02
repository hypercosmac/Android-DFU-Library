package no.nordicsemi.android.dfu.app.onboarding

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OnboardingUiState(
    val currentStep: OnboardingStep = OnboardingStep.Welcome,
    val isScanning: Boolean = false,
    val foundDevice: BluetoothDevice? = null,
    val isConnected: Boolean = false
)

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: OnboardingRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()
    
    private var bluetoothAdapter: BluetoothAdapter? = null
    private var bluetoothLeScanner: BluetoothLeScanner? = null
    private var scanCallback: ScanCallback? = null
    
    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            bluetoothAdapter = bluetoothManager.adapter
            bluetoothLeScanner = bluetoothAdapter?.bluetoothLeScanner
            scanCallback = createScanCallback()
        }
    }
    
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun createScanCallback(): ScanCallback {
        return object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                val device = result.device
                // Accept all devices regardless of name/manufacturer
                stopScanning()
                _uiState.value = _uiState.value.copy(
                    foundDevice = device,
                    isScanning = false
                )
            }
            
            override fun onBatchScanResults(results: MutableList<ScanResult>) {
                // Accept the first device found
                if (results.isNotEmpty()) {
                    val device = results[0].device
                    stopScanning()
                    _uiState.value = _uiState.value.copy(
                        foundDevice = device,
                        isScanning = false
                    )
                }
            }
            
            override fun onScanFailed(errorCode: Int) {
                stopScanning()
            }
        }
    }
    
    fun navigateToStep(step: OnboardingStep) {
        _uiState.value = _uiState.value.copy(currentStep = step)
    }
    
    fun startScanning() {
        if (bluetoothLeScanner == null || bluetoothAdapter?.isEnabled != true) {
            return
        }
        
        _uiState.value = _uiState.value.copy(isScanning = true, foundDevice = null)
        
        val settings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()
        
        val callback = scanCallback ?: return
        // Scan for all devices without filters
        bluetoothLeScanner?.startScan(null, settings, callback)
    }
    
    fun stopScanning() {
        scanCallback?.let { callback ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                bluetoothLeScanner?.stopScan(callback)
            }
        }
        _uiState.value = _uiState.value.copy(isScanning = false)
    }
    
    fun connectToDevice() {
        // For now, just mark as connected
        // Actual connection logic would go here
        _uiState.value = _uiState.value.copy(isConnected = true)
    }
    
    fun completeOnboarding() {
        viewModelScope.launch {
            repository.completeOnboarding()
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        stopScanning()
    }
}

