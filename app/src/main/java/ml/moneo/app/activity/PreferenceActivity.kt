package ml.moneo.app.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ml.moneo.app.R
import ml.moneo.app.activity.fragment.PreferenceFragment

class PreferenceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)
    }
}