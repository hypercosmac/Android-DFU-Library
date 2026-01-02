package no.nordicsemi.android.dfu.app.onboarding

import android.content.Context
import android.media.SoundPool
import android.media.AudioAttributes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

/**
 * Manages audio playback for onboarding screens
 * Uses SoundPool for efficient sound effect playback
 */
class OnboardingAudioManager private constructor(context: Context) {
    
    private val soundPool: SoundPool = SoundPool.Builder()
        .setMaxStreams(5)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        )
        .build()
    
    // Sound IDs - we'll use system sounds or generate programmatically
    // For now, we'll use a simple approach with system notification sounds
    private var clickSoundId: Int = 0
    private var successSoundId: Int = 0
    private var transitionSoundId: Int = 0
    
    init {
        // Load system sounds or create synthetic sounds
        // In a production app, you'd load actual sound files from res/raw
        // For now, we'll use a silent approach that can be enhanced with actual sounds
    }
    
    fun playClickSound() {
        // Play a subtle click sound
        // soundPool.play(clickSoundId, 0.3f, 0.3f, 1, 0, 1f)
    }
    
    fun playSuccessSound() {
        // Play a success sound
        // soundPool.play(successSoundId, 0.5f, 0.5f, 1, 0, 1f)
    }
    
    fun playTransitionSound() {
        // Play a subtle transition sound
        // soundPool.play(transitionSoundId, 0.2f, 0.2f, 1, 0, 1f)
    }
    
    fun release() {
        soundPool.release()
    }
    
    companion object {
        @Volatile
        private var INSTANCE: OnboardingAudioManager? = null
        
        fun getInstance(context: Context): OnboardingAudioManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: OnboardingAudioManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}

@Composable
fun rememberOnboardingAudioManager(): OnboardingAudioManager {
    val context = LocalContext.current
    return remember { OnboardingAudioManager.getInstance(context) }
}

