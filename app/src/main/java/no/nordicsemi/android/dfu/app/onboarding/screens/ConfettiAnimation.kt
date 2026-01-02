package no.nordicsemi.android.dfu.app.onboarding.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

data class ConfettiParticle(
    val x: Float,
    val y: Float,
    val color: Color,
    val size: Float,
    val velocityX: Float,
    val velocityY: Float,
    val rotation: Float,
    val rotationSpeed: Float
)

@Composable
fun ConfettiAnimation(
    visible: Boolean,
    modifier: Modifier = Modifier,
    durationMillis: Int = 2000
) {
    val particles = remember { mutableStateOf<List<ConfettiParticle>>(emptyList()) }
    var animationProgress by remember { mutableStateOf(0f) }
    var canvasSize by remember { mutableStateOf(Size(1000f, 1000f)) }
    val density = LocalDensity.current
    
    LaunchedEffect(visible, canvasSize) {
        if (visible && particles.value.isEmpty() && canvasSize.width > 0 && canvasSize.height > 0) {
            // Generate particles when visible and we know the size
            val newParticles = List(50) {
                ConfettiParticle(
                    x = Random.nextFloat() * canvasSize.width,
                    y = -50f,
                    color = when (Random.nextInt(5)) {
                        0 -> Color(0xFFFF6B6B) // Red
                        1 -> Color(0xFF4ECDC4) // Teal
                        2 -> Color(0xFFFFE66D) // Yellow
                        3 -> Color(0xFF95E1D3) // Mint
                        else -> Color(0xFFFFA07A) // Light Salmon
                    },
                    size = Random.nextFloat() * 8f + 4f,
                    velocityX = (Random.nextFloat() - 0.5f) * 4f,
                    velocityY = Random.nextFloat() * 3f + 2f,
                    rotation = Random.nextFloat() * 360f,
                    rotationSpeed = (Random.nextFloat() - 0.5f) * 10f
                )
            }
            particles.value = newParticles
            animationProgress = 0f
            
            // Animate particles
            val startTime = System.currentTimeMillis()
            while (animationProgress < 1f) {
                val elapsed = System.currentTimeMillis() - startTime
                animationProgress = (elapsed.toFloat() / durationMillis).coerceIn(0f, 1f)
                kotlinx.coroutines.delay(16) // ~60fps
            }
        } else if (!visible) {
            particles.value = emptyList()
            animationProgress = 0f
        }
    }
    
    if (visible) {
        Canvas(
            modifier = modifier
                .fillMaxSize()
                .onGloballyPositioned { coordinates ->
                    canvasSize = with(density) {
                        Size(
                            coordinates.size.width.toFloat(),
                            coordinates.size.height.toFloat()
                        )
                    }
                }
        ) {
            particles.value.forEach { particle ->
                val progress = animationProgress
                val currentY = particle.y + particle.velocityY * progress * 100f
                val currentX = particle.x + particle.velocityX * progress * 100f
                val currentRotation = particle.rotation + particle.rotationSpeed * progress * 360f
                
                // Only draw if particle is still on screen
                if (currentY < this.size.height + 50 && currentX > -50 && currentX < this.size.width + 50) {
                    drawConfettiParticle(
                        center = Offset(currentX, currentY),
                        size = particle.size,
                        color = particle.color,
                        rotation = currentRotation,
                        alpha = (1f - progress).coerceIn(0f, 1f)
                    )
                }
            }
        }
    }
}

private fun DrawScope.drawConfettiParticle(
    center: Offset,
    size: Float,
    color: Color,
    rotation: Float,
    alpha: Float
) {
    // Draw a small square/rectangle confetti piece
    val halfSize = size / 2f
    val corners = listOf(
        Offset(-halfSize, -halfSize),
        Offset(halfSize, -halfSize),
        Offset(halfSize, halfSize),
        Offset(-halfSize, halfSize)
    )
    
    // Rotate corners around center
    val rotatedCorners = corners.map { corner ->
        val cos = cos(Math.toRadians(rotation.toDouble()))
        val sin = sin(Math.toRadians(rotation.toDouble()))
        val dx = corner.x * cos - corner.y * sin
        val dy = corner.x * sin + corner.y * cos
        Offset(center.x + dx.toFloat(), center.y + dy.toFloat())
    }
    
    // Draw the confetti piece
    drawLine(
        color = color.copy(alpha = alpha),
        start = rotatedCorners[0],
        end = rotatedCorners[1],
        strokeWidth = size * 0.8f
    )
    drawLine(
        color = color.copy(alpha = alpha),
        start = rotatedCorners[1],
        end = rotatedCorners[2],
        strokeWidth = size * 0.8f
    )
    drawLine(
        color = color.copy(alpha = alpha),
        start = rotatedCorners[2],
        end = rotatedCorners[3],
        strokeWidth = size * 0.8f
    )
    drawLine(
        color = color.copy(alpha = alpha),
        start = rotatedCorners[3],
        end = rotatedCorners[0],
        strokeWidth = size * 0.8f
    )
}

