package ml.moneo.app.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ComposeView
import androidx.preference.PreferenceManager
import ml.moneo.app.R
import ml.moneo.app.activity.fragment.ManualFragment
import ml.moneo.app.view.component.AnchorPopup

class ManualActivity : AppCompatActivity(R.layout.activity_main)
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var deviceId = ""

        val fragment = supportFragmentManager.findFragmentById(R.id.included_manual) as ManualFragment
        intent.getStringExtra("MANUAL_ID")?.let { fragment.initializeAR(it) }
        intent.getStringExtra("DEVICE_ID")?.let { deviceId = it }

        val sharedPreferences = PreferenceManager
            .getDefaultSharedPreferences(applicationContext)

        findViewById<ComposeView>(R.id.my_composable).setContent {
            AnchorPopup(sharedPreferences.getString("theme", "system")!!, "https://staging.moneo.ml/api/${deviceId}/anchor")
        }

        //ALTERNATIVE
        //setContentView(R.layout.activity_main)
    }
}
