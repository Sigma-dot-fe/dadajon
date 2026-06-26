package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.random.Random

data class Particle(
    var x: Float,
    var y: Float,
    var speed: Float,
    var size: Float,
    var alpha: Float,
    val isHeart: Boolean,
    val color: Color
)

@Composable
fun FestiveParticleOverlay(modifier: Modifier = Modifier, enabled: Boolean = true) {
    if (!enabled) return

    val particles = remember { mutableStateListOf<Particle>() }

    val colors = remember {
        listOf(
            Color(0xFFFF1744), // Crimson Red
            Color(0xFFFF4081), // Pink Rose
            Color(0xFFFFB300), // Gold Amber
            Color(0xFFFF8A80), // Soft Coral
            Color(0xFFFFD54F)  // Warm Yellow Gold
        )
    }

    LaunchedEffect(Unit) {
        if (particles.isEmpty()) {
            for (i in 0..35) {
                particles.add(
                    Particle(
                        x = Random.nextFloat(),
                        y = Random.nextFloat() * 1.2f - 0.2f,
                        speed = 0.05f + Random.nextFloat() * 0.12f,
                        size = 12f + Random.nextFloat() * 24f,
                        alpha = 0.3f + Random.nextFloat() * 0.7f,
                        isHeart = Random.nextBoolean(),
                        color = colors.random()
                    )
                )
            }
        }

        while (true) {
            withFrameMillis { _ ->
                for (i in particles.indices) {
                    val p = particles[i]
                    p.y += p.speed * 0.015f
                    p.x += (Random.nextFloat() - 0.5f) * 0.003f

                    if (p.x < 0f) p.x = 1f
                    if (p.x > 1f) p.x = 0f

                    if (p.y > 1.1f) {
                        p.y = -0.1f
                        p.x = Random.nextFloat()
                    }
                }
            }
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        particles.forEach { p ->
            val px = p.x * width
            val py = p.y * height
            if (p.isHeart) {
                drawHeart(
                    offset = Offset(px, py),
                    size = p.size,
                    color = p.color.copy(alpha = p.alpha)
                )
            } else {
                drawStar(
                    offset = Offset(px, py),
                    size = p.size * 0.8f,
                    color = p.color.copy(alpha = p.alpha)
                )
            }
        }
    }
}

private fun DrawScope.drawHeart(offset: Offset, size: Float, color: Color) {
    val x = offset.x
    val y = offset.y
    val path = androidx.compose.ui.graphics.Path().apply {
        moveTo(x, y + size * 0.35f)
        cubicTo(x, y, x - size * 0.5f, y, x - size * 0.5f, y + size * 0.35f)
        cubicTo(x - size * 0.5f, y + size * 0.7f, x, y + size, x, y + size)
        cubicTo(x, y + size, x + size * 0.5f, y + size * 0.7f, x + size * 0.5f, y + size * 0.35f)
        cubicTo(x + size * 0.5f, y, x, y, x, y + size * 0.35f)
        close()
    }
    drawPath(path = path, color = color)
}

private fun DrawScope.drawStar(offset: Offset, size: Float, color: Color) {
    val x = offset.x
    val y = offset.y
    val path = androidx.compose.ui.graphics.Path().apply {
        moveTo(x, y - size)
        lineTo(x + size * 0.25f, y - size * 0.25f)
        lineTo(x + size, y)
        lineTo(x + size * 0.25f, y + size * 0.25f)
        lineTo(x, y + size)
        lineTo(x - size * 0.25f, y + size * 0.25f)
        lineTo(x - size, y)
        lineTo(x - size * 0.25f, y - size * 0.25f)
        close()
    }
    drawPath(path = path, color = color)
}
