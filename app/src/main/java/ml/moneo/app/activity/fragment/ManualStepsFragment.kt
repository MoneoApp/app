package ml.moneo.app.activity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ml.moneo.app.R
import ml.moneo.app.databinding.FragmentHelpBinding
import ml.moneo.app.viewmodel.HelpViewModel

class ManualStepsFragment : Fragment() {
    private lateinit var helpViewModel: HelpViewModel
    private lateinit var binding: FragmentHelpBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentHelpBinding.inflate(inflater, container, false)
        helpViewModel = ViewModelProvider(this).get(HelpViewModel::class.java)

        helpViewModel.getCurrentStep().observe(viewLifecycleOwner, {
            binding.steps.manualTextview.text =
                getString(R.string.manual_step, it + 1, helpViewModel.getDescription())
            binding.guideImage.setBackgroundResource(helpViewModel.getImage())
        })

        binding.steps.nextButton.setOnClickListener {
            helpViewModel.next()
        }

        binding.steps.previousButton.setOnClickListener {
            helpViewModel.previous()
        }

        return binding.root
    }
}
