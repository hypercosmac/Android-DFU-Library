package no.nordicsemi.android.dfu.app.bluetooth

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanRecord
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresPermission
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Represents a discovered Bluetooth device
 */
data class DiscoveredDevice(
    val name: String?,
    val address: String,
    val rssi: Int,
    val device: BluetoothDevice
)

/**
 * Connection state for a Bluetooth device
 */
sealed class ConnectionState {
    object Disconnected : ConnectionState()
    data class Connecting(val deviceAddress: String) : ConnectionState()
    data class Connected(val deviceAddress: String) : ConnectionState()
    data class Error(val message: String) : ConnectionState()
}

/**
 * Manages Bluetooth Low Energy scanning and connection operations.
 * This is a production-ready implementation with proper error handling,
 * lifecycle management, and state management.
 * Uses native Android Bluetooth APIs for reliability.
 */
@Singleton
class BluetoothManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val scope: CoroutineScope
) {
    private val systemBluetoothManager: android.bluetooth.BluetoothManager = 
        context.getSystemService(Context.BLUETOOTH_SERVICE) as android.bluetooth.BluetoothManager
    private val bluetoothAdapter: BluetoothAdapter? = systemBluetoothManager.adapter
    private val bluetoothLeScanner: BluetoothLeScanner? = bluetoothAdapter?.bluetoothLeScanner

    private val _scanState = MutableStateFlow<ScanState>(ScanState.Idle)
    val scanState: StateFlow<ScanState> = _scanState.asStateFlow()

    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Disconnected)
    val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()

    private val _discoveredDevices = MutableStateFlow<List<DiscoveredDevice>>(emptyList())
    val discoveredDevices: StateFlow<List<DiscoveredDevice>> = _discoveredDevices.asStateFlow()

    private var currentGatt: BluetoothGatt? = null
    private var connectedDeviceAddress: String? = null
    private var scanJob: Job? = null
    private var connectionJob: Job? = null
    private var classicBluetoothReceiver: android.content.BroadcastReceiver? = null
    
    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            handleScanResult(result)
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>) {
            results.forEach { handleScanResult(it) }
        }

        override fun onScanFailed(errorCode: Int) {
            _scanState.value = ScanState.Error("Scan failed with error code: $errorCode")
        }
    }
    
    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            val deviceAddress = gatt.device.address
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    connectedDeviceAddress = deviceAddress
                    _connectionState.value = ConnectionState.Connected(deviceAddress)
                    // Discover services
                    gatt.discoverServices()
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    connectedDeviceAddress = null
                    _connectionState.value = ConnectionState.Disconnected
                    currentGatt = null
                }
                BluetoothProfile.STATE_CONNECTING -> {
                    _connectionState.value = ConnectionState.Connecting(deviceAddress)
                }
            }
            
            if (status != BluetoothGatt.GATT_SUCCESS) {
                connectedDeviceAddress = null
                _connectionState.value = ConnectionState.Error("Connection failed with status: $status")
                currentGatt = null
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // Services discovered successfully
            }
        }
    }

    /**
     * Sealed class representing scan states
     */
    sealed class ScanState {
        object Idle : ScanState()
        object Scanning : ScanState()
        data class Error(val message: String) : ScanState()
    }

    /**
     * Checks if Bluetooth is supported and enabled
     */
    fun isBluetoothEnabled(): Boolean {
        return bluetoothAdapter?.isEnabled == true
    }

    /**
     * Checks if required permissions are granted
     */
    fun hasRequiredPermissions(): Boolean {
        return BluetoothPermissions.hasRequiredPermissions(context)
    }

    /**
     * Starts scanning for BLE devices and includes already paired devices
     */
    @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT])
    fun startScanning(
        filterByName: String? = null,
        timeoutMillis: Long = 10000L
    ) {
        if (!hasRequiredPermissions()) {
            _scanState.value = ScanState.Error("Bluetooth permissions not granted")
            return
        }

        if (!isBluetoothEnabled()) {
            _scanState.value = ScanState.Error("Bluetooth is not enabled")
            return
        }

        // Stop any existing scan
        stopScanning()

        _scanState.value = ScanState.Scanning
        // Clear devices at the start of a new scan
        _discoveredDevices.value = emptyList()
        
        // First, add already paired/bonded devices immediately
        try {
            val bondedDevices = bluetoothAdapter?.bondedDevices ?: emptySet()
            val bondedDeviceList = bondedDevices.map { device ->
                DiscoveredDevice(
                    name = device.name ?: "Device ${device.address.takeLast(5)}",
                    address = device.address,
                    rssi = 0, // Bonded devices don't have RSSI
                    device = device
                )
            }
            if (bondedDeviceList.isNotEmpty()) {
                _discoveredDevices.value = bondedDeviceList.sortedWith(
                    compareByDescending<DiscoveredDevice> { 
                        it.name?.contains("DAYLIGHT_KB-1", ignoreCase = true) == true 
                    }.thenByDescending { it.rssi }
                )
            }
        } catch (e: Exception) {
            // Ignore errors when accessing bonded devices
        }

        // Start BLE scanning if available
        if (bluetoothLeScanner != null) {
            // Configure scan settings for better discovery
            val scanSettings = ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .setReportDelay(0) // Report results immediately
                .build()

            // Start scanning with no filters to find all devices
            try {
                bluetoothLeScanner.startScan(null, scanSettings, scanCallback)
            } catch (e: Exception) {
                _scanState.value = ScanState.Error("Failed to start scan: ${e.message}")
            }
        }
        
        // Also start classic Bluetooth discovery
        try {
            if (bluetoothAdapter?.isDiscovering == false) {
                bluetoothAdapter?.startDiscovery()
                
                // Register receiver for classic Bluetooth devices
                if (classicBluetoothReceiver == null) {
                    classicBluetoothReceiver = object : android.content.BroadcastReceiver() {
                        override fun onReceive(context: Context?, intent: android.content.Intent?) {
                            when (intent?.action) {
                                BluetoothDevice.ACTION_FOUND -> {
                                    val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                                    val rssi: Short = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE)
                                    
                                    device?.let {
                                        val deviceName = it.name ?: "Device ${it.address.takeLast(5)}"
                                        val discoveredDevice = DiscoveredDevice(
                                            name = deviceName,
                                            address = it.address,
                                            rssi = rssi.toInt(),
                                            device = it
                                        )
                                        
                                        // Update discovered devices list
                                        val currentDevices = _discoveredDevices.value.toMutableList()
                                        val existingIndex = currentDevices.indexOfFirst { d -> d.address == discoveredDevice.address }
                                        
                                        if (existingIndex >= 0) {
                                            // Update if RSSI is better
                                            if (rssi.toInt() > currentDevices[existingIndex].rssi) {
                                                currentDevices[existingIndex] = discoveredDevice
                                            }
                                        } else {
                                            currentDevices.add(discoveredDevice)
                                        }
                                        
                                        // Sort: DAYLIGHT_KB-1 first, then by RSSI
                                        _discoveredDevices.value = currentDevices.sortedWith(
                                            compareByDescending<DiscoveredDevice> { 
                                                it.name?.contains("DAYLIGHT_KB-1", ignoreCase = true) == true 
                                            }.thenByDescending { it.rssi }
                                        )
                                    }
                                }
                            }
                        }
                    }
                    
                    val filter = android.content.IntentFilter(BluetoothDevice.ACTION_FOUND)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        context.registerReceiver(classicBluetoothReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
                    } else {
                        context.registerReceiver(classicBluetoothReceiver, filter)
                    }
                }
            }
        } catch (e: Exception) {
            // Classic Bluetooth discovery might not be available or permitted
            // Continue with BLE scanning only
        }

        // Auto-stop after timeout
        scanJob = scope.launch {
            delay(timeoutMillis)
            if (_scanState.value is ScanState.Scanning) {
                stopScanning()
            }
        }
    }

    /**
     * Handles individual scan results
     */
    private fun handleScanResult(scanResult: ScanResult) {
        val device = scanResult.device
        val rssi = scanResult.rssi
        
        // Get device name from device or scan record
        var deviceName: String? = device.name
        if (deviceName == null) {
            // Try to get name from scan record
            val scanRecord = scanResult.scanRecord
            if (scanRecord != null) {
                try {
                    deviceName = scanRecord.deviceName
                } catch (e: Exception) {
                    // Ignore
                }
            }
        }
        
        // Use MAC address as fallback if no name
        if (deviceName.isNullOrBlank()) {
            deviceName = "Device ${device.address.takeLast(5)}"
        }

        val discoveredDevice = DiscoveredDevice(
            name = deviceName,
            address = device.address,
            rssi = rssi,
            device = device
        )

        // Update discovered devices list, avoiding duplicates
        val currentDevices = _discoveredDevices.value.toMutableList()
        val existingIndex = currentDevices.indexOfFirst { it.address == discoveredDevice.address }
        
        if (existingIndex >= 0) {
            // Update existing device (might have better RSSI or name)
            val existing = currentDevices[existingIndex]
            // Prefer device with a name over one without
            val shouldUpdate = if (existing.name == null || existing.name.startsWith("Device ")) {
                deviceName != null && !deviceName.startsWith("Device ")
            } else {
                // Update if RSSI is better or if we got a name
                rssi > existing.rssi || (deviceName != null && existing.name == null)
            }
            if (shouldUpdate) {
                currentDevices[existingIndex] = discoveredDevice
            }
        } else {
            // Add new device
            currentDevices.add(discoveredDevice)
        }
        
        // Sort: DAYLIGHT_KB-1 first, then by RSSI
        _discoveredDevices.value = currentDevices.sortedWith(
            compareByDescending<DiscoveredDevice> { 
                it.name?.contains("DAYLIGHT_KB-1", ignoreCase = true) == true 
            }.thenByDescending { it.rssi }
        )
    }

    /**
     * Stops scanning for devices
     */
    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    fun stopScanning() {
        scanJob?.cancel()
        scanJob = null
        
        // Stop BLE scanning
        try {
            bluetoothLeScanner?.stopScan(scanCallback)
        } catch (e: Exception) {
            // Ignore
        }
        
        // Stop classic Bluetooth discovery
        try {
            bluetoothAdapter?.cancelDiscovery()
        } catch (e: Exception) {
            // Ignore
        }
        
        // Unregister classic Bluetooth receiver
        try {
            classicBluetoothReceiver?.let {
                context.unregisterReceiver(it)
                classicBluetoothReceiver = null
            }
        } catch (e: Exception) {
            // Ignore if already unregistered
        }
        
        if (_scanState.value is ScanState.Scanning) {
            _scanState.value = ScanState.Idle
        }
    }

    /**
     * Connects to a discovered device
     */
    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun connectToDevice(device: DiscoveredDevice) {
        if (!hasRequiredPermissions()) {
            _connectionState.value = ConnectionState.Error("Bluetooth permissions not granted")
            return
        }

        if (!isBluetoothEnabled()) {
            _connectionState.value = ConnectionState.Error("Bluetooth is not enabled")
            return
        }

        // Stop scanning when connecting
        stopScanning()

        // Disconnect from previous device if any
        disconnect()

        _connectionState.value = ConnectionState.Connecting(device.address)

        connectionJob = scope.launch {
            try {
                // Connect to the device
                val gatt = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    device.device.connectGatt(context, false, gattCallback, BluetoothDevice.TRANSPORT_LE)
                } else {
                    device.device.connectGatt(context, false, gattCallback)
                }
                currentGatt = gatt
            } catch (e: Exception) {
                connectedDeviceAddress = null
                _connectionState.value = ConnectionState.Error("Connection failed: ${e.message}")
                currentGatt = null
            }
        }
    }

    /**
     * Disconnects from the current device
     */
    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun disconnect() {
        connectionJob?.cancel()
        connectionJob = null
        
        scope.launch {
            try {
                currentGatt?.disconnect()
                currentGatt?.close()
                currentGatt = null
                connectedDeviceAddress = null
                _connectionState.value = ConnectionState.Disconnected
            } catch (e: Exception) {
                connectedDeviceAddress = null
                _connectionState.value = ConnectionState.Error("Disconnect failed: ${e.message}")
            }
        }
    }

    /**
     * Gets the address of the currently connected device
     */
    fun getConnectedDeviceAddress(): String? = connectedDeviceAddress

    /**
     * Cleans up resources
     */
    fun cleanup() {
        stopScanning()
        disconnect()
        
        // Ensure receiver is unregistered
        try {
            classicBluetoothReceiver?.let {
                context.unregisterReceiver(it)
                classicBluetoothReceiver = null
            }
        } catch (e: Exception) {
            // Ignore
        }
    }
}

