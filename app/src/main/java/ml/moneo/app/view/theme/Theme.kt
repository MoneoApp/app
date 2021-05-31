package ml.moneo.app.view.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = Yellow
)

private val LightColorPalette = lightColors(
    primary = Yellow
)

@Composable
fun Theme(
    theme: String,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = when (theme) {
            "dark" -> DarkColorPalette
            "light" -> LightColorPalette
            else -> if (isSystemInDarkTheme()) DarkColorPalette else LightColorPalette
        },
        content = content
    )
}
