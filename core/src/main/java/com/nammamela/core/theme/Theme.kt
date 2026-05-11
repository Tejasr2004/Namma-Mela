package com.nammamela.core.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val AppleLightColorScheme = lightColorScheme(
    primary = SaffronAccent,
    secondary = TealAccent,
    tertiary = MarigoldAccent,
    background = AppleSystemGrey,
    surface = AppleWhite,
    onPrimary = AppleWhite,
    onSecondary = AppleWhite,
    onTertiary = AppleWhite,
    onBackground = AppleTextPrimary,
    onSurface = AppleTextPrimary,
)

@Composable
fun NammaMelaTheme(
    darkTheme: Boolean = false, // Forced Light Mode as requested
    content: @Composable () -> Unit
) {
    val colorScheme = AppleLightColorScheme
    val view = LocalView.current
    
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Assuming Typography is standard or updated
        content = content
    )
}
