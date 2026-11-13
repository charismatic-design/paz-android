package com.paz.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val ObsidianBg = Color(0xFF0B0F19)
val CardSurface = Color(0xFF161F30)
val CardSurfaceElevated = Color(0xFF1C273C)
val BorderStroke = Color(0xFF223049)
val GoldAccent = Color(0xFFD4AF37)
val GoldLight = Color(0xFFFDE68A)
val CyanAccent = Color(0xFF38BDF8)
val GreenBullish = Color(0xFF10B981)
val RedBearish = Color(0xFFF43F5E)
val TextPrimary = Color(0xFFF8FAFC)
val TextSecondary = Color(0xFF94A3B8)
val TextMuted = Color(0xFF64748B)

private val PazColorScheme = darkColorScheme(
    primary = GoldAccent,
    onPrimary = ObsidianBg,
    secondary = CyanAccent,
    onSecondary = ObsidianBg,
    background = ObsidianBg,
    onBackground = TextPrimary,
    surface = CardSurface,
    onSurface = TextPrimary,
    surfaceVariant = CardSurfaceElevated,
    onSurfaceVariant = TextSecondary,
    outline = BorderStroke
)

@Composable
fun PazTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = PazColorScheme,
        content = content
    )
}
