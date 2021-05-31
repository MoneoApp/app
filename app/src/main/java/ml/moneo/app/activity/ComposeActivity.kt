package ml.moneo.app.activity

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.core.view.WindowCompat
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets
import ml.moneo.app.R
import ml.moneo.app.activity.fragment.PreferenceFragment
import ml.moneo.app.view.theme.Theme

abstract class ComposeActivity : ComponentActivity(), SharedPreferences.OnSharedPreferenceChangeListener {
    private val currentTheme = MutableLiveData("")

    override fun onCreate(bundle: Bundle?) {
        setTheme(R.style.Theme_Moneo)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val sharedPreferences = PreferenceManager
            .getDefaultSharedPreferences(applicationContext)

        sharedPreferences.registerOnSharedPreferenceChangeListener(this)

        setDefaultSettings(sharedPreferences)

        super.onCreate(bundle)

        setContent {
            ProvideWindowInsets {
                val theme by currentTheme.observeAsState()
                Theme(theme!!) {
                    GetContent()
                }
            }
        }
    }

    @Composable
    abstract fun GetContent()

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (sharedPreferences != null && key == "theme") {
            currentTheme.value = sharedPreferences.getString(key, "system")
        }
    }

    private fun setDefaultSettings(sharedPreferences: SharedPreferences?) {
        if (sharedPreferences != null) {
            val theme = sharedPreferences.getString("theme", "system")
            currentTheme.value = theme
            PreferenceFragment.setAppNightMode(theme!!)
        }
    }
}
