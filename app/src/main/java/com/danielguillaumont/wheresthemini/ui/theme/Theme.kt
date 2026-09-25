package com.danielguillaumont.wheresthemini.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val WheresTheMiniColorScheme = lightColorScheme(
    primary = MiniCitron,
    onPrimary = BonnetBlack,

    secondary = BonnetBlack,
    onSecondary = WarmCream,

    tertiary = BritishRed,
    onTertiary = TicketPaper,

    background = WarmCream,
    onBackground = BonnetBlack,

    surface = TicketPaper,
    onSurface = BonnetBlack,

    surfaceVariant = MiniCitron,
    onSurfaceVariant = BonnetBlack,

    error = BritishRed,
    onError = TicketPaper
)

@Composable
fun WheresTheMiniTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = WheresTheMiniColorScheme,
        typography = Typography,
        content = content
    )
}