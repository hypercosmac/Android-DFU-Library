package no.nordicsemi.android.dfu.app.onboarding

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.onboardingDataStore: DataStore<Preferences> by preferencesDataStore(name = "onboarding")

private val ONBOARDING_COMPLETED_KEY = booleanPreferencesKey("onboarding_completed")
private val KEYBOARD_BACKLIGHT_KEY = booleanPreferencesKey("keyboard_backlight")
private val KEY_SOUND_KEY = booleanPreferencesKey("key_sound")
private val AUTO_SLEEP_KEY = booleanPreferencesKey("auto_sleep")

@Singleton
class OnboardingRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    val isOnboardingCompleted: Flow<Boolean> = context.onboardingDataStore.data.map { prefs ->
        prefs[ONBOARDING_COMPLETED_KEY] ?: false
    }
    
    val preferences: Flow<OnboardingPreferences> = context.onboardingDataStore.data.map { prefs ->
        OnboardingPreferences(
            keyboardBacklight = prefs[KEYBOARD_BACKLIGHT_KEY] ?: true,
            keySound = prefs[KEY_SOUND_KEY] ?: true,
            autoSleep = prefs[AUTO_SLEEP_KEY] ?: true
        )
    }
    
    suspend fun completeOnboarding() {
        context.onboardingDataStore.edit { prefs ->
            prefs[ONBOARDING_COMPLETED_KEY] = true
        }
    }
    
    suspend fun savePreferences(preferences: OnboardingPreferences) {
        context.onboardingDataStore.edit { prefs ->
            prefs[KEYBOARD_BACKLIGHT_KEY] = preferences.keyboardBacklight
            prefs[KEY_SOUND_KEY] = preferences.keySound
            prefs[AUTO_SLEEP_KEY] = preferences.autoSleep
        }
    }
}

