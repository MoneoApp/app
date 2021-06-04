package ml.moneo.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ml.moneo.app.R
import ml.moneo.app.model.*

class HelpViewModel : ViewModel() {

    private val manual = createHelpManual();
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

    private fun createHelpManual(): HelpManual {
        return HelpManual(
            id = "0",
            name = "Test manual",
            steps = listOf(
                HelpStep(
                    "0",
                    "Op het eerste scherm kunt u een apparaat scannen door deze in beeld te houden.",
                    R.drawable.help_step_1
                ),
                HelpStep(
                    "1",
                    "Zodra het apparaat herkend wordt komt er een pop-up. Klik op 'Manual' als het gescande apparaat correct is.",
                    R.drawable.help_step_2
                ),
                HelpStep(
                    "2",
                    "Selecteer hierna het juiste modelnummer van uw apparaat.",
                    R.drawable.help_step_3
                ),
                HelpStep(
                    "3",
                    "Selecteer nu een van de handleidingen.",
                    R.drawable.help_step_4
                ),
                HelpStep(
                    "4",
                    "Volg nu de handleiding. Bij sommige stappen worden bijbehorende knoppen in groen opgelicht.",
                    R.drawable.help_step_5
                )
            )
        )
    }
}
