package ml.moneo.app.activity

import androidx.compose.runtime.Composable
import ml.moneo.app.view.OnboardingView

class OnboardingActivity : ComposeActivity() {
    @Composable
    override fun getContent() {
        OnboardingView()
    }
}
