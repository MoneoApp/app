package ml.moneo.app.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import ml.moneo.app.util.openActivity
import ml.moneo.app.view.WelcomeView

class MainActivity : ComposeActivity() {
    @Composable
    override fun getContent() {
        WelcomeView()
    }

    override fun onCreate(bundle: Bundle?) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            openActivity(this, OnboardingActivity::class.java, true)
        }

        super.onCreate(bundle)
    }
}
