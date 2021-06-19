package ml.moneo.app.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import ml.moneo.app.R
import ml.moneo.app.activity.fragment.ManualFragment
import ml.moneo.app.view.component.AnchorPopup
import ml.moneo.app.viewmodel.ManualViewModel

class ManualActivity : AppCompatActivity()
{
    private lateinit var manualViewModel: ManualViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide();

        manualViewModel = ViewModelProvider(this).get(ManualViewModel::class.java)

        intent.getStringExtra("MANUAL_ID")?.let { initializeAR(it) }
    }

    fun initializeAR(manualId: String) {
        manualViewModel.setupManual(manualId)

        manualViewModel.getManual().observe(this, {
            manualViewModel.loadBitmap()
        })

        manualViewModel.getBitmap().observe(this, {
            setContentView(R.layout.activity_main);
            var deviceId = ""


            val fragment = supportFragmentManager.findFragmentById(R.id.included_manual) as ManualFragment

            intent.getStringExtra("DEVICE_ID")?.let { deviceId = it }

            val sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(applicationContext)

            findViewById<ComposeView>(R.id.my_composable).setContent {
                AnchorPopup(sharedPreferences.getString("theme", "system")!!, "https://moneo.houf.io/api/${deviceId}/anchor")
            }
        })
    }
}
