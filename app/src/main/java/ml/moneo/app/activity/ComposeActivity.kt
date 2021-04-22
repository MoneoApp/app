package ml.moneo.app.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.core.view.WindowCompat
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets
import ml.moneo.app.R
import ml.moneo.app.view.theme.Theme

abstract class ComposeActivity(private val content: @Composable () -> Unit) : ComponentActivity() {
    override fun onCreate(bundle: Bundle?) {
        setTheme(R.style.Theme_Moneo)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        super.onCreate(bundle)

        setContent {
            ProvideWindowInsets {
                Theme {
                    content()
                }
            }
        }
    }
}
