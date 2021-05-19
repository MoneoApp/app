package ml.moneo.app.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ml.moneo.app.R
import ml.moneo.app.model.Guide
import ml.moneo.app.model.Remote

class GuidesViewModel : ViewModel() {
    private val availableGuides = MutableLiveData<List<Guide>>().apply {
        value = getGuides()
    }

    private var selectedGuide = MutableLiveData<Guide?>()

    fun getGuidesByRemoteId(remoteId: Int): List<Guide>? {
        return availableGuides.value?.filter { it.remoteId == remoteId }
    }

    fun getSelectedGuide(): MutableLiveData<Guide?> {

        Log.d("products", "selected: " + selectedGuide)
        return selectedGuide
    }

    fun setSelectedGuide(guideId: Int) {
        selectedGuide.value = availableGuides.value?.find { it.guideId == guideId }
    }

    fun resetSelectedGuide() {
        selectedGuide.value = null
    }

    private fun getGuides(): List<Guide> {
        return listOf(
            Guide("Aflevering kijken", 1, 1, R.drawable.default_guide),
            Guide("Ander kanaal", 2, 1, R.drawable.default_guide),
            Guide("Televisie uitzetten", 3, 1, R.drawable.default_guide),

            Guide("Instellingen scherm openen", 4, 2, R.drawable.default_guide),
            Guide("Ander kanaal", 5, 2, R.drawable.default_guide),
            Guide("Televisie uitzetten", 6, 2, R.drawable.default_guide),
        )
    }
}