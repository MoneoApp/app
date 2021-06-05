package ml.moneo.app.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.ViewModelStoreOwner
import androidx.preference.PreferenceManager
import ml.moneo.app.R
import ml.moneo.app.activity.fragment.ManualFragment
import ml.moneo.app.activity.fragment.ManualPreparationFragment
import ml.moneo.app.view.component.AnchorPopup

class ManualActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("Logge", "Hibabe")

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_prep);

        Log.d("Logge", "Hibabe")

//        var deviceId = ""
//
//        Log.d("Logge", "Hibabe")
//
////        val fragment = supportFragmentManager.findFragmentById(R.id.included_manual_prep) as ManualPreparationFragment
////        Log.d("Logge", fragment.toString())
////        intent.getStringExtra("MANUAL_ID")?.let { Log.d("Logge", it) }
////
////        intent.getStringExtra("MANUAL_ID")?.let { fragment.initializeAR(it) }
////        intent.getStringExtra("DEVICE_ID")?.let { deviceId = it }
//
//        val sharedPreferences = PreferenceManager
//            .getDefaultSharedPreferences(applicationContext)
//
//        findViewById<ComposeView>(R.id.my_composable).setContent {
//            AnchorPopup(sharedPreferences.getString("theme", "system")!!, "https://staging.moneo.ml/api/${deviceId}/anchor")
//        }

        //ALTERNATIVE
    }
}
