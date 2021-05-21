package ml.moneo.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CatalogViewModel : ViewModel() {

    enum class CATALOGTYPES {
        PRODUCTS,
        GUIDES
    }

    private val currentView = MutableLiveData<Int>()

    fun setCurrentView(viewType: CATALOGTYPES) {
        if (viewType == CATALOGTYPES.PRODUCTS) {
            currentView.value = 0

        } else if (viewType == CATALOGTYPES.GUIDES) {
            currentView.value = 1
        }
    }

    fun getCurrentView(): LiveData<Int> {
        return currentView
    }
}