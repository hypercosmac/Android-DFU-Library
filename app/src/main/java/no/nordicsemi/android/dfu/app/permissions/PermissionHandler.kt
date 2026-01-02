package no.nordicsemi.android.dfu.app.permissions

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat
import no.nordicsemi.android.dfu.app.bluetooth.BluetoothPermissions

/**
 * Result of a permission request
 */
sealed class PermissionResult {
    object Granted : PermissionResult()
    object Denied : PermissionResult()
    object PermanentlyDenied : PermissionResult()
}

/**
 * Handler for managing runtime permissions and Bluetooth state
 */
class PermissionHandler(
    private val activity: Activity
) {
    /**
     * Checks if all required permissions are granted
     */
    fun hasRequiredPermissions(): Boolean {
        return BluetoothPermissions.hasRequiredPermissions(activity)
    }

    /**
     * Checks if Bluetooth is enabled
     */
    fun isBluetoothEnabled(): Boolean {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        return bluetoothAdapter?.isEnabled == true
    }

    /**
     * Opens app settings page
     */
    fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", activity.packageName, null)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        activity.startActivity(intent)
    }

    /**
     * Opens Bluetooth settings page
     */
    fun openBluetoothSettings() {
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
        } else {
            Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        }
        activity.startActivity(intent)
    }

    /**
     * Gets the list of missing permissions
     */
    fun getMissingPermissions(): Array<String> {
        val required = BluetoothPermissions.getRequiredPermissions()
        return required.filter { permission ->
            ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()
    }

    /**
     * Checks if a permission was permanently denied (user selected "Don't ask again")
     */
    fun isPermissionPermanentlyDenied(permission: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return !activity.shouldShowRequestPermissionRationale(permission)
        }
        return false
    }
}

/**
 * Composable function to remember a PermissionHandler instance
 */
@Composable
fun rememberPermissionHandler(activity: Activity): PermissionHandler {
    return remember { PermissionHandler(activity) }
}

/**
 * Composable function to create a permission launcher that handles permission requests
 * and shows a dialog to open settings if permissions are permanently denied
 */
@Composable
fun rememberPermissionLauncher(
    activity: Activity,
    onPermissionResult: (PermissionResult) -> Unit,
    showSettingsDialog: (() -> Unit) -> Unit
): androidx.activity.result.ActivityResultLauncher<Array<String>> {
    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.all { it.value }
        
        if (allGranted) {
            onPermissionResult(PermissionResult.Granted)
        } else {
            // Check if any permission was permanently denied
            val permanentlyDenied = permissions.any { (permission, granted) ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    !granted && !activity.shouldShowRequestPermissionRationale(permission)
                } else {
                    !granted
                }
            }
            
            if (permanentlyDenied) {
                showSettingsDialog {
                    onPermissionResult(PermissionResult.PermanentlyDenied)
                }
            } else {
                onPermissionResult(PermissionResult.Denied)
            }
        }
    }
}

