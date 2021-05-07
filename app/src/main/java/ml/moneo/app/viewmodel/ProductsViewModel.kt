package ml.moneo.app.viewmodel

import android.util.Log
import androidx.lifecycle.*
import ml.moneo.app.R
import ml.moneo.app.model.Guide
import ml.moneo.app.model.Product
import ml.moneo.app.model.Remote

class ProductsViewModel : ViewModel() {
    private val availableProducts = MutableLiveData<List<Remote>>().apply {
        value = getProducts()
    }
    private val availableGuides = MutableLiveData<List<Guide>>().apply {
        value = getGuides()
    }

    fun getAllProducts(): LiveData<List<Remote>> {
        return availableProducts
    }

    fun getGuidesByRemoteId(remoteId: Int): List<Guide>? {
        return availableGuides.value?.filter { it.remoteId == remoteId}
    }

    //Update the searched products
//    private var searchString = MutableLiveData<String>()
//    fun getSearchResults(searchString: String): LiveData<List<Product>> {
//        return Transformations.map(availableProducts) { it ->
//            if (!searchString.isEmpty()) {
//                it.filter {
//                    it.name.contains(searchString)
//                }
//            }
//        }
//    }

//    fun setSearchString(name: String) {
//        searchString.value = name
//    }

    private fun getProducts(): List<Remote> {
        return listOf(
            Remote("Samsung 3212-GHIU-90", 1, R.drawable.default_remote),
            Remote("KPN Z-NH-TYU", 2, R.drawable.default_remote),
            Remote("One For All URC6820", 3, R.drawable.default_remote),
            Remote("Logitech G567", 4, R.drawable.default_remote),
            Remote("Logitech G609FGH", 5, R.drawable.default_remote),
            Remote("Samsung 32KL344JK", 6, R.drawable.default_remote),
            Remote("Logitech GRT676HN", 7, R.drawable.default_remote),
            Remote("KPN Remoto", 8, R.drawable.default_remote)
        )
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