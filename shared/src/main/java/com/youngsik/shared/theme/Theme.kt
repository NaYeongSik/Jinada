package com.youngsik.shared.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = SoftPink.copy(alpha = 0.8f), // 다크모드에서는 Primary를 살짝 연하게
    onPrimary = TextPrimary,
    primaryContainer = DarkerPinkText,
    onPrimaryContainer = LightPinkContainer,

    secondary = SoftMint.copy(alpha = 0.8f),
    onSecondary = TextPrimary,
    secondaryContainer = DarkerMintText,
    onSecondaryContainer = LightMintContainer,

    background = DarkBackground,
    surface = DarkSurface,
    onBackground = TextOnDark,
    onSurface = TextOnDark,
    onSurfaceVariant = TextSecondaryOnDark,
    outline = Color(0xFF444444)
)

private val LightColorScheme = lightColorScheme(
    primary = SoftPink,
    onPrimary = Color.White,
    primaryContainer = LightPinkContainer,
    onPrimaryContainer = DarkerPinkText,

    secondary = SoftMint,
    onSecondary = Color.White,
    secondaryContainer = LightMintContainer,
    onSecondaryContainer = DarkerMintText,

    background = AppBackground,
    surface = CardSurface,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary, // 선택 안 된 아이콘/텍스트 등
    outline = DividerColor // 카드 테두리, 구분선 등
)

@Composable
fun JinadaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}