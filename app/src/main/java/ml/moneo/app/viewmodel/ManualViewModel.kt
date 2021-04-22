package ml.moneo.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.ar.sceneform.math.Vector3
import ml.moneo.app.model.Interaction
import ml.moneo.app.model.Manual
import ml.moneo.app.model.Overlay
import ml.moneo.app.model.Step

class ManualViewModel : ViewModel() {
    private val manual = createTestManual();
    private val currentStep = MutableLiveData(0)

    fun getCurrentStep(): LiveData<Int> {
        return currentStep;
    }

    fun getDescription(): String {
        return currentStep.value?.let { manual.steps[it].description }!!
    }

    fun getButtonPosition(): Vector3
    {
        return currentStep.value?.let { Vector3(manual.steps[it].interaction.x, 0.0f, manual.steps[it].interaction.y) }!!
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

    private fun createTestManual(): Manual {

        return Manual(
            id = "0",
            name = "Test manual",
            steps = listOf(
                Step(
                    "0",
                    "Zet de afstandbediening aan.",
                    Interaction(
                        "0",
                        0f,
                        0.006f,
                        10f,
                        10f,
                        "TestButton0",
                        Overlay("0", "overlay0", listOf()),
                        listOf()
                    )
                ),
                Step(
                    "1",
                    "Druk op de 'Coole knop' die aangegeven is in het rood.",
                    Interaction(
                        "1",
                        .0f,
                        -.006f,
                        10f,
                        10f,
                        "TestButton1",
                        Overlay("1", "overlay1", listOf()),
                        listOf()
                    )
                ),
                Step(
                    "2",
                    "Druk nu op de 'Nog coolere knop' die ook aangegeven is in het rood",
                    Interaction(
                        "2",
                        .0f,
                        -.018f,
                        10f,
                        10f,
                        "TestButton2",
                        Overlay("2", "overlay2", listOf()),
                        listOf()
                    )
                ),
                Step(
                    "3",
                    "Zet nu de afstandsbediening uit door de uitknop in te drukken.",
                    Interaction(
                        "3",
                        .0f,
                        .006f,
                        10f,
                        10f,
                        "TestButton3",
                        Overlay("3", "overlay3", listOf()),
                        listOf()
                    )
                ),
                Step(
                    "4",
                    "Bedenk dat die laatste stap eigenlijk niet zo slim was, en zet de afstandsbediening toch maar weer aan door de aanknop in te drukken.",
                    Interaction(
                        "4",
                        .0f,
                        -.006f,
                        10f,
                        10f,
                        "TestButton4",
                        Overlay("4", "overlay4", listOf()),
                        listOf()
                    )
                ),
                Step(
                    "5",
                    "Gefeliciteerd. Je hebt zojuist de afstandsbediening(Dus niet de televisie zelf) aan, daarna uit en tot slot weer aan gezet. Dat heb je heel goed gedaan.",
                    Interaction(
                        "5",
                        .0f,
                        -.018f,
                        10f,
                        10f,
                        "TestButton5",
                        Overlay("5", "overlay5", listOf()),
                        listOf()
                    )
                )
            )
        )
    }
}
