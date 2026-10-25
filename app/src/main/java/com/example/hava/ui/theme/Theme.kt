package com.example.hava.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// ── Koyu tema paleti ──────────────────────────────────────────────────────────
val DarkColorScheme = darkColorScheme(
    primary          = Color(0xFF4FC3F7),
    onPrimary        = Color(0xFF001E2E),
    primaryContainer = Color(0xFF003549),
    secondary        = Color(0xFF90CAF9),
    onSecondary      = Color(0xFF001D32),
    background       = Color(0xFF050D1A),
    onBackground     = Color(0xFFE3EAF2),
    surface          = Color(0xFF0D1B2E),
    onSurface        = Color(0xFFE3EAF2),
    surfaceVariant   = Color(0xFF142035),
    onSurfaceVariant = Color(0xFFB0C4D8),
    error            = Color(0xFFFF6B6B),
    onError          = Color(0xFF1A0000),
    outline          = Color(0xFF2E4057)
)

// ── Açık tema paleti ─────────────────────────────────────────────────────────
val LightColorScheme = lightColorScheme(
    primary          = Color(0xFF0277BD),
    onPrimary        = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFB3E5FC),
    secondary        = Color(0xFF1565C0),
    onSecondary      = Color(0xFFFFFFFF),
    background       = Color(0xFFE8F4FD),
    onBackground     = Color(0xFF0D1B2E),
    surface          = Color(0xFFF0F8FF),
    onSurface        = Color(0xFF0D1B2E),
    surfaceVariant   = Color(0xFFD0EAFA),
    onSurfaceVariant = Color(0xFF1A3A5C),
    error            = Color(0xFFD32F2F),
    onError          = Color(0xFFFFFFFF),
    outline          = Color(0xFF90CAF9)
)

@Composable
fun HavaTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val ctx = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(ctx) else dynamicLightColorScheme(ctx)
    } else {
        if (darkTheme) DarkColorScheme else LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Typography,
        content     = content
    )
}