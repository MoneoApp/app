package ml.moneo.app.viewmodel

import androidx.lifecycle.*
import ml.moneo.app.R
import ml.moneo.app.model.Guide
import ml.moneo.app.model.Remote

class ProductsViewModel : ViewModel() {
    private val availableProducts = MutableLiveData<List<Remote>>().apply {
        value = getProducts()
    }
    private var selectedProduct = MutableLiveData<Remote>()
    private val productSearchString = MutableLiveData<String>()

    fun getAllProducts(): LiveData<List<Remote>> {
        return availableProducts
    }

    fun setSelectedProduct(remoteId: Int) {
        selectedProduct.value = availableProducts.value?.find { it.remoteId == remoteId }
    }

    fun getSelectedProduct(): LiveData<Remote> {
        return selectedProduct
    }

    fun setProductSearchString(searchString: String) {
        productSearchString.value = searchString
    }

    fun getProductSearchString(): MutableLiveData<String> {
        return productSearchString
    }

    fun resetSelectedProduct(){
        selectedProduct = MutableLiveData<Remote>()
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
}