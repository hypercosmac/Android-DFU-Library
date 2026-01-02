package no.nordicsemi.android.dfu.app.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import no.nordicsemi.android.common.navigation.Navigator
import no.nordicsemi.android.dfu.profile.main.DfuMain
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val navigator: Navigator
) : ViewModel() {

    fun navigateToDFU() {
        viewModelScope.launch {
            navigator.navigateTo(DfuMain)
        }
    }

    fun navigateToKeyboardConfig() {
        viewModelScope.launch {
            navigator.navigateTo(KeyboardConfig)
        }
    }

    fun navigateToOnboarding() {
        viewModelScope.launch {
            navigator.navigateTo(Onboarding)
        }
    }
}

