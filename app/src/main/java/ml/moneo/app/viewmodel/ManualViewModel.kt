package ml.moneo.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.google.ar.sceneform.math.Vector3
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ml.moneo.DeviceByIdQuery
import ml.moneo.ManualByIdQuery
import ml.moneo.app.R
import ml.moneo.app.model.*
import ml.moneo.app.util.apolloClient

class ManualViewModel : ViewModel() {
    private val manual = MutableLiveData<Manual>()
    private val currentStep = MutableLiveData(0)

    fun getManual(): LiveData<Manual>
    {
        return manual
    }

    fun getCurrentStep(): LiveData<Int> {
        return currentStep;
    }

    fun getDescription(): String {
        return currentStep.value?.let { manual.value!!.steps[it].description }!!
    }

    fun getButtonPosition(): List<Vector3> {
        var positions = mutableListOf<Vector3>()

        manual.value!!.steps[currentStep.value!!].interaction.forEach {
            positions.add(
                Vector3(
                    it.x.toFloat(),
                    0.0f,
                    it.y.toFloat()
                )
            )
        }

        return positions
    }

    fun next() {
        currentStep.value?.let {
            if (it >= manual.value!!.getStepCount() - 1) {
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

    fun setupManual(manualId: String)
    {
        val client = apolloClient()

        GlobalScope.launch {
            val response = try {
                client.query(ManualByIdQuery(manualId)).await()
            } catch (e: ApolloException) {
                return@launch
            }

            val tManual = response.data?.manual
            if (tManual == null || response.hasErrors()) {
                println(response.errors?.firstOrNull()?.message);

                return@launch
            }

            var steps = mutableListOf<Step>()
            tManual.steps.forEach{ step ->

                var interactions = mutableListOf<Interaction>()

                step.interactions.forEach{ interaction ->

                    interactions.add(Interaction(
                        interaction.id,
                        interaction.x,
                        interaction.y,
                        interaction.width,
                        interaction.height,
                        interaction.title,
                        Overlay("0", "overlay0", listOf()),
                        listOf()
                    ))
                }

                steps.add(Step(
                    step.id,
                    step.text,
                    interactions
                ))
            }

            manual.postValue(Manual(
                id = tManual.id,
                name = tManual.title,
                steps = steps
            ))
        }
    }

//    private fun createTestManual(): Manual {
//        return Manual(
//            id = "0",
//            name = "Test manual",
//            steps = listOf(
//                Step(
//                    "0",
//                    "Zet de afstandbediening aan.",
//                    Interaction(
//                        "0",
//                        0.006f,
//                        0f,
//                        10f,
//                        10f,
//                        "TestButton0",
//                        Overlay("0", "overlay0", listOf()),
//                        listOf()
//                    )
//                ),
//                Step(
//                    "1",
//                    "Druk op de 'Coole knop' die aangegeven is in het rood.",
//                    Interaction(
//                        "1",
//                        -.006f,
//                        .0f,
//                        10f,
//                        10f,
//                        "TestButton1",
//                        Overlay("1", "overlay1", listOf()),
//                        listOf()
//                    )
//                ),
//                Step(
//                    "2",
//                    "Druk nu op de 'Nog coolere knop' die ook aangegeven is in het rood",
//                    Interaction(
//                        "2",
//                        -.018f,
//                        .0f,
//                        10f,
//                        10f,
//                        "TestButton2",
//                        Overlay("2", "overlay2", listOf()),
//                        listOf()
//                    )
//                ),
//                Step(
//                    "3",
//                    "Zet nu de afstandsbediening uit door de uitknop in te drukken.",
//                    Interaction(
//                        "3",
//                        .006f,
//                        .0f,
//                        10f,
//                        10f,
//                        "TestButton3",
//                        Overlay("3", "overlay3", listOf()),
//                        listOf()
//                    )
//                ),
//                Step(
//                    "4",
//                    "Bedenk dat die laatste stap eigenlijk niet zo slim was, en zet de afstandsbediening toch maar weer aan door de aanknop in te drukken.",
//                    Interaction(
//                        "4",
//                        -.006f,
//                        .0f,
//                        10f,
//                        10f,
//                        "TestButton4",
//                        Overlay("4", "overlay4", listOf()),
//                        listOf()
//                    )
//                ),
//                Step(
//                    "5",
//                    "Gefeliciteerd. Je hebt zojuist de afstandsbediening(Dus niet de televisie zelf) aan, daarna uit en tot slot weer aan gezet. Dat heb je heel goed gedaan.",
//                    Interaction(
//                        "5",
//                        -.018f,
//                        .0f,
//                        10f,
//                        10f,
//                        "TestButton5",
//                        Overlay("5", "overlay5", listOf()),
//                        listOf()
//                    )
//                )
//            )
//        )
//    }
}
