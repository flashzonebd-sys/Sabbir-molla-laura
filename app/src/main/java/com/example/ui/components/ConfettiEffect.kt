package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import com.example.ui.theme.NoraCoralPink
import com.example.ui.theme.NoraElectricViolet
import com.example.ui.theme.NoraMintGreen
import com.example.ui.theme.NoraPrimaryPurple
import com.example.ui.theme.NoraSkyCyan
import com.example.ui.theme.NoraSunshineYellow
import kotlin.random.Random

data class ConfettiPiece(
    val startX: Float,
    val speedX: Float,
    val speedY: Float,
    val color: Color,
    val size: Float,
    val rotationSpeed: Float
)

@Composable
fun ConfettiEffect(
    modifier: Modifier = Modifier,
    particleCount: Int = 45,
    onFinished: () -> Unit = {}
) {
    val progress = remember { Animatable(0f) }

    val colors = listOf(
        NoraPrimaryPurple, NoraElectricViolet, NoraSkyCyan,
        NoraCoralPink, NoraSunshineYellow, NoraMintGreen
    )

    val pieces = remember {
        List(particleCount) {
            ConfettiPiece(
                startX = Random.nextFloat(),
                speedX = (Random.nextFloat() - 0.5f) * 160f,
                speedY = 350f + Random.nextFloat() * 450f,
                color = colors[Random.nextInt(colors.size)],
                size = 10f + Random.nextFloat() * 14f,
                rotationSpeed = (Random.nextFloat() - 0.5f) * 720f
            )
        }
    }

    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1800, easing = LinearEasing)
        )
        onFinished()
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val t = progress.value

        for (piece in pieces) {
            val x = (piece.startX * w) + (piece.speedX * t)
            val y = (t * (h + 100f)) - 50f
            val alpha = (1f - (t * 0.9f)).coerceIn(0f, 1f)

            drawRect(
                color = piece.color.copy(alpha = alpha),
                topLeft = Offset(x, y),
                size = Size(piece.size, piece.size * 1.5f)
            )
        }
    }
}
