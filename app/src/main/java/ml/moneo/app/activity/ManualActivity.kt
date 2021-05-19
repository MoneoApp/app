package ml.moneo.app.activity

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ml.moneo.app.R
import ml.moneo.app.activity.fragment.ManualFragment

class ManualActivity : AppCompatActivity(R.layout.activity_main)
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var fragment = supportFragmentManager.findFragmentById(R.id.included_manual) as ManualFragment
        var bundle = Bundle()
        bundle.putString("MANUAL_ID", intent.getStringExtra("MANUAL_ID"))
        fragment.arguments = bundle

        //ALTERNATIVE
        //setContentView(R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)


    }
}
