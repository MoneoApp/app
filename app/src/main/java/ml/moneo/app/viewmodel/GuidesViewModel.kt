package ml.moneo.app.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ml.moneo.DeviceByIdQuery
import ml.moneo.DeviceManualsByDeviceIdQuery
import ml.moneo.app.R
import ml.moneo.app.model.Guide
import ml.moneo.app.model.Remote
import ml.moneo.app.util.apolloClient

class GuidesViewModel : ViewModel() {
    private val availableGuides = MutableLiveData<List<Guide>>()

    private var selectedGuide = MutableLiveData<Guide?>()

    fun getAvailableGuides(): LiveData<List<Guide>> {
        return availableGuides
    }

    fun getSelectedGuide(): MutableLiveData<Guide?> {

        Log.d("products", "selected: " + selectedGuide)
        return selectedGuide
    }

    fun setSelectedGuide(guideId: String) {
        selectedGuide.value = availableGuides.value?.find { it.guideId == guideId }
    }

    fun resetSelectedGuide() {
        selectedGuide.value = null
    }

    fun getGuides(deviceId: String) {
        val client = apolloClient()

        GlobalScope.launch {
            val response = try {
                client.query(DeviceManualsByDeviceIdQuery(deviceId)).await()
            } catch (e: ApolloException) {
                return@launch
            }

            val device = response.data?.device
            if (device == null || response.hasErrors()) {
                println(response.errors?.firstOrNull()?.message);

                return@launch
            }

            var tempList: MutableList<Guide> = mutableListOf()
            device.manuals.forEach{ tempList.add(Guide(it.title, it.id, deviceId, R.drawable.default_guide)) }

            availableGuides.postValue(tempList)
        }
    }
}