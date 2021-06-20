package ml.moneo.app.activity

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ml.moneo.app.R
import ml.moneo.app.databinding.ActivityHelpBinding

class HelpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHelpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("ONCREATE123", "reeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee")
        super.onCreate(savedInstanceState)

        binding = ActivityHelpBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.helpFragment.findViewById<Button>(R.id.steps_close_button).setOnClickListener {
            Log.d("ONCREATE123", "REEEBUTOONCLICKKED")
        }
    }
}