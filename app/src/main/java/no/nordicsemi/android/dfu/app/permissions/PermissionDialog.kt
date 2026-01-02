package no.nordicsemi.android.dfu.app.permissions

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import no.nordicsemi.android.dfu.app.theme.DaylightTheme

/**
 * Dialog shown when permissions are required
 */
@Composable
fun PermissionRequiredDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit,
    onOpenSettings: () -> Unit
) {
    DaylightTheme {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center
                    )
                    
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancel")
                        }
                        
                        Button(
                            onClick = {
                                onOpenSettings()
                                onDismiss()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Open Settings")
                        }
                    }
                }
            }
        }
    }
}

/**
 * Dialog shown when Bluetooth is disabled
 */
@Composable
fun BluetoothDisabledDialog(
    onDismiss: () -> Unit,
    onOpenBluetoothSettings: () -> Unit
) {
    PermissionRequiredDialog(
        title = "Bluetooth Required",
        message = "Bluetooth must be enabled to scan for and connect to devices. Please enable Bluetooth in your device settings.",
        onDismiss = onDismiss,
        onOpenSettings = onOpenBluetoothSettings
    )
}

/**
 * Dialog shown when permissions are permanently denied
 */
@Composable
fun PermissionPermanentlyDeniedDialog(
    onDismiss: () -> Unit,
    onOpenSettings: () -> Unit
) {
    PermissionRequiredDialog(
        title = "Permissions Required",
        message = "This app needs Bluetooth permissions to function properly. Please grant the required permissions in app settings.",
        onDismiss = onDismiss,
        onOpenSettings = onOpenSettings
    )
}

