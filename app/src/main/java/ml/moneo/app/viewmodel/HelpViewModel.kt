package ml.moneo.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ml.moneo.app.R
import ml.moneo.app.model.*

class HelpViewModel : ViewModel() {

    private val manual = createTestManual();
    private val currentStep = MutableLiveData(0)

    fun getCurrentStep(): LiveData<Int> {
        return currentStep;
    }

    fun getDescription(): String {
        return currentStep.value?.let { manual.steps[it].description }!!
    }

    fun getImage(): Int {
        return currentStep.value?.let { manual.steps[it].drawable }!!
    }

    fun next() {
        currentStep.value?.let {
            if (it >= manual.getStepCount() - 1) {
                return
            } else {
                currentStep.value = it + 1;
            }
        };
    }

    fun previous() {
        currentStep.value?.let {
            if (it <= 0) {
                return
            } else {
                currentStep.value = it - 1;
            }
        };
    }

    private fun createTestManual(): HelpManual {
        return HelpManual(
            id = "0",
            name = "Test manual",
            steps = listOf(
                HelpStep(
                    "0",
                    "Pak een item waar u uitleg over wil krijgen",
                    R.drawable.white_remote
                ),
                HelpStep(
                    "1",
                    "Richt uw camera naar het item tot er een pop-up up het scherm komt te staan",
                    R.drawable.default_remote
                )
            )
        )
    }
}
