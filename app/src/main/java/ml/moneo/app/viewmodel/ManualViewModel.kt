package ml.moneo.app.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.google.ar.core.AugmentedImageDatabase
import com.google.ar.sceneform.math.Vector3
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ml.moneo.ManualByIdQuery
import ml.moneo.ManualByIdQuery.*
import ml.moneo.app.R
//import ml.moneo.app.model.*
import ml.moneo.app.util.apolloClient
import ml.moneo.type.InteractionType
import java.net.HttpURLConnection
import java.net.URL

class ManualViewModel : ViewModel() {
    private val manual = MutableLiveData<Manual>()
    private val bitmap = MutableLiveData<Bitmap>()
    private val currentStep = MutableLiveData(0)

    fun getManual(): LiveData<Manual>
    {
        return manual
    }

    fun getCurrentStep(): LiveData<Int> {
        return currentStep;
    }

    fun getDescription(): String {
        return currentStep.value?.let { manual.value!!.steps[it].text }!!
    }

    fun getInteractions(): List<Interaction1> {
        var positions = mutableListOf<Vector3>()

        return manual.value!!.steps[currentStep.value!!].interactions
    }

    fun loadBitmap() {
        try {
            GlobalScope.launch{
                val url = URL(getAnchorImageURL());
                var connection = url.openConnection() as HttpURLConnection;
                connection.doInput = true;
                connection.connect();
                val input = connection.inputStream;

                bitmap.postValue(BitmapFactory.decodeStream(input))
            }
        } catch (e: Exception) {
            // Log exception
            Log.e("Tagge", e.toString());
        }
    }

    fun getAnchorImageURL(): String {
        return "https://staging.moneo.ml/api/${manual.value!!.device.id}/anchor"
    }

    fun getBitmap(): LiveData<Bitmap>
    {
        return this.bitmap;
    }

    fun getAnchorPosition(): Interaction? {
        manual.value!!.device.interactions.forEach { it ->
            if(it.type == InteractionType.ANCHOR)
            {
                return it;
            }
        }

        return null;
    }

    fun next() {
        currentStep.value?.let {
            if (it >= manual.value!!.steps.count() - 1) {
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
        //manual.postValue(createTestManual())
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

            manual.postValue(tManual!!)
        }
    }

//    private fun createTestManual(): Manual {
//        return Manual(
//            id = "0",
//            name = "Test manual",
//            steps = listOf(
//                Step(
//                    "0",
//                    "Druk op de radio knop.",
//                    listOf(
//                        Interaction(
//                            "0",
//                            0.000,
//                            -0.006,
//                            1.0,
//                            1.0,
//                            "TestButton0",
//                            Overlay("0", "overlay0", listOf()),
//                            listOf()
//                        ),
//                        Interaction(
//                            "0",
//                            0.024,
//                            -0.006,
//                            1.0,
//                            1.0,
//                            "TestButton0",
//                            Overlay("0", "overlay0", listOf()),
//                            listOf()
//                        ),
//                        Interaction(
//                            "0",
//                            -0.024,
//                            -0.006,
//                            1.0,
//                            1.0,
//                            "TestButton0",
//                            Overlay("0", "overlay0", listOf()),
//                            listOf()
//                        ),
//                        Interaction(
//                            "0",
//                            0.024,
//                            0.018,
//                            1.0,
//                            1.0,
//                            "TestButton0",
//                            Overlay("0", "overlay0", listOf()),
//                            listOf()
//                        ),
//                        Interaction(
//                            "0",
//                            -0.024,
//                            0.018,
//                            1.0,
//                            1.0,
//                            "TestButton0",
//                            Overlay("0", "overlay0", listOf()),
//                            listOf()
//                        ),
//                        Interaction(
//                            "0",
//                            0.018,
//                            -0.024,
//                            3.0,
//                            1.0,
//                            "TestButton0",
//                            Overlay("0", "overlay0", listOf()),
//                            listOf()
//                        ),
//                        Interaction(
//                            "0",
//                            -0.018,
//                            -0.024,
//                            3.0,
//                            1.0,
//                            "TestButton0",
//                            Overlay("0", "overlay0", listOf()),
//                            listOf()
//                        )
//                    )
//                )
//            )
//        )
//    }
}
