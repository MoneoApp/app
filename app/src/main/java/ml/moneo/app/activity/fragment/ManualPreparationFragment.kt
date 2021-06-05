package ml.moneo.app.activity.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import ml.moneo.app.R
import ml.moneo.app.databinding.FragmentManualBinding
import ml.moneo.app.viewmodel.ManualViewModel

class ManualPreparationFragment : Fragment() {
    private lateinit var manualViewModel: ManualViewModel
    private lateinit var binding: FragmentManualBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        manualViewModel = ViewModelProvider(activity as ViewModelStoreOwner).get(ManualViewModel::class.java)
        Log.d("Logge", "Galkoeojge")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Logge", "GHalojoejfojeofd")
    }

    fun initializeAR(manualId: String) {
        Log.d("Logge", "Hello!");
        manualViewModel.setupManual(manualId)

        Log.d("Logge", "Hello!");

        manualViewModel.getManual().observe(viewLifecycleOwner, {
            manualViewModel.loadBitmap()
        })

        manualViewModel.getBitmap().observe(viewLifecycleOwner, {

        })
    }
}
