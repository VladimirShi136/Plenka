package com.app.plenka.ui.theme

import androidx.compose.ui.graphics.Color

/*
 * Палитра Plenka. Источник правды для Compose.
 * Для XML (окно, сплэш, виджеты) те же значения лежат в res/values/colors.xml,
 * при изменении цвета правьте оба файла.
 */

// ---------- Тёмная тема (основная) ----------
val DarkBackground = Color(0xFF0F1020)
val DarkSurfaceLowest = Color(0xFF0A0B18)
val DarkSurfaceLow = Color(0xFF141526)
val DarkSurface = Color(0xFF1A1B2E)          // карточки
val DarkSurfaceHigh = Color(0xFF22233A)
val DarkSurfaceHighest = Color(0xFF2A2C45)
val DarkSurfaceVariant = Color(0xFF25263D)
val DarkOutline = Color(0xFF4A4C66)
val DarkOutlineVariant = Color(0xFF2E2F48)

val DarkOnBackground = Color(0xFFF5F5F7)
val DarkMuted = Color(0xFF9A9CB0)            // приглушённый текст

val DarkPrimary = Color(0xFFFFB400)          // янтарный
val DarkOnPrimary = Color(0xFF0F1020)
val DarkPrimaryContainer = Color(0xFF4A3700)
val DarkOnPrimaryContainer = Color(0xFFFFE0A0)

val DarkSecondary = Color(0xFFFF5A5F)        // коралловый
val DarkOnSecondary = Color(0xFF0F1020)
val DarkSecondaryContainer = Color(0xFF5A1F22)
val DarkOnSecondaryContainer = Color(0xFFFFD9DA)

val DarkError = Color(0xFFFF6B6B)
val DarkOnError = Color(0xFF0F1020)
val DarkErrorContainer = Color(0xFF5C1A1A)
val DarkOnErrorContainer = Color(0xFFFFDAD6)

// ---------- Светлая тема ----------
val LightBackground = Color(0xFFFAF8F5)
val LightSurfaceLowest = Color(0xFFFFFFFF)
val LightSurfaceLow = Color(0xFFFFFFFF)
val LightSurface = Color(0xFFFFFFFF)         // карточки
val LightSurfaceHigh = Color(0xFFF3F0EA)
val LightSurfaceHighest = Color(0xFFEBE7DF)
val LightSurfaceVariant = Color(0xFFEEEAE3)
val LightOutline = Color(0xFF9A98A8)
val LightOutlineVariant = Color(0xFFDDD9D0)

val LightOnBackground = Color(0xFF1A1B2E)
val LightMuted = Color(0xFF666880)

val LightPrimary = Color(0xFFB87500)         // тёмный янтарь
val LightOnPrimary = Color(0xFF0F1020)       // тёмный текст: контраст 5.0 (белый дал бы 3.8)
val LightPrimaryContainer = Color(0xFFFFE3A8)
val LightOnPrimaryContainer = Color(0xFF3A2800)

val LightSecondary = Color(0xFFE0434A)
val LightOnSecondary = Color(0xFF0F1020)     // контраст 4.6
val LightSecondaryContainer = Color(0xFFFFD9DA)
val LightOnSecondaryContainer = Color(0xFF4A0F12)

val LightError = Color(0xFFB3261E)
val LightOnError = Color(0xFFFFFFFF)
val LightErrorContainer = Color(0xFFFFDAD6)
val LightOnErrorContainer = Color(0xFF410002)