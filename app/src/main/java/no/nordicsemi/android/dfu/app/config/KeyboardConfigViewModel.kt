package no.nordicsemi.android.dfu.app.config

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import no.nordicsemi.android.common.navigation.Navigator
import javax.inject.Inject

@HiltViewModel
internal class KeyboardConfigViewModel @Inject constructor(
    private val navigator: Navigator
) : ViewModel() {

    fun navigateUp() {
        viewModelScope.launch {
            navigator.navigateUp()
        }
    }
}

