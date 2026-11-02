package com.app.plenka.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.app.plenka.R
import kotlinx.coroutines.delay

// Поза крышки в статичной иконке (ic_launcher_foreground.xml: rotate(-10) вокруг 104,196).
// Анимация начинается и заканчивается в ней, чтобы не было скачка при смене системного сплэша на этот экран.
private const val REST_ANGLE = -10f

// Системный сплэш рисует ic_launcher_foreground на 288dp, а содержимое иконки занимает
// 0.18 * 0.8 * 512 / 108 = 0.683 этой стороны. Здесь рисунок 512x512 => сторона бокса ≈ 197dp.
private val SPLASH_BOX_SIZE = 197.dp

/**
 * Функция анимированного запуска сплеш-экрана
 */
@Composable
fun AnimatedSplashScreen(
    onAnimationFinished: () -> Unit
) {
    val rotationAnim = remember { Animatable(REST_ANGLE) }
    val alphaAnim = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        // Небольшой замах из позы иконки
        rotationAnim.animateTo(
            targetValue = -25f,
            animationSpec = tween(durationMillis = 120, easing = FastOutSlowInEasing)
        )

        // Хлопок
        rotationAnim.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing)
        )
        delay(120)

        // Возврат в позу иконки
        rotationAnim.animateTo(
            targetValue = REST_ANGLE,
            animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
        )

        alphaAnim.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 300)
        )

        onAnimationFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F1020))
            .graphicsLayer { alpha = alphaAnim.value },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.size(SPLASH_BOX_SIZE),
            contentAlignment = Alignment.Center
        ) {
            // Неподвижная основа хлопушки
            Image(
                painter = painterResource(id = R.drawable.ic_clapper_base),
                contentDescription = "Plenka Base",
                modifier = Modifier.fillMaxSize()
            )

            // Подвижная крышка хлопушки, шарнир у левого края (104/512 по X, 196/512 по Y)
            Image(
                painter = painterResource(id = R.drawable.ic_clapper_top),
                contentDescription = "Plenka Top Lid",
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        rotationZ = rotationAnim.value
                        transformOrigin = TransformOrigin(104f / 512f, 196f / 512f)
                    }
            )
        }
    }
}