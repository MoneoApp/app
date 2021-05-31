package ml.moneo.app.activity

import android.Manifest
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import ml.moneo.app.activity.fragment.PreferenceFragment
import ml.moneo.app.util.openActivity
import ml.moneo.app.view.WelcomeView


class MainActivity : ComposeActivity() {
    @Composable
    override fun GetContent() {
        WelcomeView()
    }

    override fun onCreate(bundle: Bundle?) {
        val sharedPreferences = PreferenceManager
            .getDefaultSharedPreferences(applicationContext)

        setDefaultSettings(sharedPreferences)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            openActivity(this, OnboardingActivity::class.java, true)
        }

        super.onCreate(bundle)
    }

    private fun setDefaultSettings(sharedPreferences: SharedPreferences?) {
        if (sharedPreferences != null) {
            PreferenceFragment.setAppNightMode(sharedPreferences.getString("theme", "system")!!)
        }
    }
}
