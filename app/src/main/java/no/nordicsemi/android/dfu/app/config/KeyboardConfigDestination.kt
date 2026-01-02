package no.nordicsemi.android.dfu.app.config

import androidx.compose.runtime.Composable
import no.nordicsemi.android.common.navigation.defineDestination
import no.nordicsemi.android.dfu.app.home.KeyboardConfig

val KeyboardConfigDestination = defineDestination(KeyboardConfig) {
    KeyboardConfigScreen()
}

